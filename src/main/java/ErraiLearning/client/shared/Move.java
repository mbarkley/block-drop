package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

/*
 * Moves indices are 0-indexed.
 */
@Portable
public class Move {
	
	private int gameId;
	
	private int playerId;
	
	private int row;
	
	private int col;
	
	private boolean validated = false;
	
	public Move() {}

	public Move(int game, int player, int row, int col) {

		this.setGameId(game);
		this.setPlayerId(player);
		this.setRow(row);
		this.setCol(col);
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int game) {
		this.gameId = game;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int player) {
		this.playerId = player;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}
