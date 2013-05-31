package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

/*
 * A portable JavaBean for storing and transmitting individual moves in a Tic-Tac-Toe game.
 */
@Portable
public class Move {
	
	/* The id of the game this move is from. */
	private int gameId;
	/* The id of the player who made this move. */
	private int playerId;
	/* The 0-indexed row index of the move. */
	private int row;
	/* The 0-indexed column index of this move. */
	private int col;
	/* A flag indicating whether or not this move has been validated by the server. */
	private boolean validated = false;
	
	/*
	 * A default no-arg constructor for automated bean creation. Users should prefer
	 * to use the constructor taking gameId, playerId, row, and col values.
	 */
	public Move() {}

	/*
	 * Create a move instance from a tic-tac-toe game.
	 * 
	 * @param gameId The id of the game to which this move belongs.
	 * @param playerId The id of the player who made this move.
	 * @param row The 0-indexed row index of this move on the board.
	 * @param col The 0-indexed col index of this move on the board.
	 */
	public Move(int gameId, int playerId, int row, int col) {

		this.setGameId(gameId);
		this.setPlayerId(playerId);
		this.setRow(row);
		this.setCol(col);
	}

	/*
	 * Get the id of the game to which this move belongs.
	 * 
	 * @return The id of the game in which this move was made.
	 */
	public int getGameId() {
		return gameId;
	}

	/*
	 * Set the id of the game in which this move was made.
	 * 
	 * @param gameId The id of the game to which this move belongs.
	 */
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/*
	 * Get the id of the player who made this move.
	 * 
	 * @return The id of the player who made this move.
	 */
	public int getPlayerId() {
		return playerId;
	}

	/*
	 * Set the id of the player who made this move.
	 * 
	 * @param playerId The id of the player who made this move.
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/*
	 * Get the row index of this move.
	 * 
	 * @return The 0-indexed row index of this move.
	 */
	public int getRow() {
		return row;
	}

	/*
	 * Set the row index of this move.
	 * 
	 * @param row The 0-indexed row index of this move.
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/*
	 * Get the col index of this move.
	 * 
	 * @return The 0-indexed col index of this move.
	 */
	public int getCol() {
		return col;
	}

	/*
	 * Set the col index of this move.
	 * 
	 * @param col The 0-indexed col index of this move.
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/*
	 * Check if this move has been validated by the server.
	 * 
	 * @return True iff this move has been validated by the server.
	 */
	public boolean isValidated() {
		return validated;
	}

	/*
	 * Set whether or not this move has been validated by the server.
	 * 
	 * @param validated True iff this move has been validated by the server.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

}
