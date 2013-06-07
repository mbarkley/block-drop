package demo.client.local;

import org.jboss.errai.ui.shared.api.annotations.EventHandler;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.Timer;

import demo.client.shared.BlockOverflow;
import demo.client.shared.BoardModel;

public class BoardController {
	
	private BoardModel model;
	private Block activeBlock;
	
	private Timer timer;
	private int timeIncrement = 500;
	
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
		System.out.println("update called.");
		try {
			// If we are not still using the same active block, make a new one.
			if (!activeBlock.isModel(model.getActiveBlock())) {
				activeBlock = Block.getBlockInstance(model.getActiveBlock());
			} else {
				boardPage.undrawBlock(
					BoardPage.indexToCoordinate(model.getActiveBlockRow()),
					BoardPage.indexToCoordinate(model.getActiveBlockCol()),
					activeBlock
				);
			}
			boolean moved = model.lowerActiveBlock();
			
			// Redraw block in (possibly) new position)		
			boardPage.drawBlock(
				BoardPage.indexToCoordinate(model.getActiveBlockRow()),
				BoardPage.indexToCoordinate(model.getActiveBlockCol()),
				activeBlock
			);
			System.out.println("draw called.");
			
			// If nothing was moved, start a new active block.
			if (!moved) {
				model.initNextBlock();
			}
		} catch (BlockOverflow e) {
			System.out.println("Game is over.");
			timer.cancel();
		}
	}
	
	@EventHandler("canvas")
	public void handleKeyPress(KeyPressEvent event) {
		switch(event.getNativeEvent().getKeyCode()) {
			case KeyCodes.KEY_LEFT:
				System.out.println("Left key pressed.");
			case KeyCodes.KEY_RIGHT:
				System.out.println("Right key pressed.");
			default:
				// Stop event from bubbling up to prevent scrolling while playing game.
				event.preventDefault();
				event.stopPropagation();
				break;
		}
	}

	public void startGame() {
		// Start game loop.
		timer.scheduleRepeating(timeIncrement);		
	}
}
