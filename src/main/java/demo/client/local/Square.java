package demo.client.local;

import com.google.gwt.canvas.dom.client.Context2d;

public class Square {
	
	public static final int SIZE = 60;
	public static final String INTERIOR_DEFAULT = "red";
	public static final String OUTLINE_DEFAULT = "black";

	/* The colour of the interior of the block. */
	private String interiorColour;
	/* The colour of the block outline. */
	private String outlineColour;
	
	public Square() {
		this(INTERIOR_DEFAULT, OUTLINE_DEFAULT);
	}
	
	public Square(String interiorColour) {
		this(interiorColour, OUTLINE_DEFAULT);
	}
	
	public Square(String interiorColour, String outlineColour) {
		this.interiorColour = interiorColour;
		this.outlineColour = outlineColour;
	}
	
	public void draw(double x, double y, Context2d context2d) {
		makeOuterRectPath(x, y, context2d);
		context2d.setFillStyle(outlineColour);
		context2d.fill();
		
		makeInnerRectPath(x, y, context2d);
		context2d.setFillStyle(interiorColour);
		context2d.fill();
	}
	
	/* Does not begin or close path. Can be used to select a whole line of squares. */
	public void addSquareToCanvasPath(double x, double y, Context2d context2d) {
		context2d.rect(x, y, SIZE, SIZE);
	}
	
	private void makeOuterRectPath(double x, double y, Context2d context2d) {
		context2d.beginPath();
		context2d.rect(x, y, SIZE, SIZE);
		context2d.closePath();
	}
	
	private void makeInnerRectPath(double x, double y, Context2d context2d) {
		context2d.beginPath();
		context2d.rect(x+1, y+1, SIZE-2, SIZE-2);
		context2d.closePath();
	}
}
