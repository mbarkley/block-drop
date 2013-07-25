package demo.client.shared.game.model;

import java.util.ArrayList;
import java.util.List;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * The base class for a an active block in a {@link BoardModel board model}.
 */
@Portable
public class BlockModel {

  /**
   * The code identifying this class of block in a {@link BoardModel board model}.
   */
  public static final int BASIC_CODE = 1;

  private static int idGen = 1;

  /**
   * A list of SquareModels, containing the squares of squares.
   */
  private List<SquareModel> squares;
  /** A unique id for identifying this block. */
  private int id;

  public BlockModel() {
    this(generateId());
  }

  /**
   * Initializes BlockModel id. All subclasses should invoke this constructor using the return value
   * of {@link BlockModel#generateId()} as the argument.
   */
  protected BlockModel(int id) {
    this.id = id;
    squares = new ArrayList<SquareModel>();
  }

  /**
   * Set the squares of the squares in this block from the blocks main position. The position of a
   * single square on the board is calculated as main position + offset.
   * 
   * @param squares
   *          An array of integer pairs, representing row and column squares from this blocks main
   *          position.
   */
  protected void setOffsets(int[][] offsets, int code) {
    this.squares = new ArrayList<SquareModel>();

    for (int i = 0; i < offsets.length; i++) {
      this.squares.add(new SquareModel(offsets[i][0], offsets[i][1], code));
    }
  }

  @Override
  public boolean equals(Object other) {
    return other.getClass() == BlockModel.class && this.getId() == ((BlockModel) other).getId();
  }

  /**
   * Get the id of this BlockModel.
   * 
   * @return The id of this BlockModel.
   */
  public int getId() {
    return id;
  }

  /**
   * Generate a unique id for a BlockModel instance.
   * 
   * @return A unique id.
   */
  protected static int generateId() {
    return idGen++;
  }

  /**
   * Get the integer representing this type of BlockModel on the {@link BoardModel board model}.
   * 
   * @return An integer representing this type of block on the {@link BoardModel board model}.
   */
  public static int getCode() {
    return BASIC_CODE;
  }

  /**
   * Get an iterator for the {@link SquareModel squares} in this BlockModel.
   * 
   * @return An iterator of {@link SquareModel squares}.
   */
  public Iterable<SquareModel> getIterator() {
    return squares;
  }

  /**
   * Rotate the squares in this block by 90 degrees clockwise. BlockModels that do not rotate around
   * a central square should override this method.
   */
  public void rotate() {
    for (SquareModel square : squares) {
      // Calculate new squares (calculation derived from rotation matrix by 90 degrees)
      int newRowOffset = -1 * square.getCol();
      int newColOffset = square.getRow();

      // Assign new squares.
      square.setRow(newRowOffset);
      square.setCol(newColOffset);
    }
  }

  /**
   * Reverse the effect of a call to {@link BlockModel#rotate() rotate()}.
   */
  public void unrotate() {
    for (SquareModel square : squares) {
      // Calculate new squares (calculation derived from rotation matrix by -90 degrees).
      int newRowOffset = square.getCol();
      int newColOffset = -1 * square.getRow();

      // Assign new squares.
      square.setRow(newRowOffset);
      square.setCol(newColOffset);
    }
  }

  /**
   * Add a square to this BlockModel.
   * 
   * @param square
   *          The square to add.
   */
  public void addSquare(SquareModel square) {
    squares.add(square);
  }

  /**
   * Get the row offset of the centre of this shape.
   * 
   * To centre this block vertically around a row index <em>i</em>, place a block at <em>i</em>
   * +getCentreRowDiff().
   * 
   * @return The number to be added to a row index, <em>i</em> in order to vertically centre this
   *         block around <em>i</em>.
   */
  public double getCentreRowDiff() {
    return 0;
  }

  /**
   * Get the column offset of the centre of this shape.
   * 
   * To centre this block horizontally around a column index <em>j</em>, place a block at <em>j</em>
   * +getCentreColDiff().
   * 
   * @return The number to be added to a column index, <em>j</em> in order to horizontally centre
   *         this block around <em>j</em>.
   */
  public double getCentreColDiff() {
    return 0;
  }

  /**
   * Get the starting row of this BlockModel.
   * 
   * @return The row index of the position this block should start at in a {@link BoardModel board
   *         model}.
   */
  public int getStartingRow() {
    return -2;
  }
}
