package demo.client.local;

import com.google.gwt.canvas.dom.client.Context2d;

import demo.client.shared.BlockModel;

/* A class for drawing Block Drop blocks on an HTML5 canvas. */
public class Block {
	
	/* The width and height of each square in this block (in pixels). */
	public static final int SIZE = 60;

	/* 
	 * Get an appropriate subclass of Block based on which subclass of BlockModel is given.
	 * 
	 * @param activeBlock An object that is an instance of BlockModel or a subclass thereof.
	 * 
	 * @return An instance of Block or a subclass thereof.
	 */
	public static Block getBlockInstance(BlockModel activeBlock) {
		// TODO: Detect which kind of block activeBlock is and return an appropriate instance.
		return new Block(activeBlock);
	}
	
	/* The BlockModel associated with this instance. */
	private BlockModel model;

	/*
	 * Create a block of a single tile. This method should be overridden by subclasses.
	 */
	public Block(BlockModel blockModel) {
		model = blockModel;
	}

	/*
	 * Draw this block on the given context.
	 * 
	 * @param x The x coordinate of the position at which to draw this block.
	 * @param y The y coordinate of the position at which to draw this block.
	 * @param context2d The context on which to draw this block.
	 */
	public void draw(int x, int y, Context2d context2d) {
		Square square = new Square();
		// Get an iterator of square coordinates based around the given coordinate (x,y).
		for (Integer[] squarePos : model.getIterator(x, y)) {
			square.draw(squarePos[1], squarePos[0], context2d);
		}
	}
	
	/* 
	 * Check if a given BlockModel is the model for this Block type.
	 * 
	 * @param activeBlock The block model to compare against the the model stored in this Block.
	 * 
	 * @return True iff the given BlockModel is the same as the model of this Block instance.
	 */
	public boolean isModel(BlockModel activeBlock) {
		return model.equals(activeBlock);
	}

	/*
	 * Get a path on the given context surrounding all the squares in this block.
	 * 
	 * @param x The x coordinate of this block's position.
	 * @param y The y coordinate of this block's position.
	 * @param context2d The context on which to create this path.
	 */
	public void getPath(int x, int y, Context2d context2d) {
		Square square = new Square();
		for (Integer[] squarePos : model.getIterator(x, y)) {
			square.addSquareToCanvasPath(squarePos[1], squarePos[0], context2d);
		}
	}
}
