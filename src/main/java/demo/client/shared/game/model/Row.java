package demo.client.shared.game.model;

import org.jboss.errai.common.client.api.annotations.Portable;

/*
 * A single row in the Block Drop board.
 */
@Portable
public class Row {

  /* The spots in this row. Indices go left to right. */
  private int[] squares;

  /* The width of this row. */
  private int width;

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }
  
  public Row() {}

  /*
   * Create a row of width size.
   * 
   * @param size The width of this row.
   */
  public Row(int size) {
    width = size;
    // Values are initialized to value 0 (which is unoccupied).
    squares = new int[width];
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
   * 
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
    for (int i = 0; i < width; i++)
      if (squares[i] == 0)
        return false;

    return true;
  }
}