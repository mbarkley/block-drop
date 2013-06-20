package demo.client.local.lobby;

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
import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import demo.client.local.game.BoardPage;
import demo.client.shared.GameRoom;
import demo.client.shared.Invitation;
import demo.client.shared.LobbyUpdate;
import demo.client.shared.LobbyUpdateRequest;
import demo.client.shared.Player;
import demo.client.shared.RegisterRequest;

/*
 * A class displaying a page for a game lobby.
 */
@Page(role=DefaultPage.class)
@Templated
public class Lobby extends Composite {

  /*
   * A class for handling lobby updates and invitation from the server.
   */
  private final class LobbyMessageCallback implements MessageCallback {
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client
     * .api.messaging.Message)
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
      System.out.println(Client.getInstance().getNickname() + ": Before board transition.");
      boardTransition.go();
      // For debugging.
      System.out.println(Client.getInstance().getNickname() + ": After board transition.");
    }

    /*
     * Prompt the user to respond to an invitation from another client.
     */
    private void invitationCallback(Message message) {
      Invitation invitation = message.get(Invitation.class, MessageParts.Value);

      invitation.setAccepted(Window.confirm("You have been invited to play a game by "
              + invitation.getInviter().getNick() + ". Would you like to accept?"));

      // The server handles the invitation, whether or not it was accepted.
      MessageBuilder.createMessage().toSubject("Relay").command("invitation-response").withValue(invitation)
              .noErrorHandling().sendNowWith(dispatcher);
    }
  }

  /* For the Errai NavigationUI. */
  @Inject
  private TransitionTo<BoardPage> boardTransition;
  /* For requesting lobby updates from the server. */
  @Inject
  private Event<LobbyUpdateRequest> lobbyUpdateRequest;
  /* For registering this client with the server. */
  @Inject
  private Event<RegisterRequest> registerRequest;
  /* For inviting another user to play a game. */
  @Inject
  private Event<Invitation> gameInvitation;
  /* For receiving messages from the server. */
  private MessageBus messageBus = ErraiBus.get();
  /* For sending messages to the server. */
  private RequestDispatcher dispatcher = ErraiBus.getDispatcher();

  @Inject
  @DataField("player-button-panel")
  private HorizontalPanel playerButtonPanel;
  @Inject
  @DataField("game-button-panel")
  private HorizontalPanel gameButtonPanel;
  @Inject
  @DataField("player-list")
  private ListWidget<Player, PlayerPanel> playerList;
  @Inject
  @DataField("game-list")
  private ListWidget<GameRoom, GamePanel> gameList;

  /*
   * Create an instance of a lobby page.
   */
  public Lobby() {
  }

  /*
   * Construct the UI elements for the lobby.
   */
  @PostConstruct
  public void postConstruct() {
    Client.getInstance().init();
    joinLobby();
  }

  /*
   * Request an update of the current clients in the lobby from the server.
   */
  public void requestLobbyUpdate() {
    lobbyUpdateRequest.fire(new LobbyUpdateRequest());
    System.out.println(Client.getInstance().getNickname() + ": LobbyUpdateRequest fired.");
  }

  /*
   * Update the lobby list model and display with newest lobby update from the server.
   */
  public void updateLobby(@Observes LobbyUpdate update) {
    playerList.setItems(update.getPlayers());
    gameList.setItems(update.getGames());
  }

  /*
   * Register this user with the lobby.
   */
  public void joinLobby() {
    // If the user is joining for the first time, make a new player object.
    Player player = Client.getInstance().getPlayer() != null ? Client.getInstance().getPlayer() : new Player(Client
            .getInstance().getNickname());
    RegisterRequest request = new RegisterRequest(player);
    registerRequest.fire(request);
    // For debugging.
    System.out.println(Client.getInstance().getNickname() + ": LobbyRequest fired.");
  }

  /*
   * Accept a player object from the server as the canonical representation of this user.
   */
  public void loadPlayer(@Observes Player player) {
    // For debugging.
    System.out.println(Client.getInstance().getNickname() + ": Player object received.");

    // If this user has not yet been registered, subscribe to server relay
    if (!Client.getInstance().hasRegisteredPlayer()) {
      // For debugging.
      System.out.println(Client.getInstance().getNickname() + ": Subscribing to subject Client" + player.getId());

      messageBus.subscribe("Client" + player.getId(), new LobbyMessageCallback());
    }

    Client.getInstance().setPlayer(player);

    requestLobbyUpdate();
  }
}
