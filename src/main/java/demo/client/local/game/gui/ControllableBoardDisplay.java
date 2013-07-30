package demo.client.local.game.gui;

import demo.client.local.game.tools.Size.SizeCategory;

/**
 * A display for drawing a Block Drop game board.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public interface ControllableBoardDisplay {

  /**
   * Undraw the given block from this display.
   * 
   * @param x
   *          The x coordinate of the position of the block to be undrawn.
   * 
   * @param y
   *          The y coordinate of the position of the block to be undrawn.
   * 
   * @param activeBlock
   *          The block to undraw.
   */
  public void undrawBlock(double x, double y, Block activeBlock);

  /**
   * Draw the given block on this display.
   * 
   * @param x
   *          The x coordinate of the position of the block.
   * 
   * @param y
   *          The y coordinate of the position of the block.
   * 
   * @param activeBlock
   *          The block to draw.
   */
  public void drawBlock(double x, double y, Block activeBlock);

  /**
   * Draw a pause message on this display.
   */
  public void pause();

  /**
   * Undraw a pause message from this display.
   */
  public void unpause();

  /**
   * Clear this display.
   */
  public void clearBoard();

  /**
   * Get the enumerated size descriptor of this display.
   * 
   * @return The enumerated size category used to retrieve a Size object for drawing to this
   *         display.
   */
  public SizeCategory getSizeCategory();

  /**
   * Display a Game Over prompt to the user.
   */
  public void gameOver();
}