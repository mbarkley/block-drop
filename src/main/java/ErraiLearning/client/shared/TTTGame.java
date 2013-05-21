package ErraiLearning.client.shared;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Dependent;

/*
 * A container for a tic-tac-toe board and related logic.
 */

@Dependent
public class TTTGame {

	public final int PLAYER1;
	public final int PLAYER2;
	
	public final int GAME_ID;
	
	private List<Move> moveList = new ArrayList<Move>();
	
	/* Representation of tic-tac-toe board in row-major order. */
	private int[][] board = new int[3][3];
	
	/* The id of the player who should be next to move. */
	private int currentTurn;
	
	public TTTGame(int gameId, int player1Id, int player2Id) {
		
		GAME_ID = gameId;
		PLAYER1 = player1Id;
		PLAYER2 = player2Id;
	}
	
	/*
	 * Check if lastMove is part of a winning combination of moves.
	 * 
	 * @param lastMove The move object that is being checked.
	 * 
	 * @return True iff lastMove is part of a winning tic-tac-toe line (three-in-a-row, -column, or -diagonal).
	 */
	public boolean winningMove(Move lastMove) {
		
		return winningRow(lastMove) || winningColumn(lastMove) || winningDiagonal(lastMove);
		
	}
	
	private boolean winningRow(Move lastMove) {

		boolean res = true;
		
		for (int j = 1; j < 3; j++)
			// j is the offset to the right from the given move.
			// Taking mod 3 of lastMove.getCOL()+j causes wraps around the edge of the tic-tac-toe board.
			res = res && board[lastMove.getROW()][(lastMove.getCOL()+j)%3] == lastMove.getPLAYER();
		
		return res;
	}

	private boolean winningDiagonal(Move lastMove) {

		boolean res = true;
		
		for (int i = 1; i < 3; i++)
			res = res && board[(lastMove.getROW()+i)%3][(lastMove.getCOL()+i)%3] == lastMove.getPLAYER();
		
		return res;
	}

	private boolean winningColumn(Move lastMove) {

		boolean res = true;
		
		for (int i = 1; i < 3; i++)
			res = res && board[(lastMove.getROW()+i)%3][lastMove.getCOL()] == lastMove.getPLAYER();
		
		return res;
	}

	/*
	 * Make a tic-tac-toe move.
	 * 
	 * @param player The id of the player making the move.
	 * @param row The row of the move position (0-indexed).
	 * @param col The col of the move position (0-indexed).
	 */
	public Move makeMove(int player, int row, int col) throws InvalidMoveException {

		if (!validateMove(player, row, col))
			throw new InvalidMoveException();
		
		board[row][col] = player;
		
		Move newMove = new Move(GAME_ID, player, row, col);
		moveList.add(newMove);
		
		return newMove;
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
	public boolean validateMove(int player, int row, int col) {
		return checkBounds(row, col) && board[row][col] == 0 && player == currentTurn;
	}
	
}
