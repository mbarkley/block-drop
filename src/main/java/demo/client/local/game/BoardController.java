package demo.client.local.game;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;

import demo.client.local.lobby.Client;
import demo.client.shared.Command;
import demo.client.shared.GameRoom;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BlockOverflow;
import demo.client.shared.model.BoardModel;

/*
 * A controller class for a Block Drop game. Handles game loop and user input.
 */
class BoardController implements KeyDownHandler, KeyUpHandler {

  private static final int KEY_SPACE_BAR = 32;
  /* A Block Drop board model. */
  private BoardModel model;
  /* A block model. */
  private Block activeBlock;
  /* A block model. */
  private Block nextBlock;

  /* A timer for running the game loop. */
  private Timer timer;
  /*
   * The number of iterations for a block to drop one square on the board. Must be a multiple of 4.
   */
  private int dropIncrement = 16;
  /* The time (in milliseconds) between calls to the game loop. */
  private int loopTime = 25;
  /* A counter of elapsed iterations since a block last dropped. */
  private int loopCounter = 0;

  /*
   * The amount of rows (positive is down) the active block on the board should move this loop
   * iteration.
   */
  private int rowMove = 0;
  /*
   * The amount of columns (positive is right) the active block on the board should move this loop
   * iteration.
   */
  private int colMove = 0;
  /* True if the active block should rotate this loop iteration. */
  private boolean[] rotate = new boolean[3];
  /* True if the active block should drop to the bottom of the screen. */
  private boolean drop;
  /* True if the active block drop speed should be increased. */
  private boolean fast;
  /* True if the game is paused. */
  private boolean pause = false;
  private boolean[] moveTrack = new boolean[3];

  private ClearState clearState = ClearState.START;
  private Block toBeCleared;
  private Block bgBlock;

  /* The BoardPage on which this Block Drop game is displayed. */
  private BoardPage boardPage;
  
  @Inject
  private MessageBus messageBus = ErraiBus.get();

