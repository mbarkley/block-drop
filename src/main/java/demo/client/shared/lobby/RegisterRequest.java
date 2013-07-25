package demo.client.shared.lobby;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.Player;

/**
 * A portable bean for registering a user with the server and/or putting them in the lobby.
 */
@Portable
public class RegisterRequest {

  /** The player to be registered. */
  private Player player = null;

  /**
   * A default no-arg constructor for automatic bean creation.
   */
  public RegisterRequest() {
  }

  /**
   * Create a RegisterRequest object to register a player.
   * 
   * @param player
   *          The player to be registered with the server and/or put in the lobby.
   */
  public RegisterRequest(Player player) {
    this.player = player;
  }

  /**
   * Get the nickname of the player being registered.
   * 
   * @return The nickname of the player to be registered.
   */
  public String getNickname() {
    return player.getName();
  }

  /**
   * Check if the player has been registered previously.
   * 
   * @return True iff the player has been registered already.
   */
  public boolean hasRegistered() {
    return player != null && player.hasRegistered();
  }

  /**
   * Get the player to be registered.
   * 
   * @return The player to be registered.
   */
  public Player getPlayer() {
    return player;
  }
}
