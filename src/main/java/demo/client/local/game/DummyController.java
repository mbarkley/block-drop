package demo.client.local.game;

import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;

public class DummyController implements SecondaryDisplayController {

  @Override
  public void selectNextPlayer() {}

  @Override
  public void selectLastPlayer() {}

  @Override
  public void updateScore(int numFullRows) {}

  @Override
  public void updateAndSortScore(ScoreTracker scoreTracker) {}

  @Override
  public ScoreTracker getScoreTracker() {
    return null;
  }

  @Override
  public void drawBlockToNextCanvas(Block nextBlock) {}

  @Override
  public Player getTarget() {
    return null;
  }

  @Override
  public void removeTracker(Player player) {}
}