  /*
   * Create a BoardController instance.
   */
  BoardController() {
    // Initiate BoardModel.
    model = new BoardModel();
    activeBlock = Block.getBlockInstance(model.getActiveBlock());
    nextBlock = Block.getBlockInstance(model.getNextBlock());

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
  void setPage(BoardPage boardPage) {
    this.boardPage = boardPage;
  }

  /*
   * Handle user input, and update the game state and view. This method is is called every loopTime
   * milliseconds.
   */
  void update() {
    if (pause) {
      BoardPage.pause();
      return;
    }
    else {
      BoardPage.unpause();
    }

    boolean moved = false;

    // Check for rows to clear. Rows will stay in model until fully dealt with.
    int numFullRows = model.numFullRows();
    if (numFullRows > 0) {
      clearRows(numFullRows);
      // Only drop a new block if we are not clearing rows currently.
    }
    else {
      // Reset the active block if necessary.
      if (!activeBlock.isModel(model.getActiveBlock())) {
        activeBlock = Block.getBlockInstance(model.getActiveBlock());
        nextBlock = Block.getBlockInstance(model.getNextBlock());
        boardPage.drawBlockToNextCanvas(nextBlock);
      }
      // Update the position of the active block and record movement.
      moved = activeBlockUpdate();

      try {
        // If the block could not drop, start a new block.
        if (!moved && rowMove > 0) {
          model.initNextBlock();
        }
      } catch (BlockOverflow e) {
        // TODO: Handle game ending.
        System.out.println("Game Over.");

        timer.cancel();
      }
      // Reset for next loop.
      drop = false;
      if (rotate())
        incrementRotate();
      if (horizontalMove())
        incrementMovePacer();
      this.rowMove = 0;
      loopCounter = loopCounter == dropIncrement ? 0 : loopCounter + 1;
    }

  }

  private void clearRows(int numFullRows) {
    if (clearState.getCounter() == 0)
      switch (clearState) {
      case START:
        // Get blocks to be cleared.
        toBeCleared = new Block(model.getFullRows());
        bgBlock = new Block(model.getAboveFullRows());
        break;
      case FIRST_UNDRAW:
      case SECOND_UNDRAW:
      case THIRD_UNDRAW:
      case LAST_UNDRAW:
        boardPage.undrawBlock(0, 0, toBeCleared);
        break;
      case FIRST_REDRAW:
      case SECOND_REDRAW:
      case THIRD_REDRAW:
        boardPage.drawBlock(0, 0, toBeCleared);
        break;
      case DROPPING:
        boardPage.undrawBlock(0, 0, bgBlock);
        // Redraw background blocks that were above cleared rows lower.
        boardPage.drawBlock(0, Block.indexToCoord(numFullRows), bgBlock);
        model.clearFullRows();
        // Update the score.
        updateScore(numFullRows);
        break;
      }
    clearState = clearState.getNextState();
  }

  private void updateScore(int numFullRows) {
    ScoreTracker scoreTracker = getScoreTracker();
    scoreTracker.updateScore(numFullRows);
    boardPage.getScoreList().remove(scoreTracker);
    insertScoreInOrder(scoreTracker);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(scoreTracker).noErrorHandling()
            .sendNowWith(messageBus);
  }

  private void insertScoreInOrder(ScoreTracker scoreTracker) {
    List<ScoreTracker> scoreList = boardPage.getScoreList();
    int i;
    for (i = 0; i < scoreList.size(); i++) {
      if (scoreTracker.compareTo(scoreList.get(i)) >= 0) {
        break;
      }
    }
    
    scoreList.add(i, scoreTracker);
  }

  private ScoreTracker getScoreTracker() {
    List<ScoreTracker> trackers = boardPage.getScoreList();
    for (ScoreTracker t : trackers) {
      if (t.getPlayer().equals(Client.getInstance().getPlayer())) {
        return t;
      }
    }
    return null;
  }

  /*
   * Update the active block.
   * 
   * @return True iff the active block moved during this call.
   */
  private boolean activeBlockUpdate() {
    boardPage.undrawBlock(Block.indexToCoord(model.getActiveBlockCol()), Block.indexToCoord(model.getActiveBlockRow()),
            activeBlock);

    // If the user wishes to drop the block, do nothing else.
    if (drop) {
      colMove = 0;
      rowMove = model.getDrop();
    }
    else {
      if (rotate()) {
        model.rotateActiveBlock();
      }
      // Check if the user wants to increase the speed at which the block drops
      if (fast) {
        rowMove = 1;
        // Otherwise maintain the normal speed.
      }
      else {
        // Drop by one row every if counter hits dropIncrement.
        rowMove = loopCounter == dropIncrement ? 1 : 0;
      }
    }

    // Attempt to move model.
    boolean moved = horizontalMove() ? model.moveActiveBlock(rowMove, colMove) : model.moveActiveBlock(rowMove, 0);
    // If that didn't work, ignore the colMove (so that the block may still drop).
    if (!moved && horizontalMove())
      moved = model.moveActiveBlock(rowMove, 0);

    // Redraw block in (possibly) new position.
    boardPage.drawBlock(Block.indexToCoord(model.getActiveBlockCol()), Block.indexToCoord(model.getActiveBlockRow()),
            activeBlock);

    return moved;
  }

  private boolean horizontalMove() {
    return pacingHelper(moveTrack);
  }

  private boolean rotate() {
    return pacingHelper(rotate);
  }

  private boolean pacingHelper(boolean[] track) {
    int i;
    for (i = 0; i < track.length; i++) {
      if (!track[i]) {
        break;
      }
    }
    return i == track.length || i == 1;
  }

  private void clearRotation() {
    clearHelper(rotate);
  }

  private void clearHelper(boolean[] track) {
    for (int i = 0; i < track.length; i++) {
      track[i] = false;
    }
  }

  /*
   * Start a game of Block Drop.
   */
  void startGame() {
    // Add this as a handler for keyboard events.
    boardPage.addHandlerToMainCanvas(this, KeyUpEvent.getType());
    boardPage.addHandlerToMainCanvas(this, KeyDownEvent.getType());

    // Initiate score tracker.
    GameRoom room = Client.getInstance().getGameRoom();
    List<ScoreTracker> scoreList = boardPage.getScoreList();
    scoreList.addAll(room.getScoreTrackers().values());
    Collections.sort(scoreList);
    
    // Subscribe to game channel
    messageBus.subscribe("Game"+room.getId(), new MessageCallback() {
      
      @Override
      public void callback(Message message) {
        Command command = Command.valueOf(message.getCommandType());
        switch(command) {
        case UPDATE_SCORE:
          ScoreTracker scoreTracker = message.getValue(ScoreTracker.class);
          if (scoreTracker.getId() != Client.getInstance().getPlayer().getId()) {
            boardPage.getScoreList().remove(scoreTracker);
            insertScoreInOrder(scoreTracker);
          }
 default:
          break;
        }
      }
    });

    // Start game loop.
    timer.scheduleRepeating(loopTime);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.google.gwt.event.dom.client.KeyPressHandler#onKeyPress(com.google.gwt.event.dom.client.
   * KeyPressEvent)
   */
  @Override
  public void onKeyDown(KeyDownEvent event) {

    int keyCode = event.getNativeKeyCode();

    /*
     * If the user pressed a key used by this game, stop the event from bubbling up to prevent
     * scrolling or other undesirable events.
     */
    if (keyDownHelper(keyCode)) {
      event.stopPropagation();
      event.preventDefault();
    }
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {

    int keyCode = event.getNativeKeyCode();

    /*
     * If the user pressed a key used by this game, stop the event from bubbling up to prevent
     * scrolling or other undesirable events.
     */
    if (keyUpHelper(keyCode)) {
      event.stopPropagation();
      event.preventDefault();
    }
  }

  private boolean keyUpHelper(int keyCode) {
    boolean relevantKey = true;

    // Only arrow keys must be dealt with on key release.
    switch (keyCode) {
    case KeyCodes.KEY_LEFT:
    case KeyCodes.KEY_RIGHT:
      colMove = 0;
      clearMovePacer();
      break;
    case KeyCodes.KEY_UP:
      clearRotation();
      break;
    case KeyCodes.KEY_DOWN:
      fast = false;
      break;
    default:
      relevantKey = false;
      break;
    }

    return relevantKey;
  }

  private void clearMovePacer() {
    clearHelper(moveTrack);
  }

  /*
   * Alter the current state based on user input. Return true if the input captured a relevant
   * command.
   */
  private boolean keyDownHelper(int keyCode) {
    boolean relevantKey = true;
    // Handle pause separately.
    // Don't want to accept other input while paused.
    if (!pause) {
      switch (keyCode) {
      case KeyCodes.KEY_LEFT:
        System.out.println("Left key pressed.");
        colMove = -1;
        incrementMovePacer();
        break;
      case KeyCodes.KEY_RIGHT:
        System.out.println("Right key pressed.");
        colMove = 1;
        incrementMovePacer();
        break;
      case KeyCodes.KEY_UP:
        System.out.println("Up key pressed.");
        incrementRotate();
        break;
      case KeyCodes.KEY_DOWN:
        System.out.println("Down key pressed.");
        fast = true;
        break;
      case KEY_SPACE_BAR:
        System.out.println("Space bar pressed.");
        drop = true;
        break;
      case 80: // Ordinal of lower case p
        System.out.println("Pause pressed.");
        pause = !pause;
        break;
      default:
        System.out.println("Key code pressed: " + keyCode);
        relevantKey = false;
        break;
      }
    }
    else {
      // If paused, capture and ignore commands other than pause.
      switch (keyCode) {
      case KeyCodes.KEY_LEFT:
      case KeyCodes.KEY_RIGHT:
      case KeyCodes.KEY_UP:
      case KeyCodes.KEY_DOWN:
      case KEY_SPACE_BAR:
        break;
      case 80: // Ordinal of lower case p
        System.out.println("Pause pressed.");
        pause = !pause;
        break;
      default:
        relevantKey = false;
      }
    }

    return relevantKey;
  }

  private void incrementMovePacer() {
    incrementHelper(moveTrack);
  }

  private void incrementRotate() {
    incrementHelper(rotate);
  }

  private void incrementHelper(boolean[] tracker) {
    int i = 0;
    while (i < tracker.length && tracker[i]) {
      i++;
    }

    if (i != tracker.length) {
      tracker[i] = true;
    }
  }
}
