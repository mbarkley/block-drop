package demo.client.local.game;

import com.google.gwt.canvas.client.Canvas;

public class BoardCanvas {

  /* The background colour of the Block Drop board. */
  public static final String BOARD_COLOUR = "rgb(255,255,255)";

  private Canvas canvas;

  public BoardCanvas(Canvas canvas) {
    this.canvas = canvas;
  }

  /*
   * Fill the current path on this page's mainCanvas with the board background colour.
   */
  private void drawBackground() {
    canvas.getContext2d().setFillStyle(BOARD_COLOUR);
    canvas.getContext2d().fill();
  }

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
  public void undrawBlock(int x, int y, Block activeBlock) {
    canvas.getContext2d().beginPath();
    activeBlock.getPath(x, y, canvas.getContext2d());
    canvas.getContext2d().closePath();
    drawBackground();
  }

  /*
   * Draw a block on this page's mainCanvas.
   * 
   * @param x The x coordinate of the position of the block.
   * 
   * @param y The y coordinate of the position of the block.
   * 
   * @param activeBlock The block to draw.
   */
  public void drawBlock(int x, int y, Block activeBlock) {
    activeBlock.draw(x, y, canvas.getContext2d());
  }

}
