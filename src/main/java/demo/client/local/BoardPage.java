package demo.client.local;

import javax.annotation.PostConstruct;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.user.client.ui.Composite;

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
	
	public static int indexToCoordinate(Integer index) {
		return index * Block.SIZE;
	}
	
	@DataField
	private Canvas canvas = Canvas.createIfSupported();;
	
	private BoardController controller;
	
	public BoardPage() {
		System.out.println("Initiating BoardModel");
		canvas.setCoordinateSpaceHeight(COORD_HEIGHT);
		canvas.setCoordinateSpaceWidth(COORD_WIDTH);
		controller = new BoardController();
	}
	
	@PostConstruct
	private void constructUI() {
		// Check that canvas was supported.
		if (canvas != null) {
			System.out.println("Canvas successfully created.");
			controller.attachPage(this);
			
			controller.startGame();

		} else {
			// TODO: Display message to user that HTML5 Canvas is required.
		}
	}

	/* For now, give the background a solid colour. Path must already be set. */
	private void drawBackground() {
		canvas.getContext2d().setFillStyle(BOARD_COLOR);
		canvas.getContext2d().fill();
	}

	public void undrawBlock(int x, int y, Block activeBlock) {
		canvas.getContext2d().beginPath();
		activeBlock.getPath(x, y, canvas.getContext2d());
		canvas.getContext2d().closePath();
		drawBackground();		
	}

	public void drawBlock(int x, int y, Block activeBlock) {
		activeBlock.draw(x, y, canvas.getContext2d());
	}
}
