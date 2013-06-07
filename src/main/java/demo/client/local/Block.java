package demo.client.local;

import com.google.gwt.canvas.dom.client.Context2d;

import demo.client.shared.BlockModel;

public class Block {
	
	public static final int SIZE = 60;

	public static Block getBlockInstance(BlockModel activeBlock) {
		// TODO: Detect which kind of block activeBlock is and return an appropriate instance.
		return new Block(activeBlock);
	}
	
	private BlockModel model;

	/*
	 * Create block of single tile.
	 */
	public Block(BlockModel blockModel) {
		model = blockModel;
	}

	public void draw(int x, int y, Context2d context2d) {
		Square square = new Square();
		for (Integer[] squarePos : model.getIterator(x, y)) {
			square.draw(squarePos[1], squarePos[0], context2d);
		}
	}
	
	/* Check if a given BlockModel is the model for this Block type. */
	public boolean isModel(BlockModel activeBlock) {
		return model.equals(activeBlock);
	}

	public void getPath(int x, int y, Context2d context2d) {
		Square square = new Square();
		for (Integer[] squarePos : model.getIterator(x, y)) {
			square.addSquareToCanvasPath(squarePos[1], squarePos[0], context2d);
		}
	}
}
