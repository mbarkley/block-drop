package demo.client.local.game.controllers;

import demo.client.local.game.gui.Block;
import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;

public interface SecondaryDisplayController {

  public abstract void selectNextPlayer();

  public abstract void selectLastPlayer();

  public abstract void updateScore(int numFullRows);

  public abstract void updateAndSortScore(ScoreTracker scoreTracker);

  public abstract ScoreTracker getScoreTracker();

  public abstract void drawBlockToNextCanvas(Block nextBlock);

  public abstract Player getTarget();

  public abstract void removeTracker(Player player);

}