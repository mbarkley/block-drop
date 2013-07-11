package demo.client.local.game.tools;

import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BoardModel;

public class DummyBus implements BoardMessageBus {

  @Override
  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target) {}

  @Override
  public void sendMoveUpdate(BoardModel state, Player player) {}

}
