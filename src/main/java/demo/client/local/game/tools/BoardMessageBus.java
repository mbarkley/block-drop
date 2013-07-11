package demo.client.local.game.tools;

import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BoardModel;

public interface BoardMessageBus {

  public abstract void sendScoreUpdate(ScoreTracker scoreTracker, Player target);

  public abstract void sendMoveUpdate(BoardModel state, Player player);

}