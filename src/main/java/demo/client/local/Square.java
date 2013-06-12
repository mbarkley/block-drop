package demo.client.local;

import com.google.gwt.canvas.dom.client.Context2d;

/*
 * A class for drawing individual squares in a Block Drop block.
 */
public class Square {
	
	/* The width and height (in pixels) of each square. */
	public static final int SIZE = 60;
	/* The default colour of the interior of a square. */
	public static final String INTERIOR_DEFAULT = "red";
	/* The default colour of the outline of each square. */
	public static final String OUTLINE_DEFAULT = "black";

	/* The colour of the interior of the block. */
	private String interiorColour;
	/* The colour of the block outline. */
	private String outlineColour;
	
	/*
	 * Create a Square instance with default colours.
	 */
	public Square() {
		this(INTERIOR_DEFAULT, OUTLINE_DEFAULT);
	}
	
	/*
	 * Create a Square instance with specified interior colour.
	 * 
	 * @param interiorColour The colour of the interior of this square.
	 */
	public Square(String interiorColour) {
		this(interiorColour, OUTLINE_DEFAULT);
	}
	
	/*
	 * Create a Square instance with specified interior and outline colours.
	 * 
	 * @param interiorColour The colour of this Square's interior.
	 * @param outlineColour The colour of this Square's outline.
	 */
	public Square(String interiorColour, String outlineColour) {
		this.interiorColour = interiorColour;
		this.outlineColour = outlineColour;
	}
	
	/*
	 * Draw this Square onto an HTML5 canvas.
	 * 
	 * @param x The x coordinate of the position to draw this square.
	 * @param y The y coordinate of the position to draw this square.
	 * @param context2d The context of the canvas on which this square will be drawn.
	 */
	public void draw(double x, double y, Context2d context2d) {
		// Draw the outline.
		makeOuterRectPath(x, y, context2d);
		context2d.setFillStyle(outlineColour);
		context2d.fill();
		
		// Draw the interior.
		makeInnerRectPath(x, y, context2d);
		context2d.setFillStyle(interiorColour);
		context2d.fill();
	}
	
	/* Does not begin or close path. Can be used to select a whole line of squares. */
	public void addSquareToCanvasPath(double x, double y, Context2d context2d) {
		context2d.rect(x, y, SIZE, SIZE);
	}
	
	/*
	 * Select a rectangle path enclosing an entire square.
	 * 
	 * @param x The x coordinate of the rectangle.
	 * @param y The y coordinate of the rectangle.
	 * @param context2d The context of the canvas on which to make the rectangle path.
	 */
	private void makeOuterRectPath(double x, double y, Context2d context2d) {
		context2d.beginPath();
		context2d.rect(x, y, SIZE, SIZE);
		context2d.closePath();
	}
	
	/*
	 * Select a rectangle path enclosing the interior (not the outline) of this square.
	 * 
	 * @param x The x coordinate of the square's position.
	 * @param y The y coordinate of the sqaure's position.
	 * @param context2d The context of the canvas on which to select this path.
	 */
	private void makeInnerRectPath(double x, double y, Context2d context2d) {
		context2d.beginPath();
		context2d.rect(x+1, y+1, SIZE-2, SIZE-2);
		context2d.closePath();
	}
	
	/*
	 * Set the interior colour of this square.
	 * 
	 * @param cssColour A string with the value of a CSS colour.
	 */
	public void setInteriorColour(String cssColour) {
		interiorColour = cssColour;
	}
}
