package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Move {
	
	private final int GAME;
	
	private final int PLAYER;
	
	private final int ROW;
	
	private final int COL;
	
	private boolean validated = false;

	public Move(int game, int player, int row, int col) {

		GAME = game;
		PLAYER = player;
		ROW = row;
		COL = col;

	}

	public int getCOL() {
		return COL;
	}

	public int getROW() {
		return ROW;
	}

	public int getPLAYER() {
		return PLAYER;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}
