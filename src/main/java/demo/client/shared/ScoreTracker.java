package demo.client.shared;

import org.jboss.errai.databinding.client.api.Bindable;

/*
 * For storing and updating the score of an individual player.
 */
@Bindable
public class ScoreTracker {

	private long score;
	private String name;
	private int id;
	
	public static final int BASE_ROW_SCORE = 10;
	private static final int[] COMBO_FACTOR = new int[] {1, 2, 5, 10};
	
	public static int calculateComboFactor(int numClearedRows) {
		return COMBO_FACTOR[numClearedRows-1];
	}
	
	public ScoreTracker() {
		score = 0;
		name = "default";
		id = 0;
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
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
