package ErraiLearning.client.local;

import com.google.gwt.user.client.ui.FocusPanel;

/*
 * A UI class for a single tile of a tic-tac-toe board.
 */
public class Tile extends FocusPanel {
	
	/* The row index (0-indexed) of this tile in the display. */
	private int row;
	/* The col index (0-indexed) of this tile in the display. */
	private int column;
	
	/*
	 * Construct a tile for a tic-tac-toe board display.
	 * 
	 * @param row The row (0-indexed) of this tile in the display.
	 * @param col The col (0-indexed) of this tile in the display.
	 */
	public Tile(int row, int col) {
		super();
		
		setRow(row);
		setColumn(col);
	}
	
	/*
	 * Get the row (0-indexed) of this tile in the display.
	 * 
	 * @return The row (0-indexed) of this tile in the display.
	 */
	public int getRow() {
		return row;
	}
	
	/*
	 * Set the row index (0-indexed) of this tile in the display.
	 * 
	 * @param row The row index (0-indexed) of this tile in the display.
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/*
	 * Get the column (0-indexed) of this tile in the display.
	 * 
	 * @return The column (0-indexed) of this tile in the display.
	 */
	public int getColumn() {
		return column;
	}

	/*
	 * Set the column index (0-indexed) of this tile in the display.
	 * 
	 * @param column The column index (0-indexed) of this tile in the display.
	 */
	public void setColumn(int column) {
		this.column = column;
	}
}
