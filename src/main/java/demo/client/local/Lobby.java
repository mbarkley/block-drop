package demo.client.local;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.TransitionTo;

import demo.client.local.Board;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import demo.client.shared.GameRoom;
import demo.client.shared.Invitation;
import demo.client.shared.LobbyUpdate;
import demo.client.shared.LobbyUpdateRequest;
import demo.client.shared.Player;
import demo.client.shared.RegisterRequest;

/*
 * A class for the UI for a tic-tac-toe Lobby.
 */
//@Page(role=DefaultPage.class)
@Page
public class Lobby extends Composite {
	
	/*
	 * A class for handling lobby updates and invitation from the server.
	 */
	private final class LobbyMessageCallback implements MessageCallback {
		/*
		 * (non-Javadoc)
		 * @see org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client.api.messaging.Message)
		 */
		@Override
		public void callback(Message message) {
			if (message.getCommandType().equals("invitation"))
				invitationCallback(message);
			else if (message.getCommandType().equals("start-game"))
				startGameCallback(message);
		}
		
		/*
		 * Start a game with another player after a successfully accepted invitation.
		 */
		private void startGameCallback(Message message) {
			Client.getInstance().setGame(message.get(GameRoom.class, MessageParts.Value));
			// For debugging.
			System.out.println(Client.getInstance().getNickname()+": Before board transition.");
			boardTransition.go();
			// For debugging.
			System.out.println(Client.getInstance().getNickname()+": After board transition.");
		}

		/*
		 * Prompt the user to respond to an invitation from another client.
		 */
		private void invitationCallback(Message message) {
			Invitation invitation = message.get(Invitation.class, MessageParts.Value);
			
			invitation.setAccepted(
				Window.confirm("You have been invited to play a game by "
						+ invitation.getInviter().getNick()
						+ ". Would you like to accept?"
				)
			);
			
			// The server handles the invitation, whether or not it was accepted.
			MessageBuilder.createMessage()
			.toSubject("Relay")
			.command("invitation-response")
			.withValue(invitation)
			.noErrorHandling()
			.sendNowWith(dispatcher);
		}
	}
	
	/* For the Errai NavigationUI. */
	@Inject private TransitionTo<Board> boardTransition;
	/* For requesting lobby updates from the server. */
	@Inject	private Event<LobbyUpdateRequest> lobbyUpdateRequest;
	/* For registering this client with the server. */
	@Inject	private Event<RegisterRequest> registerRequest;
	/* For inviting another user to play a game. */
	@Inject private Event<Invitation> gameInvitation;
	/* For receiving messages from the server. */
	private MessageBus messageBus = ErraiBus.get();
	/* For sending messages to the server. */
	private RequestDispatcher dispatcher = ErraiBus.getDispatcher();

	/* The base UI element for this class. */
	private VerticalPanel vPanel = new VerticalPanel();
	/* A panel for buttons used for lobby-related actions. */
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	/* The UI element containing the list of users in the lobby. */
	private VerticalPanel lobbyPanel = new VerticalPanel();
	/* A button for users to initially join the lobby. */
	private Button lobbyButton = new Button("Join Lobby");
	
	/* Shadows lobbyPanel with corresponding player objects. */
	private List<Player> lobbyList = new ArrayList<Player>();
	
	/*
	 * Create an instance of a lobby page.
	 */
	public Lobby() {
		initWidget(vPanel);
	}
	
	/*
	 * Construct the UI elements for the lobby.
	 */
	@PostConstruct
	public void postConstruct() {
		vPanel.add(lobbyPanel);
		
		// If this user has not been to the lobby previously, display a button to join the lobby.
		if (Client.getInstance().getPlayer() == null) {
			buttonPanel.add(lobbyButton);
			vPanel.add(buttonPanel);
			lobbyButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					joinLobby();
				}
			});
		} else {
			joinLobby();
		}
	}
	
	/*
	 * Request an update of the current clients in the lobby from the server.
	 */
	public void requestLobbyUpdate() {
		lobbyUpdateRequest.fire(new LobbyUpdateRequest());
		System.out.println(Client.getInstance().getNickname()+": LobbyUpdateRequest fired.");
	}
	
	/*
	 * Update the lobby list model and display with newest lobby update from the server.
	 */
	public void updateLobby(@Observes LobbyUpdate update) {
		
		// Out with the old.
		lobbyList.clear();
		lobbyPanel.clear();

		for (Player p : update.getPlayers().values()) {
			// Add to model.
			lobbyList.add(p);
			
			// Add to view.
			Button playerButton = new Button(p.getNick());
			lobbyPanel.add(playerButton);
			
			// Don't display this user in the list.
			if (p.equals(Client.getInstance().getPlayer()))
				playerButton.setVisible(false);
			else
				playerButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Player inviter = Client.getInstance().getPlayer();
						Player invitee = lobbyList.get(lobbyPanel.getWidgetIndex((Widget) event.getSource()));
						gameInvitation.fire(new Invitation(inviter, invitee));
					}
				});
		}
	}
	
	/*
	 * Register this user with the lobby.
	 */
	public void joinLobby() {
		// If the user is joinging for the first time, make a new player object.
		Player player = Client.getInstance().getPlayer() != null 
				? Client.getInstance().getPlayer(): new Player(Client.getInstance().getNickname());
		RegisterRequest request = new RegisterRequest(player);
		registerRequest.fire(request);
		// For debugging.
		System.out.println(Client.getInstance().getNickname()+": LobbyRequest fired.");
	}
	
	/*
	 * Accept a player object from the server as the canonical representation of this user.
	 */
	public void loadPlayer(@Observes Player player) {
		// For debugging.
		System.out.println(Client.getInstance().getNickname()+": Player object received.");
		
		// Hide join lobby button.
		lobbyButton.setVisible(false);
		
		// If this user has not yet been registered, subscribe to server relay
		if (!Client.getInstance().hasRegisteredPlayer()) {
			// For debugging.
			System.out.println(Client.getInstance().getNickname()+": Subscribing to subject Client"+player.getId());

			messageBus.subscribe("Client"+player.getId(), new LobbyMessageCallback());
		}
		
		Client.getInstance().setPlayer(player);

		requestLobbyUpdate();
	}
}
