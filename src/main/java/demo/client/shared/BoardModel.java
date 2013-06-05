package demo.client.shared;

/*
 * A model of the Block Drop board.
 */
public class BoardModel {

	/*
	 * A single row in the Block Drop board.
	 */
	public class Row {

		/* The spots in this row. Indices go left to right. */
		private int[] squares;
		
		/* The width of this row. */
		public final int WIDTH;
		
		/*
		 * Create a row of width size.
		 * 
		 * @param size The width of this row.
		 */
		public Row(int size) {
			WIDTH = size;
			// Values are initialized to value 0 (which is unoccupied).
			squares = new int[WIDTH];
		}

		/*
		 * Get the value at index in this row.
		 * 
		 * @param index The index to get in this row.
		 * 
		 * @return The value at index in this row.
		 * 
		 * @throws Exception If the index is out of range.
		 */
		public int getSquare(int index) throws Exception {
			// Make sure index is valid.
			checkIndex(index);
			
			return squares[index];
		}
		
		/*
		 * Set the value at index in this row.
		 * 
		 * @param index The index of the square to set in this row.
		 * @param value The value to set.
		 * 
		 * @throws Exception If the index is out of range.
		 */
		public void setSquare(int index, int value) throws Exception {
			// Make sure index is valid.
			checkIndex(index);
			
			squares[index] = value;
		}
		
		/*
		 * Check if this row is entirely occupied.
		 * 
		 * @return True iff this row is entirely occupied.
		 */
		public boolean isFull() {
			for (int i = 0; i < WIDTH; i++)
				if (squares[i] == 0)
					return false;
			
			return true;
		}
		
		private void checkIndex(int index) throws Exception {
			if (index < 0 || index >= WIDTH)
				throw new Exception("ERROR: "+index+" is an invalid index.");
		}
	}

	/* The value representing a vacant spot on the board. */
	public static final int NO_TILE = 0;
	/* The value representing an occupied spot on the board. */
	public static final int TILE = 1;
	
	/* The number of rows in this board. */
	public final int ROW_NUM;
	/* The number of columns in this board. */
	public final int COL_NUM;
	
	/* The rows of the board. Lower indices represent lower rows. */
	private Row[] board;
	/* 
	 * The current active block. Tiles in the active block are not recorded on the board
	 * until the block is no longer active.
	 */
	private BlockModel activeBlock = null;
	/* The next block to become active. */
	private BlockModel nextBlock = null;
	
	/*
	 * Create a BoardModel.
	 * 
	 * @param width The number of squares in the width of the board.
	 * @param height The number of squares in the height of the board.
	 */
	public BoardModel(int width, int height) {
		ROW_NUM = height;
		COL_NUM = width;
		
		board = new Row[ROW_NUM];
		initBoard();
	}

	/*
	 * Initialize the board in preparation for a new game.
	 */
	private void initBoard() {
		// Initialize board array.
		for (int i = 0; i < ROW_NUM; i++)
			board[i] = new Row(COL_NUM);
		
		// Remove any blocks from the previous game.
		activeBlock = generateNextBlock();
		nextBlock = generateNextBlock();
	}
	
	private BlockModel generateNextBlock() {
		//TODO: Add some sort of random generator for different kinds of blocks.
		return new BlockModel(ROW_NUM, COL_NUM);
	}

	/*
	 * The method should be called at a regular interval to update the board by causing
	 * the active blocks position to drop, and handling when an active block settles at
	 * the bottom of the screen.
	 */
	public void incrementBoard() throws Exception {
		// Check if we can move active block down.
		boolean isMovable = true;
		for (Integer[] squarePos : activeBlock) {
			if (getSquare(squarePos) != 0) {
				isMovable = false;
				break;
			}
		}
		
		// If movable, move the active block.
		if (isMovable) {
			activeBlock.lowerPosition();
		// Otherwise write active block to board and start new active block.
		} else {
			try {
				writeActiveBlock();
				// Now that we're done with it, let activeBlock be garbage-collected
				activeBlock = nextBlock;
				nextBlock = generateNextBlock();
			} catch (BlockOverflow e) {
				//TODO: Handle end of game? Or possibly restart.
			}
		}
	}

	/*
	 * Write the current active block to the board in its current position.
	 */
	private void writeActiveBlock() throws Exception {
		for (Integer[] squarePos : activeBlock) {
			try {
				setSquare(squarePos, activeBlock.getCode());
			} catch (Exception e) {
				// If the index was out of bounds above the board, this player has lost.
				if (squarePos[0] >= ROW_NUM) {
					throw new BlockOverflow();
				// Otherwise just throw the original exception.
				} else {
					throw e;
				}
			}
		}
	}

	/*
	 * Set a square on the board.
	 * 
	 * @param squarePos A length two array of form {rowIndex, colIndex} of the square to set.
	 * @param value The value to be assigned to the square.
	 */
	private void setSquare(Integer[] squarePos, int value) throws Exception {
		checkIndices(squarePos);
		board[squarePos[0]].setSquare(squarePos[1], value);
	}

	/*
	 * Get the value of a square on the board.
	 * 
	 * @param squarePos An array of the format {rowIndex, columnIndex}, giving the position of a square.
	 */
	private int getSquare(Integer[] squarePos) throws Exception {
		checkIndices(squarePos);
		
		return board[squarePos[0]].getSquare(squarePos[1]);
	}
	
	/*
	 * Check if indices are valid and throw Exception if not.
	 */
	private void checkIndices(Integer[] squarePos) throws Exception {
		if (squarePos.length != 2)
			throw new Exception("ERROR: Given position array was of length "+squarePos.length);
		if (squarePos[0] < 0 || squarePos[0] > ROW_NUM || squarePos[1] < 0 || squarePos[1] > COL_NUM)
			throw new Exception("ERROR: ("+squarePos[0]+","+squarePos[1]+") is not a valid index.");
	}
}
