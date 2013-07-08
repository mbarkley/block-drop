package demo.client.local.game;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.api.Assert;
import org.jboss.errai.ui.client.widget.ListWidget;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageHidden;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.TransitionTo;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import demo.client.local.game.Size.SizeCategory;
import demo.client.local.lobby.Client;
import demo.client.local.lobby.Lobby;
import demo.client.shared.Command;
import demo.client.shared.ExitMessage;
import demo.client.shared.Player;
import demo.client.shared.ScoreTracker;

/*
 * An Errai Navigation Page providing the UI for a Block Drop game.
 */
@Page
@Templated("Board.html")
public class BoardPage extends Composite implements ControllableBoardDisplay {

  public static final String CANVAS_WRAPPER_ID = "mainCanvas-wrapper";

  /* A mainCanvas for drawing a Block Drop game. */
  @DataField("canvas")
  private Canvas mainCanvas = Canvas.createIfSupported();
  private BoardCanvas canvasWrapper;
  /* A mainCanvas for drawing the next piece in the Block Drop game. */
  @DataField("next-piece")
  private Canvas nextPieceCanvas = Canvas.createIfSupported();
  @DataField("opp-canvas")
  private Canvas oppCanvas = Canvas.createIfSupported();
  private BoardCanvas oppCanvasWrapper;

  @Inject
  @DataField("score-list")
  private ListWidget<ScoreTracker, ScorePanel> scoreDisplay;

  @Inject
  private TransitionTo<Lobby> lobbyTransition;

  /* A secondaryController for this view. */
  private SecondaryDisplayController secondaryController;
  private BoardController controller;
  private BoardCallback boardCallback;

  @Inject
  private RequestDispatcher dispatcher;
  @Inject
  private MessageBus messageBus;

  /*
   * Create a BoardPage for displaying a Block Drop game.
   */
  public BoardPage() {
    System.out.println("Initiating BoardModel");

    // Initialize canvases.
    mainCanvas.setCoordinateSpaceHeight(Size.MAIN_COORD_HEIGHT);
    mainCanvas.setCoordinateSpaceWidth(Size.MAIN_COORD_WIDTH);
    nextPieceCanvas.setCoordinateSpaceHeight(Size.NEXT_COORD_HEIGHT);
    nextPieceCanvas.setCoordinateSpaceWidth(Size.NEXT_COORD_WIDTH);
    oppCanvas.setCoordinateSpaceHeight(Size.OPP_COORD_HEIGHT);
    oppCanvas.setCoordinateSpaceWidth(Size.OPP_COORD_WIDTH);

    canvasWrapper = new BoardCanvas(mainCanvas, SizeCategory.MAIN);
    oppCanvasWrapper = new BoardCanvas(oppCanvas, SizeCategory.OPPONENT);
  }

  /*
   * Perform additional setup for the Board UI after this object has been constructed.
   */
  @PostConstruct
  private void setup() {
    secondaryController = new SecondaryDisplayControllerImpl(scoreDisplay, nextPieceCanvas);
    controller = new BoardController(this, secondaryController, new BoardMessageBusImpl());
    boardCallback = new BoardCallback(controller, secondaryController);
    // Subscribe to game channel
    messageBus.subscribe("Game" + Client.getInstance().getGameRoom().getId(), boardCallback);

    // Check that mainCanvas was supported.
    if (mainCanvas != null) {
      System.out.println("Canvas successfully created.");
    }
    else {
      // TODO: Display message to user that HTML5 Canvas is required.
    }
  }

  @PageShowing
  private void start() {
    try {
      EventHandler handler = new BoardKeyHandler(controller);
      addHandlerToMainCanvas((KeyUpHandler) handler, KeyUpEvent.getType());
      addHandlerToMainCanvas((KeyDownHandler) handler, KeyDownEvent.getType());
      controller.startGame();

      messageBus.subscribe("Game" + Client.getInstance().getGameRoom().getId(), new OppCallback(oppCanvasWrapper));
    } catch (NullPointerException e) {
      e.printStackTrace();
      // Null pointer likely means the user needs to register a Player object in the lobby.
      lobbyTransition.go();
    }
  }

  @PageHidden
  private void leaveGame() {
    ExitMessage exitMessage = new ExitMessage();
    exitMessage.setPlayer(Client.getInstance().getPlayer());
    exitMessage.setGame(Client.getInstance().getGameRoom());
    Client.getInstance().setGameRoom(null);
    MessageBuilder.createMessage("Relay").command(Command.LEAVE_GAME).withValue(exitMessage).noErrorHandling()
            .sendNowWith(dispatcher);
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
  /*
   * (non-Javadoc)
   * 
   * @see demo.client.local.game.ControllableBoardDisplay#undrawBlock(int, int,
   * demo.client.local.game.Block)
   */
  @Override
  public void undrawBlock(int x, int y, Block activeBlock) {
    canvasWrapper.undrawBlock(x, y, activeBlock);
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
  /*
   * (non-Javadoc)
   * 
   * @see demo.client.local.game.ControllableBoardDisplay#drawBlock(int, int,
   * demo.client.local.game.Block)
   */
  @Override
  public void drawBlock(int x, int y, Block activeBlock) {
    canvasWrapper.drawBlock(x, y, activeBlock);
  }

  /*
   * Add a key press handler to this page's mainCanvas.
   * 
   * @param handler A key press handler for the mainCanvas.
   */
  <H extends EventHandler> void addHandlerToMainCanvas(H handler, Type<H> type) {
    Assert.notNull("Could not get game-wrapper root panel.", RootPanel.get()).addDomHandler(handler, type);
  }

  /*
   * (non-Javadoc)
   * 
   * @see demo.client.local.game.ControllableBoardDisplay#pause()
   */
  @Override
  public void pause() {
    DivElement element = ((DivElement) Document.get().getElementById("pause-overlay"));
    element.setAttribute("style", "visibility: visible");
  }

  /*
   * (non-Javadoc)
   * 
   * @see demo.client.local.game.ControllableBoardDisplay#unpause()
   */
  @Override
  public void unpause() {
    DivElement element = ((DivElement) Document.get().getElementById("pause-overlay"));
    element.removeAttribute("style");
  }

  @Override
  public void clearBoard() {
    canvasWrapper.clearBoard();
  }

  @Override
  public SizeCategory getSizeCategory() {
    return canvasWrapper.getSizeCategory();
  }
}
