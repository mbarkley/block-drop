package demo.client.local;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;

import demo.client.shared.BlockOverflow;
import demo.client.shared.BoardModel;

public class BoardController implements KeyPressHandler {
	
	private BoardModel model;
	private Block activeBlock;
	
	private Timer timer;
	private int dropIncrement = 500;
	private int loopIncrement = 100;
	private int loopCounter = 0;
	
	private int rowMove = 0;
	private int colMove = 0;
	
	private BoardPage boardPage;
	
	public BoardController() {
		// Initiate BoardModel.
		model = new BoardModel();
		activeBlock = Block.getBlockInstance(model.getActiveBlock());
		
		// Create a timer to run the game loop.
		timer = new Timer() {
			@Override
			public void run() {
				update();
			}
		};
	}
	
	public void attachPage(BoardPage boardPage) {
		this.boardPage = boardPage;
	}
	
	public void update() {
		try {
			// Drop by one row every dropIncrement milliseconds.
			int rowMove = loopCounter == dropIncrement ? 1 : 0;
			int colMove = this.colMove;
			
			// If we are not still using the same active block, make a new one.
			if (!activeBlock.isModel(model.getActiveBlock())) {
				activeBlock = Block.getBlockInstance(model.getActiveBlock());
			} 
			
			// Check if the block is moving this cycle, and then move it.
			if (rowMove != 0 || colMove != 0) {
				boardPage.undrawBlock(
					BoardController.indexToCoordinate(model.getActiveBlockRow()),
					BoardController.indexToCoordinate(model.getActiveBlockCol()),
					activeBlock
				);
				
				// Attempt to move model.
				boolean moved = model.moveActiveBlock(rowMove, colMove);
				// If that didn't work, ignore the colMove (so that the block may still drop).
				if (!moved)
					moved = model.moveActiveBlock(rowMove, 0);
				
				// Redraw block in (possibly) new position.	
				boardPage.drawBlock(
					BoardController.indexToCoordinate(model.getActiveBlockRow()),
					BoardController.indexToCoordinate(model.getActiveBlockCol()),
					activeBlock
				);
				System.out.println("draw called.");
				
				// If the block could not drop, start a new block.
				if (!moved && rowMove == 1)
					model.initNextBlock();
			}
			
			// Reset for next loop.
			this.colMove = 0;
			this.rowMove = 0;
			loopCounter = loopCounter == dropIncrement ? 0 : loopCounter + loopIncrement;
		} catch (BlockOverflow e) {
			System.out.println("Game Over.");
			timer.cancel();
		}
	}

	public void startGame() {
		boardPage.addHandlerToCanvas(this);
		// Start game loop.
		timer.scheduleRepeating(loopIncrement);		
	}
	
	@Override
	public void onKeyPress(KeyPressEvent event) {
		int keyCode = event.getNativeEvent().getKeyCode();
		switch(keyCode) {
		case KeyCodes.KEY_LEFT:
			System.out.println("Left key pressed.");
			colMove = -1;
			break;
		case KeyCodes.KEY_RIGHT:
			System.out.println("Right key pressed.");
			colMove = 1;
			break;
		}
		// Stop event from bubbling up to prevent scrolling while playing game.
		switch(keyCode) {
			case KeyCodes.KEY_LEFT:
			case KeyCodes.KEY_RIGHT:
			case KeyCodes.KEY_UP:
			case KeyCodes.KEY_DOWN:
				event.preventDefault();
				event.stopPropagation();
				break;
			default:
				break;
		}
	}

	public static int indexToCoordinate(Integer index) {
		return index * Block.SIZE;
	}
}
