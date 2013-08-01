package demo.client.local.game.tools;

import demo.client.shared.game.model.BlockModel;
import demo.client.shared.game.model.SquareModel;

public class Positioner {
  
  private int lowestRow;
  private int highestRow;
  private int lowestCol;
  private int highestCol;

  public Positioner(BlockModel model) {
    SquareModel temp = model.getIterator().iterator().next();
    lowestCol = temp.getCol();
    highestCol = temp.getCol();
    lowestRow = temp.getRow();
    highestRow = temp.getRow();
    for (SquareModel square : model.getIterator()) {
      lowestRow = lowestRow > square.getRow() ? square.getRow() : lowestRow;
      highestRow = highestRow < square.getRow() ? square.getRow() : highestRow;
      lowestCol = lowestCol > square.getCol() ? square.getCol() : lowestCol;
      highestCol = highestCol < square.getCol() ? square.getCol() : highestCol;
    }
  }
  
  public double getRowShift() {
    return getShift(lowestRow, highestRow);
  }
  
  public double getColShift() {
    return getShift(lowestCol, highestCol);
  }

  private double getShift(int low, int high) {
    double mid = (high + 1 + low) / 2.0;
    
    return mid;
  }

}
