package demo.client.local.game.tools;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import demo.client.local.game.controllers.BoardController;

public class BoardMouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, DoubleClickHandler {

  private BoardController controller;
  private int lastCol;
  private boolean mouseDown = false;
  private Element canvas;

  public BoardMouseHandler(BoardController controller, Element canvas) {
    this.controller = controller;
    this.canvas = canvas;
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    lastCol = getCol(event.getRelativeX(canvas));
    mouseDown = true;
  }
  
  @Override
  public void onMouseMove(MouseMoveEvent event) {
    if (mouseDown) {
      int newCol = getCol(event.getRelativeX(canvas));
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

  @Override
  public void onDoubleClick(DoubleClickEvent event) {
    controller.rotateOnce();
  }
  
  private int getCol(int x) {
    return x / Size.MAIN_BLOCK_SIZE;
  }

}
