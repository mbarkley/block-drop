package ErraiLearning.client.local;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
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

import ErraiLearning.client.shared.Game;
import ErraiLearning.client.shared.Invitation;
import ErraiLearning.client.shared.LobbyUpdate;
import ErraiLearning.client.shared.LobbyUpdateRequest;
import ErraiLearning.client.shared.Player;
import ErraiLearning.client.shared.RegisterRequest;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

@Page(role=DefaultPage.class)
public class Lobby extends Composite {
	
	private final class LobbyMessageCallback implements MessageCallback {
		@Override
		public void callback(Message message) {
			if (message.getCommandType().equals("invitation"))
				invitationCallback(message);
			else if (message.getCommandType().equals("start-game"))
				startGameCallback(message);
		}
		
		private void startGameCallback(Message message) {
			TTTClient.getInstance().setGame(message.get(Game.class, MessageParts.Value));
			System.out.println(TTTClient.getInstance().getNickname()+": Before board transition.");
			boardTransition.go();
			System.out.println(TTTClient.getInstance().getNickname()+": After board transition.");
		}

		private void invitationCallback(Message message) {
			Invitation invitation = message.get(Invitation.class, MessageParts.Value);
			
			invitation.setAccepted(
				Window.confirm("You have been invited to play a game by "
						+ invitation.getInviter().getNick()
						+ ". Would you like to accept?"
				)
			);
			
			MessageBuilder.createMessage()
			.toSubject("Relay")
			.command("invitation-response")
			.withValue(invitation)
			.noErrorHandling()
			.sendNowWith(dispatcher);
		}
	}
	
	@Inject private TransitionTo<Board> boardTransition;
	@Inject	private Event<LobbyUpdateRequest> lobbyUpdateRequest;
	@Inject	private Event<RegisterRequest> registerRequest;
	@Inject private Event<Invitation> gameInvitation;
	private MessageBus messageBus = ErraiBus.get();
	private RequestDispatcher dispatcher = ErraiBus.getDispatcher();

	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel lobbyPanel = new VerticalPanel();
	private Button lobbyButton = new Button("Join Lobby");
	
	/* Shadows lobbyPanel. */
	private List<Player> lobbyList = new ArrayList<Player>();
	
	public Lobby() {
		initWidget(vPanel);
		
		buttonPanel.add(lobbyButton);
		vPanel.add(buttonPanel);
		vPanel.add(lobbyPanel);
		
		lobbyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				joinLobby();
			}
		});
	}
	
	public void requestLobbyUpdate() {
		lobbyUpdateRequest.fire(new LobbyUpdateRequest());
		System.out.println(TTTClient.getInstance().getNickname()+": LobbyUpdateRequest fired.");
	}
	
	public void updateLobby(@Observes LobbyUpdate update) {
		
		lobbyList.clear();
		lobbyPanel.clear();
	
		for (Player p : update.getPlayers().values()) {
			lobbyList.add(p);
			
			Button playerButton = new Button(p.getNick());
			lobbyPanel.add(playerButton);
			
			if (p.equals(TTTClient.getInstance().getPlayer()))
				playerButton.setVisible(false);
			else
				playerButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						Player inviter = TTTClient.getInstance().getPlayer();
						Player invitee = lobbyList.get(lobbyPanel.getWidgetIndex((Widget) event.getSource()));
						gameInvitation.fire(new Invitation(inviter, invitee));
					}
				});
		}
	}
	
	public void joinLobby() {
		Player player = TTTClient.getInstance().getPlayer() != null 
				? TTTClient.getInstance().getPlayer(): new Player(TTTClient.getInstance().getNickname());
		RegisterRequest request = new RegisterRequest(player);
		registerRequest.fire(request);
		System.out.println(TTTClient.getInstance().getNickname()+": LobbyRequest fired.");
	}
	
	public void loadPlayer(@Observes Player player) {
		System.out.println(TTTClient.getInstance().getNickname()+": Player object received.");
		
		// Hide join lobby button.
		lobbyButton.setVisible(false);
		
		if (!TTTClient.getInstance().hasRegisteredPlayer()) {
			System.out.println(TTTClient.getInstance().getNickname()+": Subscribing to subject Client"+player.getId());
			// Subscribe to server relay
			messageBus.subscribe("Client"+player.getId(), new LobbyMessageCallback());
		}
		
		TTTClient.getInstance().setPlayer(player);

		requestLobbyUpdate();
	}
}
