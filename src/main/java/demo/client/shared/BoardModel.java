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
		public int getSquareValue(int index) {
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
		public void setSquare(int index, int value) {
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
	}

	/* The value representing a vacant spot on the board. */
	public static final int NO_TILE = 0;
	/* The value representing an occupied spot on the board. */
	public static final int TILE = 1;
	
	/* The number of rows in this board. */
	public final static int ROW_NUM = 15;
	/* The number of columns in this board. */
	public final static int COL_NUM = 10;
	
	/* The rows of the board. Lower indices represent lower rows. */
	private Row[] board;
	/* 
	 * The current active block. Tiles in the active block are not recorded on the board
	 * until the block is no longer active.
	 */
	private BlockModel activeBlock = null;
	/* The next block to become active. */
	private BlockModel nextBlock = null;
	private int activeBlockRow;
	private int activeBlockColumn;
	
	/*
	 * Create a BoardModel.
	 * 
	 * @param width The number of squares in the width of the board.
	 * @param height The number of squares in the height of the board.
	 */
	public BoardModel() {
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
		
		initActiveBlockPosition();
	}
	
	private void initActiveBlockPosition() {
		// Start active block above board.
		activeBlockRow = -1;
		activeBlockColumn = COL_NUM/2;
	}
	
	private BlockModel generateNextBlock() {
		//TODO: Add some sort of random generator for different kinds of blocks.
		return new BlockModel();
	}

	/*
	 * The method should be called at a regular interval to update the board by causing
	 * the active blocks position to drop, and handling when an active block settles at
	 * the bottom of the screen.
	 * 
	 * @return True iff a piece moved.
	 */
	public boolean lowerActiveBlock() throws BlockOverflow {
		// Check if we can move active block down.
		boolean moved = moveActiveBlock(1, 0);
		
		// If it didn't move, write it to the board.
		if (!moved) {
			try {
				writeActiveBlock();
				// Switch to new active block.
				return false;
			} catch (BlockOverflow e) {
				//TODO: Handle end of game? Or possibly restart.
				throw e;
			}
		}
		
		return moved;
	}

	public void initNextBlock() throws BlockOverflow {
		if (activeBlock != null)
			writeActiveBlock();
		activeBlock = nextBlock;
		nextBlock = generateNextBlock();
		initActiveBlockPosition();
	}
	
	/*
	 * Get value in square adjacent to squarePos.
	 * 
	 * @param squarePos The position coordinate (row,column) of the current square.
	 * @param rowDir Either +1, 0, or -1.
	 * @param colDir Either +1, 0, or -1.
	 * 
	 * @return The value of the adjacent square, or 1 (occupied) if this adjacent spot would be off board.
	 */
	private int getSquareAdjacent(Integer[] squarePos, int rowDir, int colDir) {
		int x = squarePos[1] + colDir;
		int y = squarePos[0] + rowDir;
		
		// If adjacent spot is off-board, return occupied.
		if (x < 0 || x >= COL_NUM || y < 0 || y >= ROW_NUM) {
			return TILE;
		} else {
			return board[y].getSquareValue(x);
		}
	}

	/*
	 * Write the current active block to the board in its current position.
	 */
	private void writeActiveBlock() throws BlockOverflow {
		for (Integer[] squarePos : activeBlock.getIterator(activeBlockRow, activeBlockColumn)) {
			// If the index was out of bounds above the board, this player has lost.
			if (squarePos[0] < 0) {
				throw new BlockOverflow();
			} else {
				setSquare(squarePos, activeBlock.getCode());
			}
		}
	}

	/*
	 * Set a square on the board.
	 * 
	 * @param squarePos A length two array of form {rowIndex, colIndex} of the square to set.
	 * @param value The value to be assigned to the square.
	 */
	private void setSquare(Integer[] squarePos, int value) {
		board[squarePos[0]].setSquare(squarePos[1], value);
	}

	public BlockModel getActiveBlock() {
		return activeBlock;
	}

	public int getActiveBlockRow() {
		return activeBlockRow;
	}

	public int getActiveBlockCol() {
		return activeBlockColumn;
	}
	
	/*
	 * Move the active block.
	 * 
	 * @param rowMove The number of rows to move the active block (positive is down).
	 * @param colMove The number of columns to move the active block (positive is right).
	 * 
	 * @return True iff the block was successfully moved.
	 */
	public boolean moveActiveBlock(int rowMove, int colMove) throws BlockOverflow {
		// Check if we can move active block.
		boolean isMovable = true;
		for (Integer[] squarePos : activeBlock.getIterator(activeBlockRow, activeBlockColumn)) {
			if (getSquareAdjacent(squarePos, rowMove, colMove) != 0) {
				isMovable = false;
				break;
			}
		}
		
		// If movable, move the active block.
		if (isMovable) {
			// Higher index is lower on board.
			activeBlockRow += rowMove;
			activeBlockColumn += colMove;
			// Return true only if there was actual movement.
			return rowMove != 0 || colMove != 0;
		} else {
			// If this block couldn't move down from above the board, the board is overflowing.
			if (rowMove == 1 && activeBlockRow < 0)
				throw new BlockOverflow();
			return false;
		}
	}
}
