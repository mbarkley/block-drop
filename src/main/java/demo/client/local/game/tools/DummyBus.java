package demo.client.local.game.tools;

import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BoardModel;

/**
 * A NOOP implementation of the {@link BoardMessageBus BoardMessageBus} interface.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class DummyBus implements BoardMessageBus {

  @Override
  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target) {
  }

  @Override
  public void sendMoveUpdate(BoardModel state, Player player) {
  }

  @Override
  public void sendPauseUpdate(Player player) {
  }

}
