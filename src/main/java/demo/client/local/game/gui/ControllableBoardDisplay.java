package demo.client.local.game.gui;

import demo.client.local.game.tools.Size.SizeCategory;

public interface ControllableBoardDisplay {

  /*
   * Undraw the given block from this page's mainCanvas. Note: Any path on the mainCanvas will be
   * lost after invoking this method.
   * 
   * @param x The x coordinate of the position of the block.
   * 
   * @param y The y coordinate of the position of the block.
   * 
   * @param activeBlock The block to undraw.
   */
  public void undrawBlock(int x, int y, Block activeBlock);

  /*
   * Draw a block on this page's mainCanvas.
   * 
   * @param x The x coordinate of the position of the block.
   * 
   * @param y The y coordinate of the position of the block.
   * 
   * @param activeBlock The block to draw.
   */
  public void drawBlock(int x, int y, Block activeBlock);

  public void pause();

  public void unpause();

  public void clearBoard();
  
  public SizeCategory getSizeCategory();

  public void gameOver();
}