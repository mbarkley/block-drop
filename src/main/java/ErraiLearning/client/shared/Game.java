package ErraiLearning.client.shared;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.jboss.errai.common.client.api.annotations.Portable;

/*
 * A container for a tic-tac-toe board and related logic. Every game
 * should be assigned an id to identify different instances of the same
 * game between server and clients.
 */
@Dependent
@Portable
public class Game {
	
	/* First player (or player X) in game. */
	private Player player1 = null;
	/* Second player (or Player O) in game. */
	private Player player2 = null;
	
	/* Id for identifying different instances of the same game (between server and clients). */
	private int gameId = 0;
	
	/* An ordered list of moves that have been made in the game. */
	private List<Move> moveList = new ArrayList<Move>();
	
	/* Representation of tic-tac-toe board in row-major order.
	 * board[i][j] == 0 means no move has been made in the spot (i,j).
	 * board[i][j] == playerId means that the player with the id playerId has moved in spot (i,j).
	 * Indices are in row-major order.
	 */
	private int[][] board = new int[3][3];
	
	/* The id of the player who should move this turn. */
	private int currentTurn = 0;
	
	/*
	 * Default constructor for JavaBean compliance. Users should invoke the constructor with gameId and
	 * player arguments.
	 */
	public Game() {}
	
	/*
	 * Construct a game instance.
	 * 
	 * @param gameId The id used to identify other instances of the same game between server and clients.
	 * @param player1 The first player of the game (or player X).
	 * @param player2 The second player of the game (or player Y).
	 */
	public Game(int gameId, Player player1, Player player2) {
		this.gameId = gameId;
		this.player1 = player1;
		this.player2 = player2;
		this.currentTurn = player1.getId();
	}
	
	/*
	 * Check if lastMove is part of a winning combination of moves. Note that this may be successfully invoked
	 * before the last move has been added to the game (to predict if the next move would win.
	 * 
	 * @param lastMove The move object that is being checked.
	 * 
	 * @return True iff lastMove is part of a winning tic-tac-toe line (three-in-a-row, -column, or -diagonal).
	 */
	public boolean winningMove(Move lastMove) {
		return winningRow(lastMove) || winningColumn(lastMove) || winningDiagonal(lastMove);
	}
	
	/*
	 * Return true iff lastMove creates a winning row for the respective player.
	 */
	private boolean winningRow(Move lastMove) {
		boolean res = true;
		
		for (int j = 1; j < 3; j++) {
			// j is the offset to the right from the given move.
			// Taking mod 3 of lastMove.getCOL()+j causes wraps around the edge of the tic-tac-toe board.
			res = res && board[lastMove.getRow()][(lastMove.getCol()+j)%3] == lastMove.getPlayerId();
		}
		
		return res;
	}

	/*
	 * Return true iff the lastMove creates a winning diagonal for the respective player.
	 */
	private boolean winningDiagonal(Move lastMove) {
		// If the last move was not a corner or centre tile, then a winning diagonal is not possible.
		// The sum the row and col indices of a move is even iff it is a corner or centre tile.
		if ((lastMove.getRow() + lastMove.getCol()) % 2 != 0)
			return false;

		boolean res = true;
		
		for (int i = 1; i < 3; i++)
			res = res && board[(lastMove.getRow()+i)%3][(lastMove.getCol()+i)%3] == lastMove.getPlayerId();
		
		return res;
	}

	/*
	 * Return true iff the lastMove creates a winning column for the respective player.
	 */
	private boolean winningColumn(Move lastMove) {

		boolean res = true;
		
		for (int i = 1; i < 3; i++)
			res = res && board[(lastMove.getRow()+i)%3][lastMove.getCol()] == lastMove.getPlayerId();
		
		return res;
	}

