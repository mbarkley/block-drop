package demo.client.local.game.handlers;

import com.google.gwt.dom.client.Element;

import demo.client.local.game.controllers.BoardController;
import demo.client.local.game.tools.Size;
import demo.client.local.game.tools.Size.SizeCategory;

/**
 * A base class for handling user input on a Block Drop canvas from mouse or touch events.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class BoardInputHandler {

  protected BoardController controller;
  protected Element canvas;

  protected int lastCol;
  protected int lastRow;

  protected boolean down;

  /**
   * Create a BoardInputHandler.
   * 
   * @param controller
   *          The BoardController to manipulate.
   * @param canvas
   *          The element to use for relative coordinates.
   */
  protected BoardInputHandler(BoardController controller, Element canvas) {
    this.controller = controller;
    this.canvas = canvas;
  }

  /**
   * Start a block movmement. Used when a user first clicks/touches the screen to drag a block.
   * 
   * @param relativeX
   * @param relativeY
   */
  protected void startMove(int relativeX, int relativeY) {
    lastCol = coordToIndex(relativeX);
    lastRow = coordToIndex(relativeY);
    down = true;
  }

  /**
   * Possibly continue dragging a block. For a mouse, dragging will only occur if this is called
   * after {@link #startMove(int, int) startMove} is called.
   * 
   * @param relativeX
   *          The relative X coordinate of this movement.
   * @param relativeY
   *          The realtive Y coordinate of this movement.
   */
  protected void maybeContinueMove(int relativeX, int relativeY) {
    if (down) {
      int newCol = coordToIndex(relativeX);
      if (newCol - lastCol != 0) {
        controller.setColMoveOnce(newCol - lastCol);
        lastCol = newCol;
      }
      int newRow = coordToIndex(relativeY);
      if (newRow - lastRow > 0) {
        controller.setRowMoveOnce(newRow - lastRow);
        lastRow = newRow;
      }
    }
  }

  /**
   * End a dragging movement.
   */
  protected void endMove() {
    down = false;
  }

  /**
   * Rotate the active block on the board. See
   * {@link demo.client.local.game.controllers.BoardController#rotateOnce()
   * BoardController.rotateOnce} for details.
   */
  protected void rotateOnce() {
    controller.rotateOnce();
  }

  /**
   * Drop the active block. See
   * {@link demo.client.local.game.controllers.BoardController#setDrop(boolean)
   * BoardController.setDrop} for details.
   */
  protected void drop() {
    controller.setDrop(true);
  }

  /**
   * Convert pixel coordinates on canvas to indices.
   * 
   * @param x
   *          A pixel coordinate.
   * @return The index this coordinate maps to.
   */
  protected static int coordToIndex(double x) {
    return (int) (x / Size.getSize(SizeCategory.MAIN).getBlockSize());
  }
}
