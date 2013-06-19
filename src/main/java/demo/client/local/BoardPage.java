package demo.client.local;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;

import demo.client.shared.ScoreTracker;

/*
 * An Errai Navigation Page providing the UI for a Block Drop game.
 */
@Page(role = DefaultPage.class)
@Templated("Board.html")
public class BoardPage extends Composite {

  public static final String CANVAS_WRAPPER_ID = "mainCanvas-wrapper";

  /* The height of the board in squares. */
  public static final int HEIGHT = 15;
  /* The width of the board in squares. */
  public static final int WIDTH = 10;
  /* Canvas coordinate-space height (in pixels). */
  public static final int MAIN_COORD_HEIGHT = 900;
  /* Canvas coordinate-space width (in pixels). */
  public static final int MAIN_COORD_WIDTH = 600;
  /* The dimension of each square in pixels. */
  public static final int SIZE = 60;
  /* The background colour of the Block Drop board. */
  public static final String BOARD_COLOUR = "rgb(255,255,255)";

  public static final int NEXT_COORD_HEIGHT = 300;

  private static final int NEXT_COORD_WIDTH = 300;

  /* A mainCanvas for drawing a Block Drop game. */
  @DataField("canvas")
  private Canvas mainCanvas = Canvas.createIfSupported();
  /* A mainCanvas for drawing the next piece in the Block Drop game. */
  @DataField("next-piece")
  private Canvas nextPieceCanvas = Canvas.createIfSupported();
  @DataField("score-list")
  @Inject
  private ListWidget<ScoreTracker, ScorePanel> scoreDisplay;
  private ScorePanel panel;

  public ScoreTracker getScoreModel() {
    return panel.getModel();
  }

  /* A controller for this view. */
  private BoardController controller;

  /*
   * Create a BoardPage for displaying a Block Drop game.
   */
  public BoardPage() {
    System.out.println("Initiating BoardModel");

    // Initialize canvases.
    mainCanvas.setCoordinateSpaceHeight(MAIN_COORD_HEIGHT);
    mainCanvas.setCoordinateSpaceWidth(MAIN_COORD_WIDTH);
    nextPieceCanvas.setCoordinateSpaceHeight(NEXT_COORD_HEIGHT);
    nextPieceCanvas.setCoordinateSpaceWidth(NEXT_COORD_WIDTH);

    // Initialize controller.
    controller = new BoardController();
  }

  /*
   * Perform additional setup for the Board UI after this object has been constructed.
   */
  @PostConstruct
  private void constructUI() {
    // Check that mainCanvas was supported.
    if (mainCanvas != null) {
      System.out.println("Canvas successfully created.");
      controller.setPage(this);

      controller.startGame();
    }
    else {
      // TODO: Display message to user that HTML5 Canvas is required.
    }
  }

  public void initScoreList(List<ScoreTracker> scoreList) {
    scoreDisplay.setItems(scoreList);
  }

  /*
   * Fill the current path on this page's mainCanvas with the board background colour.
   */
  private void drawBackground() {
    mainCanvas.getContext2d().setFillStyle(BOARD_COLOUR);
    mainCanvas.getContext2d().fill();
  }

  /*
   * Undraw the given block from this page's mainCanvas. Note: Any path on the mainCanvas will be
   * lost after invoking this method.
   * 
   * @param x The x coordinate of the position of the block.
   * 
   * @param y The y coordinate of the position of the block.
   * 
   * @param activeBlock The block to undraw.
   */
  public void undrawBlock(int x, int y, Block activeBlock) {
    mainCanvas.getContext2d().beginPath();
    activeBlock.getPath(x, y, mainCanvas.getContext2d());
    mainCanvas.getContext2d().closePath();
    drawBackground();
  }

  /*
   * Draw a block on this page's mainCanvas.
   * 
   * @param x The x coordinate of the position of the block.
   * 
   * @param y The y coordinate of the position of the block.
   * 
   * @param activeBlock The block to draw.
   */
  public void drawBlock(int x, int y, Block activeBlock) {
    activeBlock.draw(x, y, mainCanvas.getContext2d());
  }

  /*
   * Add a key press handler to this page's mainCanvas.
   * 
   * @param handler A key press handler for the mainCanvas.
   */
  public void addHandlerToMainCanvas(KeyPressHandler handler) {
    mainCanvas.addKeyPressHandler(handler);
  }

  public void drawBlockToNextCanvas(Block nextBlock) {
    // Clear everything.
    nextPieceCanvas.getContext2d().setFillStyle("lightgrey");
    nextPieceCanvas.getContext2d().fillRect(0, 0, NEXT_COORD_WIDTH, MAIN_COORD_HEIGHT);

    // Draw title.
    nextPieceCanvas.getContext2d().setFillStyle("black");
    nextPieceCanvas.getContext2d().setFont("bold 20px sans-serif");
    nextPieceCanvas.getContext2d().fillText("Next Block", 10, 20);

    nextBlock.draw(2 * Block.SIZE + nextBlock.getCentreColDiff() * Block.SIZE,
            2 * Block.SIZE + nextBlock.getCentreRowDiff() * Block.SIZE, nextPieceCanvas.getContext2d());
  }

  public List<ScoreTracker> getScoreList() {
    return scoreDisplay.getValue();
  }
}
