package demo.client.local.game.tools;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import demo.client.local.game.controllers.BoardController;

public class BoardMouseHandler implements MouseDownHandler, MouseUpHandler {

  private BoardController controller;
  private int lastCol;

  public BoardMouseHandler(BoardController controller) {
    this.controller = controller;
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    lastCol = getCol(event.getX());
  }

  @Override
  public void onMouseUp(MouseUpEvent event) {
    int newCol = getCol(event.getX());
    if (newCol - lastCol != 0) {
      controller.setColMoveOnce((newCol - lastCol) / Math.abs(newCol - lastCol));
    }
  }

  private int getCol(int x) {
    return x / Size.MAIN_BLOCK_SIZE;
  }

}
