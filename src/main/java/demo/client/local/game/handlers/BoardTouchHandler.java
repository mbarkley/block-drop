package demo.client.local.game.handlers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;

import demo.client.local.game.controllers.BoardController;

public class BoardTouchHandler extends BoardInputHandler implements TouchStartHandler, TouchMoveHandler,
        TouchEndHandler {

  public BoardTouchHandler(BoardController controller, Element canvas) {
    super(controller, canvas);
  }

  @Override
  public void onTouchEnd(TouchEndEvent event) {
    if (event.getTouches().length() == 0) {
      endMove();
      event.preventDefault();
    }
  }

  @Override
  public void onTouchMove(TouchMoveEvent event) {
    if (isSingleFinger(event)) {
      Touch touch = event.getTouches().get(0);
      maybeContinueMove(touch.getRelativeX(canvas), touch.getRelativeY(canvas));
      event.preventDefault();
    }
  }

  @Override
  public void onTouchStart(TouchStartEvent event) {
    if (isSingleFinger(event)) {
      Touch touch = event.getTouches().get(0);
      startMove(touch.getRelativeX(canvas), touch.getRelativeY(canvas));
      event.preventDefault();
    }
  }
  
  private boolean isSingleFinger(TouchEvent<?> event) {
    return event.getTouches().length() == 1;
  }

}
