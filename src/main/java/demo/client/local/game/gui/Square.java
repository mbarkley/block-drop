package demo.client.local.game.gui;

import com.google.gwt.canvas.dom.client.Context2d;

import demo.client.local.game.tools.Size;
import demo.client.local.game.tools.Size.SizeCategory;

/**
 * A class for drawing individual squares in a {@link Block Block}.
 */
class Square {

  // The default colour of the interior of a square.
  public static final String INTERIOR_DEFAULT = "red";
  // The default colour of the outline of each square.
  public static final String OUTLINE_DEFAULT = "black";

  // The colour of the interior of the block.
  private String interiorColour;
  // The colour of the block outline.
  private String outlineColour;
  private Size size;

  /**
   * Create a Square instance with specified interior and outline colours.
   * 
   * @param interiorColour
   *          The colour of this Square's interior.
   * 
   * @param outlineColour
   *          The colour of this Square's outline.
   * 
   * @param sizeCategory
   *          The enumerated size of this square.
   */
  Square(String interiorColour, String outlineColour, SizeCategory sizeCategory) {
    this.interiorColour = interiorColour;
    this.outlineColour = outlineColour;
    this.size = Size.getSize(sizeCategory);
  }

  /**
   * Create a Square instance with default interior and outline colours.
   * 
   * @param sizeCategory
   *          The enumerated size of this square.
   */
  public Square(SizeCategory sizeCategory) {
    this(INTERIOR_DEFAULT, OUTLINE_DEFAULT, sizeCategory);
  }

  /**
   * Draw this Square onto an HTML5 canvas.
   * 
   * @param x
   *          The x coordinate of the position to draw this square.
   * 
   * @param y
   *          The y coordinate of the position to draw this square.
   * 
   * @param context2d
   *          The context of the canvas on which this square will be drawn.
   */
  void draw(double x, double y, Context2d context2d) {
    // Draw the outline.
    makeOuterRectPath(x, y, context2d);
    context2d.setFillStyle(outlineColour);
    context2d.fill();

    // Draw the interior.
    makeInnerRectPath(x, y, context2d);
    context2d.setFillStyle(interiorColour);
    context2d.fill();
  }

  /**
   * Add this square to the open path on a canvas.
   * 
   * @param x
   *          The x-coordinate of the top-left corner of this square.
   * @param y
   *          The y-coordinate of the top-left corner of this square.
   * @param context2d
   *          The context of the canvas on which this square will be added.
   */
  void addSquareToCanvasPath(double x, double y, Context2d context2d) {
    context2d.rect(x, y, size.getBlockSize(), size.getBlockSize());
  }

  /**
   * Select a rectangular path enclosing an entire square.
   * 
   * @param x
   *          The x coordinate of the rectangle.
   * 
   * @param y
   *          The y coordinate of the rectangle.
   * 
   * @param context2d
   *          The context of the canvas on which to make the rectangle path.
   */
  private void makeOuterRectPath(double x, double y, Context2d context2d) {
    context2d.beginPath();
    context2d.rect(x, y, size.getBlockSize(), size.getBlockSize());
    context2d.closePath();
  }

  /**
   * Select a rectangle path enclosing the interior (not the outline) of this square.
   * 
   * @param x
   *          The x coordinate of the square's position.
   * 
   * @param y
   *          The y coordinate of the sqaure's position.
   * 
   * @param context2d
   *          The context of the canvas on which to select this path.
   */
  private void makeInnerRectPath(double x, double y, Context2d context2d) {
    context2d.beginPath();
    context2d.rect(x + 1, y + 1, size.getBlockSize() - 2, size.getBlockSize() - 2);
    context2d.closePath();
  }

  /**
   * Set the interior colour of this square.
   * 
   * @param cssColour
   *          A string with the value of a CSS colour.
   */
  void setInteriorColour(String cssColour) {
    interiorColour = cssColour;
  }
}
