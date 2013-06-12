package demo.client.shared.model;

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