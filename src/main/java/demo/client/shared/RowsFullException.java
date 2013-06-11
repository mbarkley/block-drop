package demo.client.shared;

public class RowsFullException extends Exception {
	
	private int numFullRows;

	public RowsFullException(int numFullRows) {
		this.numFullRows = numFullRows;
	}
	
	public int getNumFullRows() {
		return numFullRows;
	}

}
