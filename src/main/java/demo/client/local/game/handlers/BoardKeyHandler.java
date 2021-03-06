package demo.client.local.game.handlers;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import demo.client.local.game.controllers.BoardController;
import demo.client.local.game.controllers.SecondaryDisplayController;

/**
 * A keyboard input handler for the local player's game. This handler asynchronously sets values in
 * an associated {@link BoardController BoardController} which are handled by the
 * {@link BoardController BoardController} in it's game loop.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class BoardKeyHandler implements KeyDownHandler, KeyUpHandler {

  private BoardController controller;
  private SecondaryDisplayController secondaryController;
  private static final int KEY_SPACE_BAR = 32;

  /**
   * Create a BoardKeyHandler instance.
   * 
   * @param boardController
   *          The BoardController associated with this handler.
   */
  public BoardKeyHandler(BoardController boardController) {
    controller = boardController;
    secondaryController = controller.getSecondaryController();
  }

  @Override
  public void onKeyDown(KeyDownEvent event) {

    int keyCode = event.getNativeKeyCode();

    /*
     * If the user pressed a key used by this game, stop the event from bubbling up to prevent
     * scrolling or other undesirable events.
     */
    if (keyDownHelper(keyCode)) {
      event.stopPropagation();
      event.preventDefault();
    }
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {

    int keyCode = event.getNativeKeyCode();

    /*
     * If the user pressed a key used by this game, stop the event from bubbling up to prevent
     * scrolling or other undesirable events.
     */
    if (keyUpHelper(keyCode)) {
      event.stopPropagation();
      event.preventDefault();
    }
  }

  private boolean keyUpHelper(int keyCode) {
    boolean relevantKey = true;

    // Only arrow keys must be dealt with on key release.
    switch (keyCode) {
    case KeyCodes.KEY_LEFT:
    case KeyCodes.KEY_RIGHT:
      controller.setColMove(0);
      break;
    case KeyCodes.KEY_UP:
      controller.clearRotation();
      break;
    case KeyCodes.KEY_DOWN:
      controller.setFast(false);
      break;
    default:
      relevantKey = false;
      break;
    }

    return relevantKey;
  }

  /*
   * Alter the current state based on user input. Return true if the input captured a relevant
   * command.
   */
  private boolean keyDownHelper(int keyCode) {
    boolean relevantKey = true;
    // Handle pause separately.
    // Don't want to accept other input while paused.
    if (!controller.isPaused()) {
      switch (keyCode) {
      case KeyCodes.KEY_LEFT:
        controller.setColMove(-1);
        break;
      case KeyCodes.KEY_RIGHT:
        controller.setColMove(1);
        break;
      case KeyCodes.KEY_UP:
        controller.incrementRotate();
        break;
      case KeyCodes.KEY_DOWN:
        controller.setFast(true);
        break;
      case KEY_SPACE_BAR:
        controller.setDrop(true);
        break;
      case 80: // Ordinal of lower case p
        controller.setPaused(true);
        break;
      case 78: // Ordinal of lower case n
        secondaryController.selectNextPlayer();
        break;
      case 66: // Ordinal of lower case b
        secondaryController.selectLastPlayer();
        break;
      default:
        relevantKey = false;
        break;
      }
    }
    else {
      // If paused, capture and ignore commands other than pause.
      switch (keyCode) {
      case KeyCodes.KEY_LEFT:
      case KeyCodes.KEY_RIGHT:
      case KeyCodes.KEY_UP:
      case KeyCodes.KEY_DOWN:
      case KEY_SPACE_BAR:
        break;
      case 78: // Ordinal of lower case n
        secondaryController.selectNextPlayer();
        break;
      case 66: // Ordinal of lower case b
        secondaryController.selectLastPlayer();
        break;
      case 80: // Ordinal of lower case p
        controller.setPaused(false);
        break;
      default:
        relevantKey = false;
      }
    }

    return relevantKey;
  }

}
