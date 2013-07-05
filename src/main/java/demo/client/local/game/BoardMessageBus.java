package demo.client.local.game;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import demo.client.shared.Command;
import demo.client.shared.Player;
import demo.client.shared.ScoreEvent;
import demo.client.shared.ScoreTracker;

public class BoardMessageBus {
  
  private MessageBus messageBus = ErraiBus.get();

  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target) {
    ScoreEvent event = new ScoreEvent(scoreTracker, target);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }
  
}
