package demo.client.local.game.handlers;

import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
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
  private List<Element> elements;
  
  private boolean down;
  private int initialY;
  private int lastY;

  public PauseInputHandler(BoardController controller, List<Element> elements) {
    this.controller = controller;
    this.elements = elements;
  }

  private void onDown(int initialY) {
    down = true;
    this.initialY = initialY;
  }

  private void onUp(int newY) {
    if (down && BoardInputHandler.coordToIndex(newY) + 3 < BoardInputHandler.coordToIndex(this.initialY)) {
      down = false;
      controller.setPaused(!controller.isPaused());
    }
  }
  
  private boolean fromValidTarget(NativeEvent event) {
    EventTarget target = event.getEventTarget();
    for (Element e : elements) {
      if (target.equals(e))
        return true;
    }
    return false;
  }

  @Override
  public void onMouseUp(MouseUpEvent event) {
    onUp(event.getClientY());
    down = false;
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    if (fromValidTarget(event.getNativeEvent()))
      onDown(event.getClientY());
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    down = false;
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    if (BoardTouchHandler.isSingleFinger(event) && fromValidTarget(event.getNativeEvent())) {
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
