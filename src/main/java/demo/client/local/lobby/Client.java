package demo.client.local.lobby;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.Navigation;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import demo.client.shared.GameRoom;
import demo.client.shared.Player;


/*
 * A container class for a game client.
 */
@EntryPoint
@ApplicationScoped
public class Client {

  /* The user's chosen nickname. */
  private String nickname = null;
  /* The player object associated with this user. */
  private Player player = null;
  /* The game this user is currently in (or null if not in a game). */
  private GameRoom game = null;

  /* For the Errai NavigationUI. */
  @Inject
  private Navigation nav;

  /* For debugging only. */
  private static int curDebugId = 1;
  /* For debugging only. */
  private int debugId;

  /* For debugging only. */
  private static synchronized int nextDebugId() {
    return curDebugId++;
  }

  /* For accessing the singleton instance of this object. */
  private static Client instance = null;

  /*
   * Get the singleton instance of this client.
   */
  public static Client getInstance() {
    return instance;
  }

  /*
   * Create a Client object. Client should be a singleton, but this is not enforced. This
   * constructor should only be called once.
   */
  public Client() {
    // For debugging.
    debugId = nextDebugId();
    System.out.println("Client" + debugId + ": TTTClient constructor called.");

    instance = this;
  }
  
  public void maybeInit() {
    if (nickname == null) {
      init();
    }
  }

  /*
   * Prompt the user for a nickname and attach the navigation panel.
   */
  private void init() {
    String initialValue = "Foobar";
    String msg = "Please select a username.";
    nickname = Window.prompt(msg, initialValue);

    if (nickname == null)
      nickname = "default";

    // For debugging.
    System.out.println(nickname + ": User selected " + nickname + " as their nickname.");

    // Add nav to root panel.
    RootPanel.get().add(nav.getContentPanel());
  }

  /*
   * Check if this client has registered a player object with the server.
   * 
   * @return True iff this client has registered a player with the server.
   */
  public boolean hasRegisteredPlayer() {
    return player != null;
  }

  /*
   * Get the player object associated with this user.
   * 
   * @return The player object associated with this user.
   */
  public Player getPlayer() {
    return player;
  }

  /*
   * Set the player object associated with this user.
   * 
   * @param player The new player object to be associated with this user.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /*
   * Get the nickname of this player.
   * 
   * @return The nickname of this player.
   */
  public String getNickname() {
    return nickname;
  }

  /*
   * Get the game object representing the game this client is currently in.
   * 
   * @return The game object representing the game this client is currently in.
   */
  public GameRoom getGame() {
    return game;
  }

  /*
   * Set the game object representing the game this client is currently in.
   * 
   * @param game The game object representing the game this client is currently in.
   */
  public void setGame(GameRoom game) {
    this.game = game;
  }
}
