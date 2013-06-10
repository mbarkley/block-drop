package demo.client.local;

import javax.annotation.PostConstruct;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.Composite;

/*
 * An Errai Navigation Page providing the UI for a Block Drop game.
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
	/* The background colour of the Block Drop board. */
	public static final String BOARD_COLOUR = "rgb(255,255,255)";
	
	/* A canvas for drawing a Block Drop game. */
	@DataField
	private Canvas canvas = Canvas.createIfSupported();;
	/* A controller for this view. */
	private BoardController controller;
	
	/*
	 * Create a BoardPage for displaying a Block Drop game.
	 */
	public BoardPage() {
		System.out.println("Initiating BoardModel");
		canvas.setCoordinateSpaceHeight(COORD_HEIGHT);
		canvas.setCoordinateSpaceWidth(COORD_WIDTH);
		controller = new BoardController();
	}
	
	/*
	 * Perform additional setup for the Board UI after this object has been constructed.
	 */
	@PostConstruct
	private void constructUI() {
		// Check that canvas was supported.
		if (canvas != null) {
			System.out.println("Canvas successfully created.");
			controller.setPage(this);
			
			controller.startGame();
		} else {
			// TODO: Display message to user that HTML5 Canvas is required.
		}
	}

	/*
	 * Fill the current path on this page's canvas with the board background colour.
	 */
	private void drawBackground() {
		canvas.getContext2d().setFillStyle(BOARD_COLOUR);
		canvas.getContext2d().fill();
	}

	/*
	 * Undraw the given block from this page's canvas.
	 * Note: Any path on the canvas will be lost after invoking this method.
	 * 
	 * @param x The x coordinate of the position of the block.
	 * @param y The y coordinate of the position of the block.
	 * @param activeBlock The block to undraw.
	 */
	public void undrawBlock(int x, int y, Block activeBlock) {
		canvas.getContext2d().beginPath();
		activeBlock.getPath(x, y, canvas.getContext2d());
		canvas.getContext2d().closePath();
		drawBackground();		
	}

	/*
	 * Draw a block on this page's canvas.
	 * 
	 * @param x The x coordinate of the position of the block.
	 * @param y The y coordinate of the position of the block.
	 * @param activeBlock The block to draw.
	 */
	public void drawBlock(int x, int y, Block activeBlock) {
		activeBlock.draw(x, y, canvas.getContext2d());
	}

	/*
	 * Add a key press handler to this page's canvas.
	 * 
	 * @param handler A key press handler for the canvas.
	 */
	public void addHandlerToCanvas(KeyPressHandler handler) {
		canvas.addKeyPressHandler(handler);
	}
}
