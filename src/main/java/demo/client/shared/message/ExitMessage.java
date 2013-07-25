package demo.client.shared.message;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.GameRoom;
import demo.client.shared.meta.Player;

/**
 * A bean used to notify the server that a player is actively exiting a game.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Portable
public class ExitMessage {

  private GameRoom game;
  private Player player;

  /**
   * Get the player exiting the game.
   * 
   * @return The player exiting the game.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set the player exiting the game.
   * 
   * @param player
   *          The player exiting the game.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Get the game room the player is leaving.
   * 
   * @return The game being left.
   */
  public GameRoom getGame() {
    return game;
  }

  /**
   * Set the game the player is leaving.
   * 
   * @param game
   *          The game being left.
   */
  public void setGame(GameRoom game) {
    this.game = game;
  }
}
