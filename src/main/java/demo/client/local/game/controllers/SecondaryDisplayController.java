package demo.client.local.game.controllers;

import demo.client.local.game.gui.Block;
import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;

public interface SecondaryDisplayController {

  public void selectNextPlayer();

  public void selectLastPlayer();

  public void updateScore(int numFullRows);

  public void updateAndSortScore(ScoreTracker scoreTracker);

  public ScoreTracker getScoreTracker();

  public void drawBlockToNextCanvas(Block nextBlock);

  public Player getTarget();

  public void removeTracker(Player player);

  public void selectPlayerByIndex(int i);

}