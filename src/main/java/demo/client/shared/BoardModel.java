package demo.client.shared;

import com.google.gwt.user.client.Random;

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
	/* The row position of the active block on this board. */
	private int activeBlockRow;
	/* The column position of the active block on this board. */
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
	
	/*
	 * Initialize position of the active BlockModel.
	 */
	private void initActiveBlockPosition() {
		// Start active block above board.
		activeBlockRow = -1;
		activeBlockColumn = COL_NUM/2;
	}
	
	/*
	 * Generate the next BlockModel to be used as the active block on this board.
	 */
	private BlockModel generateNextBlock() {
		int decider = Random.nextInt(7);
		
		BlockModel retVal;
		
		switch(decider) {
			case 0:
				retVal = new LBlockModel();
				break;
			case 1:
				retVal = new ReverseLBlockModel();
				break;
			case 2:
				retVal = new TBlockModel();
				break;
			case 3:
				retVal = new SBlockModel();
				break;
			case 4:
				retVal = new ZBlockModel();
				break;
			case 5:
				retVal = new LongBlockModel();
				break;
			case 6:
				retVal = new SquareBlockModel();
				break;
			default:
				retVal = null;
				break;
		}
		
		return retVal;
	}

	/*
	 * Initialize the next BlockModel to be the active block. This should be called
	 * whenever the current active block has settled at the bottom of the board.
	 * 
	 * @throws If the BlockModel that is active (before invoking this method) is partially off
	 * the screen (i.e. the board is overflowing).
	 */
	public void initNextBlock() throws BlockOverflow, RowsFullException {
		if (activeBlock != null)
			writeActiveBlock();
		activeBlock = nextBlock;
		nextBlock = generateNextBlock();
		initActiveBlockPosition();
	}
	
	/*
	 * Get value of the square at (row,col).
	 * 
	 * @param row The row position of a square.
	 * @param col The column position of a square.
	 * 
	 * @return The value of the square, 0 if the square is above the board, and 1 if
	 * the square is beside or below the board.
	 */
	private int getSquare(int row, int col) {
		
		// If adjacent spot is beside or below the board, return occupied.
		if (col < 0 || col >= COL_NUM || row >= ROW_NUM) {
			return TILE;
		// If above the board return unoccupied.
		} else if (row < 0) {
			return 0;
		} else {
			return board[row].getSquareValue(col);
		}
	}

	/*
	 * Write the current active block to the board in its current position.
	 * 
	 * @throws BlockOverflow If the block to be written is partially above the board.
	 */
	private void writeActiveBlock() throws BlockOverflow, RowsFullException {
		
		for (SquareModel squareModel : activeBlock.getIterator()) {
			if (activeBlockRow + squareModel.getRow() < 0) {
				throw new BlockOverflow();
			} else {
				setSquare(
						activeBlockRow+squareModel.getRow(),
						activeBlockColumn+squareModel.getCol(),
						activeBlock.getCode()
				);
			}
		}
		
		// Check if any rows have been cleared.
		int fullRows = numFullRows();
		
		if (fullRows > 0) {
			throw new RowsFullException(fullRows);
		}
	}

	/*
	 * Get the number of consecutive rows from the bottom of the board which are full.
	 * 
	 * @return The number of consecutive rows from the bottom of the board which are full.
	 */
	private int numFullRows() {

		int retVal = 0;
		for (int i = ROW_NUM-1; i >= 0; i--) {
			if (!board[i].isFull()) {
				break;
			}
			retVal += 1;
		}
		
		return retVal;
	}

	/*
	 * Set a square on the board.
	 * 
	 * @param row The row index of the square.
	 * @param col The column index of the square.
	 * @param value The value to be assigned to the square.
	 */
	private void setSquare(int row, int col, int value) {
		board[row].setSquare(col, value);
	}

	/*
	 * Get the active block.
	 * 
	 * @return The active BlockModel on this board.
	 */
	public BlockModel getActiveBlock() {
		return activeBlock;
	}

	/*
	 * Get the row position of the active block.
	 * 
	 * @return The row index of the active block.
	 */
	public int getActiveBlockRow() {
		return activeBlockRow;
	}

	/*
	 * Get the column index of the active block.
	 * 
	 * @return The column index of the active block.
	 */
	public int getActiveBlockCol() {
		return activeBlockColumn;
	}
	
	/*
	 * Check if the active block is in a valid position.
	 */
	private boolean isValidPosition(int row, int col) {
		boolean isMovable = true;
		for (SquareModel squareModel : activeBlock.getIterator()) {
			if (getSquare(row+squareModel.getRow(), col+squareModel.getCol()) != 0) {
				isMovable = false;
				break;
			}
		}
		
		return isMovable;
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
		boolean isMovable = isValidPosition(activeBlockRow+rowMove, activeBlockColumn+colMove);
		
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

	/*
	 * Rotate the current active block if this is possible.
	 */
	public void rotateActiveBlock() {
		// Try rotating block.
		activeBlock.rotate();
		
		// If this position is invalid, undo it.
		if (!isValidPosition(activeBlockRow, activeBlockColumn)) {
			activeBlock.unrotate();
			System.out.println("Rotation failed.");
		}
	}

	/*
	 * Get the greatest distance directly downward that the active block can travel.
	 */
	public int getDrop() {
		int i = 0;
		while (isValidPosition(activeBlockRow+i, activeBlockColumn))
			i += 1;
		
		return i-1;
	}

	public BackgroundBlockModel getBackgroundBlock() {
		
		BackgroundBlockModel retVal = new BackgroundBlockModel();
		
		for (int i = 0; i < ROW_NUM; i++) {
			for (int j = 0; j < COL_NUM; j++) {
				
			}
		}

		return retVal;
	}
}
