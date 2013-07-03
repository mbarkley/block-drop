package demo.client.shared.model;

import com.google.gwt.user.client.Random;

/*
 * A model of the Block Drop board.
 */
public class BoardModel {

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
   * The current active block. Tiles in the active block are not recorded on the board until the
   * block is no longer active.
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
   * 
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
    activeBlockColumn = COL_NUM / 2;
  }

  /*
   * Generate the next BlockModel to be used as the active block on this board.
   */
  private BlockModel generateNextBlock() {
    int decider = Random.nextInt(7);

    BlockModel retVal;

    switch (decider) {
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
   * Initialize the next BlockModel to be the active block. This should be called whenever the
   * current active block has settled at the bottom of the board.
   * 
   * @throws BlockOverflow If the BlockModel that is active (before invoking this method) is
   * partially off the screen (i.e. the board is overflowing).
   */
  public void initNextBlock() throws BlockOverflow {
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
   * 
   * @param col The column position of a square.
   * 
   * @return The value of the square, 0 if the square is above the board, and 1 if the square is
   * beside or below the board.
   */
  private int getSquare(int row, int col) {

    // If adjacent spot is beside or below the board, return occupied.
    if (col < 0 || col >= COL_NUM || row >= ROW_NUM) {
      return TILE;
      // If above the board return unoccupied.
    }
    else if (row < 0) {
      return 0;
    }
    else {
      return board[row].getSquareValue(col);
    }
  }

  /*
   * Write the current active block to the board in its current position.
   */
  private void writeActiveBlock() throws BlockOverflow {

    for (SquareModel squareModel : activeBlock.getIterator()) {
      if (activeBlockRow + squareModel.getRow() < 0) {
        throw new BlockOverflow();
      }
      else {
        setSquare(activeBlockRow + squareModel.getRow(), activeBlockColumn + squareModel.getCol(),
                squareModel.getCode());
      }
    }
  }

  /*
   * Get the number of rows on the board which are full.
   * 
   * @return The number of rows on the board which are full.
   */
  public int numFullRows() {

    int retVal = 0;

    for (int i = 0; i < ROW_NUM; i++) {
      if (board[i].isFull()) {
        retVal += 1;
      }
    }

    return retVal;
  }

  /*
   * Set a square on the board.
   * 
   * @param row The row index of the square.
   * 
   * @param col The column index of the square.
   * 
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
      if (getSquare(row + squareModel.getRow(), col + squareModel.getCol()) != NO_TILE) {
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
   * 
   * @param colMove The number of columns to move the active block (positive is right).
   * 
   * @return True iff the block was successfully moved.
   */
  public boolean moveActiveBlock(int rowMove, int colMove) {
    // Check if we can move active block.
    boolean isMovable = isValidPosition(activeBlockRow + rowMove, activeBlockColumn + colMove);

    // If movable, move the active block.
    if (isMovable) {
      // Higher index is lower on board.
      activeBlockRow += rowMove;
      activeBlockColumn += colMove;
      // Return true only if there was actual movement.
      return rowMove != 0 || colMove != 0;
    }
    else {
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
    while (isValidPosition(activeBlockRow + i, activeBlockColumn))
      i += 1;

    return i - 1;
  }

  /*
   * Get a list of models of the blocks which have settled on this BoardModel above any full rows.
   */
  public BackgroundBlockModel getNonFullRows() {
    BackgroundBlockModel retVal = new BackgroundBlockModel();

    for (int i = 0; i < ROW_NUM; i++) {
      if (!board[i].isFull()) {
        for (int j = 0; j < COL_NUM; j++) {
          if (board[i].getSquareValue(j) != NO_TILE) {
            retVal.addSquare(new SquareModel(i, j, board[i].getSquareValue(j)));
          }
        }
      }
    }

    return retVal;
  }

  /*
   * Clear any full rows.
   */
  public void clearFullRows() {
    Row[] newBoard = new Row[ROW_NUM];

    int i = ROW_NUM - 1, j = ROW_NUM - 1;
    while (i >= 0) {
      if (!board[i].isFull()) {
        newBoard[j--] = board[i--];
      }
      else {
        i -= 1;
      }
    }

    // Fill the remaining space in newBoard with clear rows.
    while (j >= 0) {
      newBoard[j--] = new Row(COL_NUM);
    }

    board = newBoard;
  }

  public BlockModel getFullRows() {
    BlockModel retVal = new BackgroundBlockModel();

    for (int i = 0; i < ROW_NUM; i++) {
      if (board[i].isFull()) {
        for (int j = 0; j < COL_NUM; j++) {
          retVal.addSquare(new SquareModel(i, j, board[i].getSquareValue(j)));
        }
      }
    }

    return retVal;
  }

  /*
   * Get the next block.
   * 
   * @return The next block to come in this game.
   */
  public BlockModel getNextBlock() {
    return nextBlock;
  }
}
