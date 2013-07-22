package demo.client.local.game.tools;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import demo.client.local.game.controllers.BoardController;

public class BoardMouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

  private BoardController controller;
  private int lastCol;
  private boolean mouseDown = false;

  public BoardMouseHandler(BoardController controller) {
    this.controller = controller;
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    lastCol = getCol(event.getX());
    mouseDown = true;
  }
  
  @Override
  public void onMouseMove(MouseMoveEvent event) {
    if (mouseDown) {
      int newCol = getCol(event.getX());
      if (newCol - lastCol != 0) {
        controller.setColMoveOnce(newCol - lastCol);
        lastCol = newCol;
      }
    }
  }

  @Override
  public void onMouseUp(MouseUpEvent event) {
    mouseDown = false;
  }

  private int getCol(int x) {
    return x / Size.MAIN_BLOCK_SIZE;
  }

}
