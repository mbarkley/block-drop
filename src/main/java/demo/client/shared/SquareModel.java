package demo.client.shared;

public class SquareModel {

	private int rowOffset;
	private int colOffset;
	
	public SquareModel(int rowIndex, int colIndex) {
		this.rowOffset = rowIndex;
		this.colOffset = colIndex;
	}

	public int getCol() {
		return colOffset;
	}

	public int getRow() {
		return rowOffset;
	}

	public void setRow(int newRowOffset) {
		rowOffset = newRowOffset;
	}

	public void setCol(int newColOffset) {
		colOffset = newColOffset;
	}
}