	/*
	 * Make a tic-tac-toe move.
	 * 
	 * @param player The id of the player making the move.
	 * @param row The row of the move position (0-indexed).
	 * @param col The col of the move position (0-indexed).
	 */
	public void makeMove(int playerId, int row, int col) throws InvalidMoveException {
		if (!validMove(playerId, row, col))
			throw new InvalidMoveException();
		
		board[row][col] = playerId;
		
		Move newMove = new Move(gameId, playerId, row, col);
		moveList.add(newMove);
		
		currentTurn = getPlayerX().getId() == playerId ? getPlayerO().getId() : getPlayerX().getId();
	}
	
	/*
	 * Check that row and column are within board array bounds.
	 * @param row The row of the move to be checked (0-indexed).
	 * @param col The column of the move to be checked (0-indexed).
	 * 
	 * @return True iff row and col will not be outside array bounds.
	 */
	private boolean checkBounds(int row, int col) {
		return 0 <= row && row < 3 && 0 <= col && col < 3;
	}
	
	/*
	 * Validate a tic-tac-toe move.
	 * 
	 * @param player The id of the player who should move next.
	 * @param row The row of the move to be validated (0-indexed).
	 * @param col The col of the move to be validated (0-indexed).
	 * 
	 * @return True iff a move can be made to the spot at (row,col).
	 */
	public boolean validMove(int playerId, int row, int col) {
		return  playerId == currentTurn && (getLastMove() == null || getLastMove().isValidated()) 
				&& checkBounds(row, col) && board[row][col] == 0;
	}
	
	/*
	 * Checks if the given move (which has presumably been validated externally) is also valid 
	 * within the internal state of this game instance. The provided move should be the same as
	 * the last recorded move in this game instance, except for possibly the validation flag.
	 * 
	 * This method is designed to help check that the server and clients have the same game state.
	 * If this method returns true then the validated flag on the recorded last move will be set to true.
	 * 
	 * @param move The given move to be compared to this game instances last move.
	 * 
	 * @return True iff the given move is identical to the last move recorded in this game instance in all
	 * attributes except possibly the validation flag.
	 */
	public boolean validateLastMove(Move move) {
		Move localMove = getLastMove();
		
		if (localMove.getGameId() == move.getGameId() && localMove.getRow() == move.getRow()
				&& localMove.getCol() == move.getCol()) {
			localMove.setValidated(true);
			return true;
		} else {
			//TODO: Figure out what happened and do something about it.
			return false;
		}
	}
	
	/*
	 * Check if it is player's turn.
	 * 
	 * @param player The player whose turn status is in question.
	 * 
	 * @return True iff it is player's turn to move.
	 */
	public boolean isPlayersTurn(Player player) {
		return player != null && player.getId() == currentTurn;
	}

	/*
	 * Get the id of this game.
	 * 
	 * @return Return the id of this game instance.
	 */
	public int getGameId() {
		return gameId;
	}

	/*
	 * Get the id of player X (who is player1).
	 * 
	 * @return Return the id of the player X (also the first player).
	 */
	public Player getPlayerX() {
		return player1;
	}

	/*
	 * Get the id of player O (who is player2).
	 * 
	 * @return Return the id of the player O (also the second player).
	 */
	public Player getPlayerO() {
		return player2;
	}

	/*
	 * Get the last move made in this game if any moves have been made.
	 * 
	 * @return Return the last move made in this game, or null if no moves have been made.
	 */
	public Move getLastMove() {
		return moveList.isEmpty() ? null : moveList.get(moveList.size()-1);
	}

	/*
	 * Check if the game is over from either a draw or a winning move.
	 * 
	 * @return Return true iff the game is over.
	 */
	public boolean isOver() {
		return moveList.size() == 9 || winningMove(getLastMove());
	}
	
	/*
	 * Check if the game has ended in a draw.
	 * 
	 * @return Return true iff the game is over and there is no winner.
	 */
	public boolean isDraw() {
		return moveList.size() == 9 && !winningMove(getLastMove());
	}
	
	/*
	 * Get the id of the player who has won this game.
	 * 
	 * @return Return the id of the player who has won this game, or 0 if there is no winner.
	 */
	public int getWinnerId() {
		return winningMove(getLastMove()) ? getLastMove().getPlayerId() : 0;
	}
}
