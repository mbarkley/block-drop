package demo.client.local;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Timer;

import demo.client.shared.BlockModel;
import demo.client.shared.BlockOverflow;
import demo.client.shared.BoardModel;
import demo.client.shared.RowsFullException;

/*
 * A controller class for a Block Drop game. Handles game loop and user input.
 */
public class BoardController implements KeyPressHandler {
	
	private static final int KEY_SPACE_BAR = 32;
	/* A Block Drop board model. */
	private BoardModel model;
	/* A block model. */
	private Block activeBlock;
	
	/* A timer for running the game loop. */
	private Timer timer;
	/* The number (in milliseconds) of time for a block to drop one square on the board. */
	private int dropIncrement = 500;
	/* The time (in milliseconds) between calls to the game loop. */
	private int loopIncrement = 100;
	/* A counter of ellapsed time (in milliseconds) since a block last dropped. */
	private int loopCounter = 0;
	
	/* 
	 * The amount of rows (positive is down) the active block on the board
	 * should move this loop iteration.
	 */
	private int rowMove = 0;
	/*
	 * The amount of columns (positive is right) the active block on the board
	 * should move this loop iteration.
	 */
	private int colMove = 0;
	/* True if the active block should rotate this loop iteration. */
	private boolean rotate;
	/* True if the active block should drop to the bottom of the screen. */
	private boolean drop;
	
	/* The BoardPage on which this Block Drop game is displayed. */
	private BoardPage boardPage;
	
	/*
	 * Create a BoardController instance.
	 */
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
	
	/*
	 * Set the page which this BoardController controls.
	 * 
	 * @param boardPage The BoardPage to be controlled.
	 */
	public void setPage(BoardPage boardPage) {
		this.boardPage = boardPage;
	}
	
	/*
	 * Handle user input, and update the game state and view. This method is
	 * is called every loopIncrement milliseconds.
	 */
	public void update() {
		try {
			int rowMove;
			int colMove;
			
			// If the user wishes to drop the block, do nothing else.
			if (drop) {
				colMove = 0;
				rowMove = model.getDrop();
				rotate = false;
			} else {
				// Drop by one row every dropIncrement milliseconds.
				rowMove = loopCounter == dropIncrement ? 1 : 0;
				colMove = this.colMove;
			}
			
			boardPage.undrawBlock(
				Block.indexToCoord(model.getActiveBlockCol()),
				Block.indexToCoord(model.getActiveBlockRow()),
				activeBlock
			);
			
			// Rotate the block model if the user hit rotate.
			if (rotate) {
				model.rotateActiveBlock();
			}
			
			// Attempt to move model.
			boolean moved = model.moveActiveBlock(rowMove, colMove);
			// If that didn't work, ignore the colMove (so that the block may still drop).
			if (!moved)
				moved = model.moveActiveBlock(rowMove, 0);
			
			// Redraw block in (possibly) new position.	
			boardPage.drawBlock(
				Block.indexToCoord(model.getActiveBlockCol()),
				Block.indexToCoord(model.getActiveBlockRow()),
				activeBlock
			);
			
			// If the block could not drop, start a new block.
			if (!moved && rowMove > 0) {
				model.initNextBlock();
			}
		} catch (BlockOverflow e) {
			// TODO: Handle game ending.
			System.out.println("Game Over.");
			
			// Redraw last block.
			boardPage.drawBlock(
					Block.indexToCoord(model.getActiveBlockCol()),
					Block.indexToCoord(model.getActiveBlockRow()),
					activeBlock
			);
			
			timer.cancel();
		} catch (RowsFullException e) {
			// Check how many rows need to be cleared.
			int numRows = e.getNumFullRows();
			
			BlockModel bgBlockModel = model.getBackgroundBlock();
		} finally {
			// Reset for next loop.
			drop = false;
			rotate = false;
			this.colMove = 0;
			this.rowMove = 0;
			loopCounter = loopCounter == dropIncrement ? 0 : loopCounter + loopIncrement;
			if (!activeBlock.isModel(model.getActiveBlock()))
				activeBlock = Block.getBlockInstance(model.getActiveBlock());
		}
	}

	/*
	 * Start a game of Block Drop.
	 */
	public void startGame() {
		// Add this as a handler for keyboard events.
		boardPage.addHandlerToCanvas(this);
		// Start game loop.
		timer.scheduleRepeating(loopIncrement);		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyPressHandler#onKeyPress(com.google.gwt.event.dom.client.KeyPressEvent)
	 */
	@Override
	public void onKeyPress(KeyPressEvent event) {

		/*
		 * Some key presses return char codes whereas others return key codes.
		 */
		int keyCode = event.getCharCode() == 0 ? event.getNativeEvent().getKeyCode() : event.getCharCode();
		
		/*
		 *  If the user pressed a key used by this game, stop the event from
		 *  bubbling up to prevent scrolling or other undesrible events.
		 */
		if (keyPressHelper(keyCode)) {
			event.stopPropagation();
			event.preventDefault();
		}
	}
	
	/*
	 * Alter the current state based on user input.
	 */
	private boolean keyPressHelper(int keyCode) {
	// First handle relevant key presses.
		boolean relevantKey = false;
		switch(keyCode) {
			case KeyCodes.KEY_LEFT:
				System.out.println("Left key pressed.");
				colMove = -1;
				relevantKey = true;
				break;
			case KeyCodes.KEY_RIGHT:
				System.out.println("Right key pressed.");
				colMove = 1;
				relevantKey = true;
				break;
			case KeyCodes.KEY_UP:
				System.out.println("Up key pressed.");
				rotate = true;
				relevantKey = true;
				break;
			case KEY_SPACE_BAR:
				System.out.println("Space bar pressed.");
				drop = true;
				relevantKey = true;
				break;
			default:
				System.out.println("Key code pressed: "+keyCode);
				break;
		}
		
		return relevantKey;
	}
}
