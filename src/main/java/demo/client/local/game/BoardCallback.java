package demo.client.local.game;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import demo.client.local.lobby.Client;
import demo.client.shared.Command;
import demo.client.shared.ScoreEvent;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.MoveEvent;

public class BoardCallback implements MessageCallback {
  
  private MessageBus messageBus = ErraiBus.get();
  
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
      ScoreTracker scoreTracker = event.getScoreTracker();
      if (scoreTracker.getId() != Client.getInstance().getPlayer().getId()) {
        secondaryController.updateAndSortScore(scoreTracker);
        if (Client.getInstance().getPlayer().equals(event.getTarget())) {
          controller.addRows(scoreTracker.getRowsClearedLast());
        }
      }
      break;
    case MOVE_UPDATE:
      MoveEvent moveEvent = message.getValue(MoveEvent.class);
      if (!moveEvent.getPlayer().equals(Client.getInstance().getPlayer())) {
        System.out.println("move update has reached final destination");
        
      }
      else {
        System.out.println("received own move update");
      }
      break;
    default:
      break;
    }
  }
}