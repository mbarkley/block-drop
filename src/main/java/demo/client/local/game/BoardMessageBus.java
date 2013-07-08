package demo.client.local.game;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import demo.client.local.lobby.Client;
import demo.client.shared.Command;
import demo.client.shared.Player;
import demo.client.shared.ScoreEvent;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.BoardModel;
import demo.client.shared.model.MoveEvent;

public class BoardMessageBus {

  private MessageBus messageBus = ErraiBus.get();

  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target) {
    ScoreEvent event = new ScoreEvent(scoreTracker, target);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }

  public void sendMoveUpdate(BoardModel state, Player player) {
    MoveEvent event = new MoveEvent(state, player, Client.getInstance().getGameRoom().getId());
    MessageBuilder.createMessage("Relay").command(Command.MOVE_UPDATE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
    System.out.println("move update sent to relay");
  }

}
