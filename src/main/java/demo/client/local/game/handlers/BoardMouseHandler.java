package demo.client.local.game.handlers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.RootPanel;

import demo.client.local.game.controllers.BoardController;
import demo.client.local.game.tools.Size;

public class BoardMouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler, DoubleClickHandler {

  private BoardController controller;
  private int lastCol;
  private int lastRow;
  private boolean mouseDown = false;
  private Element canvas;

  public BoardMouseHandler(BoardController controller, Element canvas) {
    this.controller = controller;
    this.canvas = canvas;

    // Disable context menu on board.
    RootPanel.get().addDomHandler(new ContextMenuHandler() {

      @Override
      public void onContextMenu(ContextMenuEvent event) {
        if (event.getNativeEvent().getEventTarget().equals(BoardMouseHandler.this.canvas)) {
          event.preventDefault();
          event.stopPropagation();
        }
      }
    }, ContextMenuEvent.getType());
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
      lastCol = coordToIndex(event.getRelativeX(canvas));
      lastRow = coordToIndex(event.getRelativeY(canvas));
      mouseDown = true;
    }
    else if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT
            && event.getNativeEvent().getEventTarget().equals(canvas)) {
      controller.rotateOnce();
    }
  }

  @Override
  public void onMouseMove(MouseMoveEvent event) {
    if (mouseDown) {
      int newCol = coordToIndex(event.getRelativeX(canvas));
      if (newCol - lastCol != 0) {
        controller.setColMoveOnce(newCol - lastCol);
        lastCol = newCol;
      }
      int newRow = coordToIndex(event.getRelativeY(canvas));
      if (newRow - lastRow > 0) {
        controller.setRowMoveOnce(1);
        lastRow = newRow;
      }
    }
  }

  @Override
  public void onMouseUp(MouseUpEvent event) {
    if (event.getNativeButton() == NativeEvent.BUTTON_LEFT)
      mouseDown = false;
  }

  @Override
  public void onDoubleClick(DoubleClickEvent event) {
    controller.setDrop(true);
  }

  private static int coordToIndex(int x) {
    return x / Size.MAIN_BLOCK_SIZE;
  }

}
