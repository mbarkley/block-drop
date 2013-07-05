package demo.client.local.game;

import com.google.gwt.user.client.Timer;

import demo.client.shared.model.BackgroundBlockModel;
import demo.client.shared.model.BlockOverflow;
import demo.client.shared.model.BoardModel;

public class BoardController {

  private ControllableBoardDisplay boardDisplay;
  private SecondaryDisplayController secondaryController;
  private BoardMessageBus messageBus;

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

  /* True if the active block should rotate this loop iteration. */
  private boolean[] rotate = new boolean[3];
  /* True if the game is paused. */
  private boolean pause = false;
  private boolean[] moveTrack = new boolean[3];

  private ClearState clearState = ClearState.START;
  private Block toBeCleared;
  private Block bgBlock;

  public BoardController(ControllableBoardDisplay boardDisplay, SecondaryDisplayController secondaryController,
          BoardMessageBus messageBus) {
    this.boardDisplay = boardDisplay;
    this.secondaryController = secondaryController;
    this.messageBus = messageBus;

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
   * Start a game of Block Drop.
   */
  public void startGame() {
    timer.scheduleRepeating(loopTime);
  }

  /*
   * Handle user input, and update the game state and view. This method is is called every loopTime
   * milliseconds.
   */
  void update() {
    if (pause) {
      boardDisplay.pause();
      return;
    }
    else {
      boardDisplay.unpause();
    }

    boolean moved = false;

    // Check for rows to clear. Rows will stay in model until fully dealt with.
    int numFullRows = model.numFullRows();
    if (numFullRows > 0) {
      clearRows(numFullRows);
    }
    else if (model.getRowsToAdd() > 0) {
      addRowsToBottom();
    }
    // Only drop a new block if we are not clearing rows currently.
    else {
      // Reset the active block if necessary.
      if (!activeBlock.isModel(model.getActiveBlock())) {
        activeBlock = Block.getBlockInstance(model.getActiveBlock());
        nextBlock = Block.getBlockInstance(model.getNextBlock());
        secondaryController.drawBlockToNextCanvas(nextBlock);
      }
      // Update the position of the active block and record movement.
      moved = activeBlockUpdate();

      try {
        // If the block could not drop, start a new block.
        if (!moved && model.getPendingRowMove() > 0) {
          model.initNextBlock();
        }
      } catch (BlockOverflow e) {
        // TODO: Handle game ending.
        System.out.println("Game Over.");

        timer.cancel();
      }
      // Reset for next loop.
      model.setDrop(false);
      if (rotate())
        incrementRotate();
      if (horizontalMove())
        incrementMovePacer();
      model.setPendingRowMove(0);
      loopCounter = loopCounter == dropIncrement ? 0 : loopCounter + 1;
    }

  }

  private void addRowsToBottom() {
    // This method should always be called when there are no full rows.
    BackgroundBlockModel bgModel = model.getNonFullRows();
    Block bg = new Block(bgModel);
    boardDisplay.undrawBlock(0, 0, bg);
    boardDisplay.undrawBlock(Block.indexToCoord(model.getActiveBlockCol()),
            Block.indexToCoord(model.getActiveBlockRow()), activeBlock);
    model.addRows();
    bgModel = model.getNonFullRows();
    bg = new Block(bgModel);
    boardDisplay.drawBlock(0, 0, bg);
    boardDisplay.drawBlock(Block.indexToCoord(model.getActiveBlockCol()),
            Block.indexToCoord(model.getActiveBlockRow()), activeBlock);
  }

  private void clearRows(int numFullRows) {
    if (clearState.getCounter() == 0)
      switch (clearState) {
      case START:
        // Get blocks to be cleared.
        toBeCleared = new Block(model.getFullRows());
        bgBlock = new Block(model.getNonFullRows());
        break;
      case FIRST_UNDRAW:
      case SECOND_UNDRAW:
      case THIRD_UNDRAW:
      case LAST_UNDRAW:
        boardDisplay.undrawBlock(0, 0, toBeCleared);
        break;
      case FIRST_REDRAW:
      case SECOND_REDRAW:
      case THIRD_REDRAW:
        boardDisplay.drawBlock(0, 0, toBeCleared);
        break;
      case DROPPING:
        boardDisplay.undrawBlock(0, 0, bgBlock);
        model.clearFullRows();
        bgBlock = new Block(model.getNonFullRows());
        // Redraw background blocks that were above cleared rows.
        boardDisplay.drawBlock(0, 0, bgBlock);
        // Update the score.
        secondaryController.updateScore(numFullRows);
        messageBus.sendScoreUpdate(secondaryController.getScoreTracker(), secondaryController.getTarget());
        break;
      }
    clearState = clearState.getNextState();
  }

  /*
   * Update the active block.
   * 
   * @return True iff the active block moved during this call.
   */
  private boolean activeBlockUpdate() {
    boardDisplay.undrawBlock(Block.indexToCoord(model.getActiveBlockCol()),
            Block.indexToCoord(model.getActiveBlockRow()), activeBlock);

    // If the user wishes to drop the block, do nothing else.
    if (model.isDropping()) {
      setColMove(0);
      model.setPendingRowMove(model.getDropDistance());
    }
    else {
      if (rotate()) {
        model.rotateActiveBlock();
      }
      // Check if the user wants to increase the speed at which the block drops
      if (model.isFast()) {
        model.setPendingRowMove(1);
        // Otherwise maintain the normal speed.
      }
      else {
        // Drop by one row every time counter hits dropIncrement.
        model.setPendingRowMove(loopCounter == dropIncrement ? 1 : 0);
      }
    }

    // Attempt to move model.
    boolean moved = horizontalMove() ? model.moveActiveBlock(true) : model.moveActiveBlock(false);
    // If that didn't work, ignore the colMove (so that the block may still drop).
    if (!moved && horizontalMove())
      moved = model.moveActiveBlock(false);

    // Redraw block in (possibly) new position.
    boardDisplay.drawBlock(Block.indexToCoord(model.getActiveBlockCol()),
            Block.indexToCoord(model.getActiveBlockRow()), activeBlock);

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

  void clearRotation() {
    clearHelper(rotate);
  }

  private void clearHelper(boolean[] track) {
    for (int i = 0; i < track.length; i++) {
      track[i] = false;
    }
  }

  private void incrementMovePacer() {
    incrementHelper(moveTrack);
  }

  void incrementRotate() {
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

  private void clearMovePacer() {
    clearHelper(moveTrack);
  }

  void setColMove(int i) {
    model.setPendingColMove(i);
    if (i != 0)
      incrementMovePacer();
    else
      clearMovePacer();
  }

  boolean isPaused() {
    return pause;
  }

  void setDrop(boolean b) {
    model.setDrop(b);
  }

  void setPaused(boolean b) {
    pause = b;
  }

  void setFast(boolean fast) {
    model.setFast(fast);
  }

  public SecondaryDisplayController getSecondaryController() {
    return secondaryController;
  }

  public void addRows(int rowsClearedLast) {
    model.setRowsToAdd(rowsClearedLast);
  }
}
