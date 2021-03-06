package demo.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.Navigation;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import demo.client.shared.meta.GameRoom;
import demo.client.shared.meta.Player;

/**
 * A top-level class containing information on the local player.
 */
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

  /**
   * Prompt the user for a nickname and attach the navigation panel.
   */
  @PostConstruct
  private void init() {
    String initialValue = "Foobar";
    String msg = "Please select a username.";
    nickname = Window.prompt(msg, initialValue);

    if (nickname == null)
      nickname = "default";

    // Add nav to root panel.
    RootPanel.get().add(nav.getContentPanel());
  }

  /**
   * Check if this client has registered a player object with the server.
   * 
   * @return True iff this client has registered a player with the server.
   */
  public boolean hasRegisteredPlayer() {
    return player != null;
  }

  /**
   * Get the player object associated with this client.
   * 
   * @return The player object associated with this client.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set the player object associated with this client.
   * 
   * @param player
   *          The new player object to be associated with this client.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Get the nickname of this player.
   * 
   * @return The nickname of this player.
   */
  public String getNickname() {
    return nickname;
  }

  /**
   * Get the game object representing the game this client is currently in.
   * 
   * @return The game this client is currently in.
   */
  public GameRoom getGameRoom() {
    return game;
  }

  /**
   * Set the game object representing the game this client is currently in.
   * 
   * @param game
   *          The game this client is currently in.
   */
  public void setGameRoom(GameRoom game) {
    this.game = game;
  }
}
