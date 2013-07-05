package demo.client.local.game;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.ui.client.widget.ListWidget;

import com.google.gwt.canvas.client.Canvas;

import demo.client.local.Style;
import demo.client.local.lobby.Client;
import demo.client.shared.Command;
import demo.client.shared.GameRoom;
import demo.client.shared.Player;
import demo.client.shared.ScoreEvent;
import demo.client.shared.ScoreTracker;

/*
 * A controller class for a Block Drop game. Handles game loop and user input.
 */
class SecondaryDisplayController {

  private ListWidget<ScoreTracker, ScorePanel> scoreList;
  private Canvas nextCanvas;

  @Inject
  private MessageBus messageBus = ErraiBus.get();

  public SecondaryDisplayController(ListWidget<ScoreTracker, ScorePanel> scoreList, Canvas nextCanvas) {
    this.scoreList = scoreList;
    this.nextCanvas = nextCanvas;

    // Initiate score tracker.
    GameRoom room = Client.getInstance().getGameRoom();
    List<ScoreTracker> scoreTrackers = scoreList.getValue();
    scoreTrackers.addAll(room.getScoreTrackers().values());
    Collections.sort(scoreTrackers);

    getScoreTracker().select();
    scoreList.getWidget(getScoreTracker()).setSelected(true);
  }

  public void selectNextPlayer() {
    ScoreTracker current = getSelectedTracker();
    if (current != null) {
      int index = scoreList.getValue().indexOf(current);
      int next = (index + 1) % scoreList.getValue().size();
      scoreList.getValue().get(index).deselect();
      scoreList.getWidget(index).setSelected(false);
      scoreList.getValue().get(next).select();
      scoreList.getWidget(next).setSelected(true);
    }
  }

  public void selectLastPlayer() {
    ScoreTracker current = getSelectedTracker();
    if (current != null) {
      int index = scoreList.getValue().indexOf(current);
      int last = (index - 1 + scoreList.getValue().size()) % scoreList.getValue().size();
      scoreList.getValue().get(index).deselect();
      scoreList.getWidget(index).setSelected(false);
      scoreList.getWidget(last).setSelected(true);
      scoreList.getValue().get(last).select();
    }
  }

  private ScoreTracker getSelectedTracker() {
    for (ScoreTracker tracker : scoreList.getValue()) {
      if (tracker.isSelected()) {
        return tracker;
      }
    }
    return null;
  }

  public void updateScore(int numFullRows) {
    ScoreTracker scoreTracker = getScoreTracker();
    scoreTracker.updateScore(numFullRows);
    updateAndSortScore(scoreTracker);
    ScoreTracker selected = getSelectedTracker();
    Player target = getScoreTracker().equals(selected) ? null : selected.getPlayer();
    ScoreEvent event = new ScoreEvent(scoreTracker, target);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }

  void updateAndSortScore(ScoreTracker scoreTracker) {
    List<ScoreTracker> modelList = scoreList.getValue();
    if (modelList.contains(scoreTracker)) {
      // Remove out-of-date score from list (different instance)
      modelList.remove(scoreTracker);
    }
    modelList.add(scoreTracker);
    Collections.sort(scoreList.getValue(), Collections.reverseOrder());
    for (int i = 0; i < modelList.size(); i++) {
      scoreList.getWidget(i).setSelected(modelList.get(i).isSelected());
    }
  }

  private ScoreTracker getScoreTracker() {
    List<ScoreTracker> trackers = scoreList.getValue();
    for (ScoreTracker t : trackers) {
      if (t.getPlayer().equals(Client.getInstance().getPlayer())) {
        return t;
      }
    }
    return null;
  }

  public void drawBlockToNextCanvas(Block nextBlock) {
    // Clear everything.
    nextCanvas.getContext2d().setFillStyle("lightgrey");
    nextCanvas.getContext2d().fillRect(0, 0, Size.NEXT_COORD_WIDTH, Size.MAIN_COORD_HEIGHT);

    // Draw title.
    nextCanvas.getContext2d().setFillStyle("black");
    nextCanvas.getContext2d().setFont("bold 20px sans-serif");
    nextCanvas.getContext2d().fillText("Next Block", 10, 20);

    nextBlock.draw(2 * Size.BLOCK_SIZE + nextBlock.getCentreColDiff() * Size.BLOCK_SIZE, 2 * Size.BLOCK_SIZE
            + nextBlock.getCentreRowDiff() * Size.BLOCK_SIZE, nextCanvas.getContext2d());
  }
}
