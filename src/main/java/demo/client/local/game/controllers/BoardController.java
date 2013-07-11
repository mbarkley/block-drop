package demo.client.local.game.controllers;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

import demo.client.local.game.gui.Block;
import demo.client.local.game.gui.BoardPage;
import demo.client.local.game.gui.ControllableBoardDisplay;
import demo.client.local.game.tools.BoardMessageBus;
import demo.client.local.game.tools.ClearState;
import demo.client.local.game.tools.GameHeartBeat;
import demo.client.local.lobby.Client;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BackgroundBlockModel;
import demo.client.shared.model.BlockOverflow;
import demo.client.shared.model.BoardModel;

public class BoardController {

  protected ControllableBoardDisplay boardDisplay;
  private SecondaryDisplayController secondaryController;
  private BoardMessageBus messageBus;

  /* A Block Drop board model. */
  protected BoardModel model;
  /* A block model. */
  protected Block activeBlock;
  /* A block model. */
  protected Block nextBlock;

  /* A updateTimer for running the game loop. */
  private Timer updateTimer;
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
  
  private GameHeartBeat gameTimer = new GameHeartBeat();
  
  public BoardController(ControllableBoardDisplay boardDisplay, SecondaryDisplayController secondaryController,
          BoardMessageBus messageBus) {
    this.boardDisplay = boardDisplay;
    this.secondaryController = secondaryController;
    this.messageBus = messageBus;

    // Initiate BoardModel.
    model = new BoardModel();
    activeBlock = new Block(model.getActiveBlock(), boardDisplay.getSizeCategory());
    nextBlock = new Block(model.getNextBlock(), boardDisplay.getSizeCategory());

    // Create a updateTimer to run the game loop.
    updateTimer = new Timer() {
      @Override
      public void run() {
        update();
      }
    };
  }

  protected void reset() {
    activeBlock = new Block(model.getActiveBlock(), boardDisplay.getSizeCategory());
    nextBlock = new Block(model.getNextBlock(), boardDisplay.getSizeCategory());
    clearState = ClearState.START;
    toBeCleared = new Block(model.getFullRows(), boardDisplay.getSizeCategory());
    bgBlock = new Block(model.getNonFullRows(), boardDisplay.getSizeCategory());
  }

  /*
   * Start a game of Block Drop.
   */
  public void startGame() {
    updateTimer.scheduleRepeating(loopTime);
  }

  /*
   * Handle user input, and update the game state and view. This method is is called every loopTime
   * milliseconds.
   */
  void update() {
    if (pause) {
      return;
    }

    boolean moved = false;

    // Check for rows to clear. Rows will stay in model until fully dealt with.
    int numFullRows = model.numFullRows();
    if (numFullRows > 0) {
      if (clearState.equals(ClearState.START))
        messageBus.sendMoveUpdate(model, Client.getInstance().getPlayer());
      clearRows(numFullRows);
    }
    else if (model.getRowsToAdd() > 0) {
      addRowsToBottom();
      messageBus.sendMoveUpdate(model, Client.getInstance().getPlayer());
    }
    // Only drop a new block if we are not clearing rows currently.
    else {
      // Reset the active block if necessary.
      if (!activeBlock.isModel(model.getActiveBlock())) {
        activeBlock = new Block(model.getActiveBlock(), boardDisplay.getSizeCategory());
        nextBlock = new Block(model.getNextBlock(), boardDisplay.getSizeCategory());
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
        updateTimer.cancel();
        
        long finalScore = secondaryController.getScoreTracker().getScore();
        boolean keepPlaying = Window.confirm("Game Over. Final Score: " + finalScore
                + ". Would you like to continue playing with a " + ScoreTracker.LOSS_PENALTY + " point penalty?");
        if (keepPlaying) {
          // Reset board model and controller
          model = new BoardModel();
          boardDisplay.clearBoard();
          reset();
          
          // Subtract score penalty and update score
          secondaryController.getScoreTracker().setScore(finalScore - ScoreTracker.LOSS_PENALTY);
          secondaryController.updateAndSortScore(secondaryController.getScoreTracker());
          messageBus.sendScoreUpdate(secondaryController.getScoreTracker(), null);
          
          // Show time
          startGame();
        }
        else {
          BoardPage.getInstance().goToLobby();
        }
      }
      // Reset for next loop.
      model.setDrop(false);
      if (rotate())
        incrementRotate();
      if (horizontalMove())
        incrementMovePacer();
      model.setPendingRowMove(0);
      loopCounter = loopCounter == dropIncrement ? 0 : loopCounter + 1;
      if (moved)
        messageBus.sendMoveUpdate(model, Client.getInstance().getPlayer());
    }
  }

  private void addRowsToBottom() {
    // This method should always be called when there are no full rows.
    BackgroundBlockModel bgModel = model.getNonFullRows();
    Block bg = new Block(bgModel, boardDisplay.getSizeCategory());
    boardDisplay.undrawBlock(0, 0, bg);
    boardDisplay.undrawBlock(Block.indexToCoord(model.getActiveBlockCol(), boardDisplay.getSizeCategory()),
            Block.indexToCoord(model.getActiveBlockRow(), boardDisplay.getSizeCategory()), activeBlock);
    model.addRows();
    bgModel = model.getNonFullRows();
    bg = new Block(bgModel, boardDisplay.getSizeCategory());
    boardDisplay.drawBlock(0, 0, bg);
    boardDisplay.drawBlock(Block.indexToCoord(model.getActiveBlockCol(), boardDisplay.getSizeCategory()),
            Block.indexToCoord(model.getActiveBlockRow(), boardDisplay.getSizeCategory()), activeBlock);
  }

  protected void clearRows(int numFullRows) {
    if (clearState.getCounter() == 0)
      switch (clearState) {
      case START:
        // Get blocks to be cleared.
        toBeCleared = new Block(model.getFullRows(), boardDisplay.getSizeCategory());
        bgBlock = new Block(model.getNonFullRows(), boardDisplay.getSizeCategory());
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
        bgBlock = new Block(model.getNonFullRows(), boardDisplay.getSizeCategory());
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
  protected boolean activeBlockUpdate() {
    boardDisplay.undrawBlock(Block.indexToCoord(model.getActiveBlockCol(), boardDisplay.getSizeCategory()),
            Block.indexToCoord(model.getActiveBlockRow(), boardDisplay.getSizeCategory()), activeBlock);

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
    boardDisplay.drawBlock(Block.indexToCoord(model.getActiveBlockCol(), boardDisplay.getSizeCategory()),
            Block.indexToCoord(model.getActiveBlockRow(), boardDisplay.getSizeCategory()), activeBlock);

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

  public void clearRotation() {
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

  public void incrementRotate() {
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

  public void setColMove(int i) {
    model.setPendingColMove(i);
    if (i != 0)
      incrementMovePacer();
    else
      clearMovePacer();
  }

  public boolean isPaused() {
    return pause;
  }

  public void setDrop(boolean b) {
    model.setDrop(b);
  }

  public void setPaused(boolean b) {
    if (b && !pause) {
      gameTimer.scheduleRepeating(2000);
      boardDisplay.pause();
    }
    else if (!b && pause) {
      gameTimer.cancel();
      boardDisplay.unpause();
    }
    pause = b;
  }

  public void setFast(boolean fast) {
    model.setFast(fast);
  }

  public SecondaryDisplayController getSecondaryController() {
    return secondaryController;
  }

  public void addRows(int rowsClearedLast) {
    model.setRowsToAdd(rowsClearedLast);
  }

  public void destroy() {
    updateTimer.cancel();
  }
}
