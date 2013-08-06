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

/**
 * A mouse input handler for the local player's game. This handler asynchronously sets values in an
 * associated {@link BoardController BoardController} which are handled by the
 * {@link BoardController BoardController} in it's game loop.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class BoardMouseHandler extends BoardInputHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler,
        DoubleClickHandler {

  /**
   * Create a BoardMouseHandler instance.
   * 
   * @param controller
   *          The associated BoardController instance.
   * @param canvas
   *          An HTML5 canvas. Coordinates of mouse movements and clicks will be taken relative to
   *          this canvas.
   */
  public BoardMouseHandler(BoardController controller, Element canvas) {
    super(controller, canvas);
    // Disable context menu on board.
    RootPanel.get().addDomHandler(new ContextMenuHandler() {

      @Override
      public void onContextMenu(ContextMenuEvent event) {
        if (event.getNativeEvent().getEventTarget().equals(BoardMouseHandler.this.canvas)) {
          event.preventDefault();
        }
      }
    }, ContextMenuEvent.getType());
  }

  @Override
  public void onMouseDown(MouseDownEvent event) {
    if (eventFromCanvas(event)) {
      if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
        startMove(event.getRelativeX(canvas), event.getRelativeY(canvas));
        event.preventDefault();
      }
      else if (event.getNativeButton() == NativeEvent.BUTTON_RIGHT
              && event.getNativeEvent().getEventTarget().equals(canvas)) {
        controller.rotateOnce();
      }
    }
  }

  @Override
  public void onMouseMove(MouseMoveEvent event) {
    maybeContinueMove(event.getRelativeX(canvas), event.getRelativeY(canvas));
    event.preventDefault();
  }

  @Override
  public void onMouseUp(MouseUpEvent event) {
    if (event.getNativeButton() == NativeEvent.BUTTON_LEFT) {
      endMove();
      event.preventDefault();
    }
  }

  @Override
  public void onDoubleClick(DoubleClickEvent event) {
    drop();
    event.preventDefault();
  }

}
