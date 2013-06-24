package demo.client.local.lobby;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
@Page(role = DefaultPage.class)
@Templated
public class Lobby extends Composite {

  private static Lobby instance;

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

  private Set<Player> selected = new HashSet<Player>();

  /*
   * Create an instance of a lobby page.
   */
  public Lobby() {
    instance = this;
  }

  /*
   * Construct the UI elements for the lobby.
   */
  @PostConstruct
  public void postConstruct() {
    Client.getInstance().maybeInit();

    Button newGameButton = new Button("New Game");
    newGameButton.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        // TODO: Handle starting game properly.

        MessageBuilder.createMessage("Client" + Client.getInstance().getPlayer().getId()).command("start-game")
                .noErrorHandling().sendNowWith(messageBus);
      }
    });

    playerButtonPanel.add(newGameButton);
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

  public static Lobby getInstance() {
    return instance;
  }

  public void toggleSelected(Player model) {
    if (selected.contains(model)) {
      System.out.println("Player " + model.getNick() + " deselected.");
      selected.remove(model);
    }
    else {
      System.out.println("Player " + model.getNick() + " selected.");
      selected.add(model);
    }
  }

  void goToBoard() {
    boardTransition.go();
  }
}
