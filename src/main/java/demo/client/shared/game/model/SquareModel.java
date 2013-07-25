package demo.client.shared.game.model;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.marshalling.client.api.annotations.MapsTo;

@Portable
public class SquareModel {

  private int rowOffset;
  private int colOffset;
  private int code;

  public SquareModel(@MapsTo("rowOffset") int rowIndex, @MapsTo("colOffset") int colIndex, @MapsTo("code") int code) {
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
