package demo.client.local.game.callbacks;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import demo.client.local.game.controllers.BoardController;
import demo.client.local.game.controllers.SecondaryDisplayController;
import demo.client.local.game.tools.CallOutManager;
import demo.client.local.lobby.Client;
import demo.client.shared.message.Command;
import demo.client.shared.message.ScoreEvent;
import demo.client.shared.meta.Player;
import demo.client.shared.meta.ScoreTracker;

/**
 * A MessageCallback implementation for handling remote events from the Block Drop Server.
 * 
 * @author Max Barkley <mbarkley@redhat.com>
 * 
 */
public class BoardCallback implements MessageCallback {

  private BoardController controller;
  private SecondaryDisplayController secondaryController;

  public BoardCallback(BoardController controller, SecondaryDisplayController secondaryController) {
    this.controller = controller;
    this.secondaryController = secondaryController;
  }

  @Override
  public void callback(Message message) {
    Command command = Command.valueOf(message.getCommandType());
    switch (command) {
    case UPDATE_SCORE:
      ScoreEvent event = message.getValue(ScoreEvent.class);
      updateScore(event.getScoreTracker(), event.getTarget());
      break;
    case LEAVE_GAME:
      Player player = message.getValue(Player.class);
      if (!player.equals(Client.getInstance().getPlayer()))
        removePlayer(player);
      break;
    default:
      break;
    }
  }

  private void removePlayer(Player player) {
    Client.getInstance().getGameRoom().removePlayer(player.getId());
    secondaryController.removeTracker(player);
  }

  private void updateScore(ScoreTracker scoreTracker, Player target) {
    if (scoreTracker.getId() != Client.getInstance().getPlayer().getId()) {
      secondaryController.updateAndSortScore(scoreTracker);
      if (Client.getInstance().getPlayer().equals(target)) {
        controller.addRows(scoreTracker.getRowsClearedLast());
        CallOutManager.createAttackCallout("score-panel-" + scoreTracker.getId(), scoreTracker.getName());
      }
    }
  }
}