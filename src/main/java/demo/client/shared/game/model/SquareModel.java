package demo.client.shared.game.model;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * A model of a single square in a {@link BlockModel block model}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Portable
public class SquareModel {

  private int rowOffset;
  private int colOffset;
  private int code;

  /**
   * Create SquareModel instance.
   * 
   * @param rowIndex
   *          The row position of this square.
   * @param colIndex
   *          The column position of this square.
   * @param code
   *          The code for which type of block this square is a part of as per
   *          {@link BlockModel#getCode() BlcokModel.getCode}.
   */
  public SquareModel(@MapsTo("rowOffset") int rowIndex, @MapsTo("colOffset") int colIndex, @MapsTo("code") int code) {
    this.rowOffset = rowIndex;
    this.colOffset = colIndex;
    this.code = code;
  }

  /**
   * Get the column position of this square.
   * 
   * @return The column position of this square.
   */
  public int getCol() {
    return colOffset;
  }

  /**
   * Get the row position of this square.
   * 
   * @return The row position of this square.
   */
  public int getRow() {
    return rowOffset;
  }

  /**
   * Set the row position of this square.
   * 
   * @param newRowOffset
   *          The new row position of this square.
   */
  public void setRow(int newRowOffset) {
    rowOffset = newRowOffset;
  }

  /**
   * Set the column position of this square.
   * 
   * @param newColOffset
   *          The new column position of this square.
   */
  public void setCol(int newColOffset) {
    colOffset = newColOffset;
  }

  /**
   * Get the code for this square, as per {@link BlockModel#getCode() BlockModel.getCode}.
   * 
   * @return The code for this square.
   */
  public int getCode() {
    return code;
  }
}
