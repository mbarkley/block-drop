package demo.client.shared.message;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.game.model.BoardModel;
import demo.client.shared.meta.Player;

/**
 * A portable bean for sending {@link Command#MOVE_UPDATE move updates} between server and clients.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Portable
public class MoveEvent {

  private BoardModel state;
  private Player player;
  private int gameId;

  /**
   * A default no-arg constructor for proxying.
   */
  public MoveEvent() {
  }

  /**
   * Create a MoveEvent.
   * 
   * @param state
   *          The state of the game after the move triggering this event.
   * @param player
   *          The player initially sending this event.
   * @param gameId
   *          The id of the game in which this move occurred.
   */
  public MoveEvent(BoardModel state, Player player, int gameId) {
    this.setState(state);
    this.setPlayer(player);
    this.setGameId(gameId);
  }

  /**
   * Get the state of the game in this MoveEvent.
   * 
   * @return The most recent game state.
   */
  public BoardModel getState() {
    return state;
  }

  /**
   * Set the state of the game in this MoveEvent.
   * 
   * @param state
   *          The most recent game state.
   */
  public void setState(BoardModel state) {
    this.state = state;
  }

  /**
   * Get the player who initially sent this MoveEvent.
   * 
   * @return The player who initially sent this MoveEvent.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set the player who initially sent this move event.
   * 
   * @param player
   *          The player who initially sent this move event.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Get the id of the game in which this move occurred.
   * 
   * @return The id of the game in which this move occurred.
   */
  public int getGameId() {
    return gameId;
  }

  /**
   * Set the id of the game in which this move occurred.
   * 
   * @param gameId
   *          The id of the game in which this move occurred.
   */
  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

}
