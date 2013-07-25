package demo.client.local.game.controllers;

import demo.client.local.game.gui.Block;
import demo.client.shared.meta.Player;
import demo.client.shared.meta.ScoreTracker;

/**
 * A controller for peripheral displays, which include:
 * <ul>
 * <li>A display for the upcoming block</li>
 * <li>The list of current players and their scores</li>
 * </ul>
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public interface SecondaryDisplayController {

  /**
   * Deselect the currently selected player in the player list and select the next player.
   */
  public void selectNextPlayer();

  /**
   * Deselect the currently selected player in the player list and select the previous player.
   */
  public void selectLastPlayer();

  /**
   * Update the local players score and sort the score list.
   * 
   * @param numFullRows
   *          The number of rows cleared, which will determine the number of points added to the
   *          score.
   */
  public void updateScore(int numFullRows);

  /**
   * Update any players score and sort the score list. This method may be used to add a new
   * ScoreTracker to the list as well as updating a previous tracker.
   * 
   * @param scoreTracker
   *          The score tracker to be updated.
   */
  public void updateAndSortScore(ScoreTracker scoreTracker);

  /**
   * Get the ScoreTracker for the local player.
   * 
   * @return The ScoreTracker of the local player.
   */
  public ScoreTracker getScoreTracker();

  /**
   * Draw the upcoming block to the appropriate canvas.
   * 
   * @param nextBlock
   *          The block to be drawn.
   */
  public void drawBlockToNextCanvas(Block nextBlock);

  /**
   * Get the player associated with the currently selected score list entry.
   * 
   * @return The currently selected player in the score list.
   */
  public Player getTarget();

  /**
   * Remove the ScoreTracker associated with the given player.
   * 
   * @param player
   *          The Player associated with the ScoreTracker to be removed.
   */
  public void removeTracker(Player player);

  /**
   * Deselect the currently selected player and select the ith player in the score list.
   * 
   * @param i
   */
  public void selectPlayerByIndex(int i);

}