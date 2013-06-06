package demo.client.local;

import javax.annotation.PostConstruct;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;

import demo.client.shared.BlockOverflow;
import demo.client.shared.BoardModel;

/*
 * An Errai Navigation Page providing the UI for a tic-tac-toe game.
 */
@Page(role=DefaultPage.class)
@Templated("Board.html")
public class BoardPage extends Composite {
	
	public static final String CANVAS_WRAPPER_ID = "canvas-wrapper";

	/* The height of the board in squares. */
	public static final int HEIGHT = 15;
	/* The width of the board in squares. */
	public static final int WIDTH = 10;
	/* Canvas coordinate-space height (in pixels). */
	public static final int COORD_HEIGHT = 900;
	/* Canvas coordinate-space width (in pixels). */
	public static final int COORD_WIDTH = 600;
	/* The dimension of each square in pixels. */
	public static final int SIZE = 60;
	
	public static final String BOARD_COLOR = "rgb(255,255,255)";
	
	private BoardModel model;
	private Block activeBlock;
	private int timeIncrement = 500;
	
	@DataField
	private Canvas canvas = Canvas.createIfSupported();;
	private Timer timer;
	
	public BoardPage() {
		System.out.println("Initiating BoardModel");
		// Initiate BoardModel.
		model = new BoardModel();
		activeBlock = Block.getBlockInstance(model.getActiveBlock());
		canvas.setCoordinateSpaceHeight(COORD_HEIGHT);
		canvas.setCoordinateSpaceWidth(COORD_WIDTH);
	}
	
	@PostConstruct
	private void constructUI() {
		if (canvas != null) {
			System.out.println("Canvas successfully created.");
			timer = new Timer() {
				@Override
				public void run() {
					update();
				}
			};
			timer.scheduleRepeating(timeIncrement);
		}
	}

	public void update() {
		System.out.println("update called.");
		try {
			// If we are not still using the same active block, make a new one.
			if (!activeBlock.isModel(model.getActiveBlock())) {
				activeBlock = Block.getBlockInstance(model.getActiveBlock());
			} else {
				activeBlock.undraw(canvas.getContext2d());
				System.out.println("undraw called.");
			}
				
				model.incrementBoard();
				System.out.println("incrementBoard called.");
				
				activeBlock.draw(canvas.getContext2d());
				System.out.println("draw called.");
		} catch (BlockOverflow e) {
			System.out.println("Game is over.");
			timer.cancel();
		}
	}
}
