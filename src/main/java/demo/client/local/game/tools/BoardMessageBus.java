package demo.client.local.game.tools;

import org.jboss.errai.bus.client.api.messaging.MessageBus;

import demo.client.local.game.controllers.BoardController;
import demo.client.shared.Command;
import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BoardModel;

/**
 * A {@link MessageBus MessageBus} wrapper for sending game updates to the server.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public interface BoardMessageBus {

  /**
   * Send a {@link Command#UPDATE_SCORE score update} to the server.
   * 
   * @param scoreTracker
   *          The local player's updated score and player information.
   * @param target
   *          A remote player in this game. This score update will result in the targeted player
   *          having {@link BoardController#addRows(int) addRows} called.
   */
  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target);

  /**
   * Send a {@link Command#MOVE_UPDATE move update} to the server.
   * 
   * @param state
   *          The current state of the local player's game.
   * @param player
   *          The local player.
   */
  public void sendMoveUpdate(BoardModel state, Player player);

  /**
   * Send a {@link Command#GAME_KEEP_ALIVE} game keep alive to the server to prevent being kicked
   * from the game.
   * 
   * @param player
   *          The local player.
   */
  public void sendPauseUpdate(Player player);

}