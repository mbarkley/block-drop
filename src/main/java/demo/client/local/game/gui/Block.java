package demo.client.local.game.gui;

import com.google.gwt.canvas.dom.client.Context2d;

import demo.client.local.game.tools.ColourMapper;
import demo.client.local.game.tools.Size;
import demo.client.local.game.tools.Size.SizeCategory;
import demo.client.shared.game.model.BlockModel;
import demo.client.shared.game.model.SquareModel;

/**
 * A class for drawing Block Drop blocks on a HTML5 canvas.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class Block {

  private BlockModel model;
  private SizeCategory sizeCategory;

  /**
   * Create a Block instance.
   * 
   * @param blockModel
   *          The model which this Block will draw.
   * @param sizeCategory
   *          The size information used to determine the dimensions of the squares in this block.
   */
  public Block(BlockModel blockModel, SizeCategory sizeCategory) {
    model = blockModel;
    this.sizeCategory = sizeCategory;
  }

  /**
   * Draw this block on the given context.
   * 
   * @param x
   *          The x coordinate of the position at which to draw this block.
   * 
   * @param y
   *          The y coordinate of the position at which to draw this block.
   * 
   * @param context2d
   *          The context on which to draw this block.
   */
  public void draw(double x, double y, Context2d context2d) {
    Square square = new Square(sizeCategory);
    // Get an iterator of square coordinates based around the given coordinate (x,y).
    for (SquareModel squareModel : model.getIterator()) {
      square.setInteriorColour(ColourMapper.codeToColour(squareModel.getCode()));
      square.draw(x + indexToCoord(squareModel.getCol(), sizeCategory),
              y + indexToCoord(squareModel.getRow(), sizeCategory), context2d);
    }
  }

  /**
   * Check if a given BlockModel is the model for this Block type.
   * 
   * @param activeBlock
   *          The block model to compare against the the model stored in this Block.
   * 
   * @return True iff the given BlockModel is the same as the model of this Block instance.
   */
  public boolean isModel(BlockModel activeBlock) {
    return model.equals(activeBlock);
  }

  /**
   * Get a path on the given context surrounding all the squares in this block.
   * 
   * @param x
   *          The x coordinate of this block's position.
   * 
   * @param y
   *          The y coordinate of this block's position.
   * 
   * @param context2d
   *          The context on which to create this path.
   */
  void getPath(int x, int y, Context2d context2d) {
    Square square = new Square(sizeCategory);
    for (SquareModel squareModel : model.getIterator()) {
      square.addSquareToCanvasPath(x + indexToCoord(squareModel.getCol(), sizeCategory),
              y + indexToCoord(squareModel.getRow(), sizeCategory), context2d);
    }
  }

  /**
   * Convert an index (used to locate BlockModels on the BoardModel) to a coordinate (used to draw
   * Blocks on the BoardPage canvas).
   * 
   * @param index
   *          The index of a BlockModel on a BoardModel to be converted to a coordinate.
   * 
   * @return The coordinate to pass to the {@link Block#draw(double, double, Context2d)
   *         Block.draw()} method for drawing a block in the correct position.
   */
  public static double indexToCoord(Integer index, SizeCategory sizeCategory) {
    return index * Size.getSize(sizeCategory).getBlockSize();
  }

  /**
   * Get the underlying model for this Block.
   * 
   * @return The model for this Block.
   */
  public BlockModel getModel() {
    return model;
  }
}
