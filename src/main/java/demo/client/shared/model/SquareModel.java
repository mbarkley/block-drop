package demo.client.shared.model;

public class SquareModel {

  private int rowOffset;
  private int colOffset;
  private int code;

  public SquareModel(int rowIndex, int colIndex, int code) {
    this.rowOffset = rowIndex;
    this.colOffset = colIndex;
    this.code = code;
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

  public int getCode() {
    return code;
  }
}
