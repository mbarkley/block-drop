package demo.client.local;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.user.client.ui.Composite;

import demo.client.shared.BlockModel;

public class Block {

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

	private void makeOuterRectPath(Context2d context2d, Integer[] squarePos) {
		context2d.beginPath();
		
		context2d.rect(
			indexToCoordinate(squarePos[1]),
			indexToCoordinate(squarePos[0]),
			BoardPage.SIZE,
			BoardPage.SIZE
		);
		
		context2d.closePath();
	}
	
	private void makeInnerRectPath(Context2d context2d, Integer[] squarePos) {
		context2d.beginPath();
		
		context2d.rect(
			indexToCoordinate(squarePos[1])+1,
			indexToCoordinate(squarePos[0])+1,
			BoardPage.SIZE-2,
			BoardPage.SIZE-2
		);
		
		context2d.closePath();
	}
	
	public void draw(Context2d context2d) {
		for (Integer[] squarePos : model) {
			context2d.save();
			
			makeOuterRectPath(context2d, squarePos);
			context2d.setFillStyle("black");
			context2d.fill();
			
			makeInnerRectPath(context2d, squarePos);
			context2d.setFillStyle("red");
			context2d.fill();
			
			context2d.restore();
		}
	}
	
	public void undraw(Context2d context2d) {
		for (Integer[] squarePos : model) {
			context2d.save();
			
			makeOuterRectPath(context2d, squarePos);
			context2d.setFillStyle(BoardPage.BOARD_COLOR);
			context2d.fill();
			
			context2d.restore();
		}
	}
	
	private static double indexToCoordinate(Integer index) {
		return new Double(index * BoardPage.SIZE);
	}

	/* Check if a given BlockModel is the model for this Block type. */
	public boolean isModel(BlockModel activeBlock) {
		return model.equals(activeBlock);
	}
}
