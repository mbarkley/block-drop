package demo.client.local.game.tools;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import demo.client.local.lobby.Client;
import demo.client.shared.game.model.BoardModel;
import demo.client.shared.message.Command;
import demo.client.shared.message.MoveEvent;
import demo.client.shared.message.ScoreEvent;
import demo.client.shared.meta.Player;
import demo.client.shared.meta.ScoreTracker;

public class BoardMessageBusImpl implements BoardMessageBus {

  private MessageBus messageBus = ErraiBus.get();

  @Override
  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target) {
    ScoreEvent event = new ScoreEvent(scoreTracker, target);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }

  @Override
  public void sendMoveUpdate(BoardModel state, Player player) {
    MoveEvent event = new MoveEvent(state, player, Client.getInstance().getGameRoom().getId());
    MessageBuilder.createMessage("Relay").command(Command.MOVE_UPDATE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }

  @Override
  public void sendPauseUpdate(Player player) {
    MessageBuilder.createMessage("Relay").command(Command.GAME_KEEP_ALIVE).withValue(player).noErrorHandling()
            .sendNowWith(messageBus);
  }

}
