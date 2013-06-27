package demo.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

/*
 * For storing and updating the score of an individual player.
 */
@Bindable
@Portable
public class ScoreTracker implements Comparable<ScoreTracker> {

  private long score;
  private Player player;
  private int gameId;

  public static final int BASE_ROW_SCORE = 10;
  private static final int[] COMBO_FACTOR = new int[] { 1, 2, 5, 10 };

  public static int calculateComboFactor(int numClearedRows) {
    return COMBO_FACTOR[numClearedRows - 1];
  }

  public ScoreTracker() {
    score = 0;
  }

  public long getScore() {
    return score;
  }

  public void setScore(long score) {
    this.score = score;
  }

  /*
   * Update the players score.
   * 
   * @param numClearedRows The number of rows the player cleared.
   */
  public void updateScore(int numClearedRows) {
    score += calculateScore(numClearedRows);
  }

  private long calculateScore(int numClearedRows) {
    return numClearedRows * BASE_ROW_SCORE * calculateComboFactor(numClearedRows);
  }

  public int getId() {
    return player != null ? player.getId() : 0;
  }

  public String getName() {
    return player != null ? player.getName() : "";
  }

  public Player getPlayer() {
    return player;
  }

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

  public int getGameId() {
    return gameId;
  }
  
  public void setGameId(int gameId) {
    this.gameId = gameId;
  }
}
