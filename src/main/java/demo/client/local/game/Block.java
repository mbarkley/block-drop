package demo.client.local.game;

import com.google.gwt.canvas.dom.client.Context2d;

import demo.client.shared.model.BlockModel;
import demo.client.shared.model.SquareModel;

/* A class for drawing Block Drop blocks on an HTML5 canvas. */
class Block {

  /*
   * Get an appropriate subclass of Block based on which subclass of BlockModel is given.
   * 
   * @param activeBlock An object that is an instance of BlockModel or a subclass thereof.
   * 
   * @return An instance of Block or a subclass thereof.
   */
  static Block getBlockInstance(BlockModel activeBlock) {
    return new Block(activeBlock);
  }

  /* The BlockModel associated with this instance. */
  private BlockModel model;

  /*
   * Create a Block instance.
   */
  Block(BlockModel blockModel) {
    model = blockModel;
  }

  /*
   * Draw this block on the given context.
   * 
   * @param x The x coordinate of the position at which to draw this block.
   * 
   * @param y The y coordinate of the position at which to draw this block.
   * 
   * @param context2d The context on which to draw this block.
   */
  void draw(double x, double y, Context2d context2d) {
    Square square = new Square();
    // Get an iterator of square coordinates based around the given coordinate (x,y).
    for (SquareModel squareModel : model.getIterator()) {
      square.setInteriorColour(ColorMapper.codeToColour(squareModel.getCode()));
      square.draw(x + indexToCoord(squareModel.getCol()), y + indexToCoord(squareModel.getRow()), context2d);
    }
  }

  /*
   * Check if a given BlockModel is the model for this Block type.
   * 
   * @param activeBlock The block model to compare against the the model stored in this Block.
   * 
   * @return True iff the given BlockModel is the same as the model of this Block instance.
   */
  boolean isModel(BlockModel activeBlock) {
    return model.equals(activeBlock);
  }

  /*
   * Get a path on the given context surrounding all the squares in this block.
   * 
   * @param x The x coordinate of this block's position.
   * 
   * @param y The y coordinate of this block's position.
   * 
   * @param context2d The context on which to create this path.
   */
  void getPath(int x, int y, Context2d context2d) {
    Square square = new Square();
    for (SquareModel squareModel : model.getIterator()) {
      square.addSquareToCanvasPath(x + indexToCoord(squareModel.getCol()), y + indexToCoord(squareModel.getRow()),
              context2d);
    }
  }

  void rotate() {
    model.rotate();
  }

  double getCentreRowDiff() {
    return model.getCentreRowDiff();
  }

  double getCentreColDiff() {
    return model.getCentreColDiff();
  }

  /*
   * Convert an index (used to locate BlockModels on the BoardModel) to a coordinate (used to draw
   * Blocks on the BoardPage canvas).
   * 
   * @param index The index of a BlockModel on a BoardModel to be converted to a coordinate.
   * 
   * @return The coordinate to pass to the Block.draw method for drawing a block in the correct
   * position.
   */
  static int indexToCoord(Integer index) {
    return index * Size.BLOCK_SIZE;
  }
}
