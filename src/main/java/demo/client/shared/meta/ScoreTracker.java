package demo.client.shared.meta;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

/**
 * A portable bean for storing and updating the score of an individual player.
 */
@Bindable
@Portable
public class ScoreTracker implements Comparable<ScoreTracker> {

  private long score;
  private Player player;
  private int gameId;
  /** Number of rows cleared on last update. */
  private int rowsClearedLast;
  private boolean selected = false;

  public static final int BASE_ROW_SCORE = 10;
  private static final int[] COMBO_FACTOR = new int[] { 1, 2, 5, 10 };
  public static final long LOSS_PENALTY = 500;

  /**
   * Calculate the combo bonus factor from the number of rows scored.
   * 
   * @param numClearedRows
   *          The number of rows scored.
   * @return The factor to multiply the base score by.
   */
  private static int calculateComboFactor(int numClearedRows) {
    return COMBO_FACTOR[numClearedRows - 1];
  }

  /**
   * Create a blank score tracker.
   */
  public ScoreTracker() {
    score = 0;
  }

  /**
   * Get the score of the player associated with this tracker.
   * 
   * @return The score of the player associated with this tracker.
   */
  public long getScore() {
    return score;
  }

  /**
   * Set the score of the player associated with this tracker.
   * 
   * @param score
   *          The score of the player associated with this tracker.
   */
  public void setScore(long score) {
    this.score = score;
  }

  /**
   * Update the players score.
   * 
   * @param numClearedRows
   *          The number of rows the player cleared.
   */
  public void updateScore(int numClearedRows) {
    score += calculateScore(numClearedRows);
    rowsClearedLast = numClearedRows;
  }

  private long calculateScore(int numClearedRows) {
    return numClearedRows * BASE_ROW_SCORE * calculateComboFactor(numClearedRows);
  }

  /**
   * Get the id of the player associated with this score tracker.
   * 
   * @return The score of the player, or 0 if there is no associated player.
   */
  public int getId() {
    return player != null ? player.getId() : 0;
  }

  /**
   * Get the name of the player associated with this tracker.
   * 
   * @return The name of the player, or {@code ""} if there is no associated player.
   */
  public String getName() {
    return player != null ? player.getName() : "";
  }

  /**
   * Get the player associated with this tracker.
   * 
   * @return A player or {@code null} if there is no player assigned.
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set the player associated with this tracker.
   * 
   * @param player
   *          A player to associate with this tracker.
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  @Override
  public int compareTo(ScoreTracker arg0) {
    return (int) (getScore() - arg0.getScore());
  }

  @Override
  public boolean equals(Object other) {
    return other != null && other instanceof ScoreTracker && getId() == ((ScoreTracker) other).getId();
  }

  /**
   * Get the id of the game to which this score tracker belongs.
   * 
   * @return The id of the game to which this score tracker belongs.
   */
  public int getGameId() {
    return gameId;
  }

  /**
   * Set the id of the game to which this score tracker belongs.
   * 
   * @param gameId
   *          A valid game id.
   */
  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  /**
   * Get the number of rows last cleared by the associated player.
   * 
   * @return The number of rows last cleared by the associated player.
   */
  public int getRowsClearedLast() {
    return rowsClearedLast;
  }

  /**
   * Set the number of rows last cleared by the associated player.
   * 
   * @param rowsClearedLast
   *          The number of rows last cleared by the associated player.
   */
  public void setRowsClearedLast(int rowsClearedLast) {
    this.rowsClearedLast = rowsClearedLast;
  }

  /**
   * Check if this tracker is selected. This is used by the client to check if the player associated
   * with this tracker will be the target of a
   * {@link demo.client.shared.message.Command#UPDATE_SCORE score update}.
   * 
   * @return True if this score tracker is selected.
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Select this score tracker. See {@link #isSelected() isSelected} for details.
   */
  public void select() {
    selected = true;
  }

  /**
   * Deselect this score tracker. See {@link #isSelected() isSelected} for details.
   */
  public void deselect() {
    selected = false;
  }
}
