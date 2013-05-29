package ErraiLearning.client.local;

import com.google.gwt.user.client.ui.FocusPanel;

public class Tile extends FocusPanel {
	
	// Both 0-indexed.
	private int row;
	private int column;
	
	public Tile(int row, int col) {
		super();
		
		setRow(row);
		setColumn(col);
	}
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	
	
}
