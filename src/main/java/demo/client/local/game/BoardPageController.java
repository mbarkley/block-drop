package demo.client.local.game;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventHandler;

import demo.client.local.lobby.Client;
import demo.client.shared.Command;
import demo.client.shared.GameRoom;
import demo.client.shared.ScoreEvent;
import demo.client.shared.ScoreTracker;

/*
 * A controller class for a Block Drop game. Handles game loop and user input.
 */
class BoardPageController {

  /* The BoardPage on which this Block Drop game is displayed. */
  private BoardPage boardPage;

  @Inject
  private MessageBus messageBus = ErraiBus.get();

  /*
   * Set the page which this BoardPageController controls.
   * 
   * @param boardPage The BoardPage to be controlled.
   */
  void setPage(BoardPage boardPage) {
    this.boardPage = boardPage;
  }

  void updateScore(int numFullRows) {
    ScoreTracker scoreTracker = getScoreTracker();
    scoreTracker.updateScore(numFullRows);
    updateAndSortScore(scoreTracker);
    ScoreEvent event = new ScoreEvent(scoreTracker, null);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }

  private void updateAndSortScore(ScoreTracker scoreTracker) {
    List<ScoreTracker> modelList = boardPage.getScoreList();
    if (modelList.contains(scoreTracker)) {
      // Remove out-of-date score from list (different instance)
      modelList.remove(scoreTracker);
    }
    modelList.add(scoreTracker);
    Collections.sort(boardPage.getScoreList(), Collections.reverseOrder());
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
   * Start a game of Block Drop.
   */
  void startGame() {
    BoardController controller = new BoardController(this);
    EventHandler handler = new BoardKeyHandler(controller);
    boardPage.addHandlerToMainCanvas((KeyUpHandler) handler, KeyUpEvent.getType());
    boardPage.addHandlerToMainCanvas((KeyDownHandler) handler, KeyDownEvent.getType());

    // Initiate score tracker.
    GameRoom room = Client.getInstance().getGameRoom();
    List<ScoreTracker> scoreList = boardPage.getScoreList();
    scoreList.addAll(room.getScoreTrackers().values());
    Collections.sort(scoreList);

    // Subscribe to game channel
    messageBus.subscribe("Game" + room.getId(), new MessageCallback() {

      @Override
      public void callback(Message message) {
        Command command = Command.valueOf(message.getCommandType());
        switch (command) {
        case UPDATE_SCORE:
          ScoreEvent event = message.getValue(ScoreEvent.class);
          ScoreTracker scoreTracker = event.getScoreTracker();
          if (scoreTracker.getId() != Client.getInstance().getPlayer().getId()) {
            updateAndSortScore(scoreTracker);
          }
        default:
          break;
        }
      }
    });

    controller.start();
  }

  void drawBlockToNextCanvas(Block nextBlock) {
    boardPage.drawBlockToNextCanvas(nextBlock);
  }

  void undrawBlock(int i, int j, Block toBeCleared) {
    boardPage.undrawBlock(i, j, toBeCleared);
  }

  void drawBlock(int i, int j, Block toBeCleared) {
    boardPage.drawBlock(i, j, toBeCleared);
  }

  void pause() {
    boardPage.pause();
  }

  void unpause() {
    boardPage.unpause();
  }
}
