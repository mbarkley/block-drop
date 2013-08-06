package demo.client.local.game.handlers;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

import demo.client.local.game.controllers.BoardController;

public class PauseInputHandler implements TouchStartHandler, TouchMoveHandler, TouchEndHandler, MouseDownHandler,
        MouseUpHandler {

  private BoardController controller;
  private boolean down;
  private int initialY;
  private int lastY;

  public PauseInputHandler(BoardController controller) {
    this.controller = controller;
  }

  private void onDown(int initialY) {
    down = true;
    this.initialY = initialY;
  }

  private void onUp(int newY) {
    if (down && BoardInputHandler.coordToIndex(newY + 1) < BoardInputHandler.coordToIndex(this.initialY)) {
      down = false;
      controller.setPaused(!controller.isPaused());
    }
  }

  @Override
  public void onMouseUp(MouseUpEvent event) {
    onUp(event.getClientY());
    down = false;
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    onDown(event.getClientY());
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    down = false;
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    if (BoardTouchHandler.isSingleFinger(event)) {
      onDown(event.getTouches().get(0).getClientY());
      lastY = initialY;
    }
    else {
      down = false;
    }
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    if (BoardTouchHandler.isSingleFinger(event) && down) {
      lastY = event.getTouches().get(0).getClientY();
      onUp(lastY);
    }
  }

}
