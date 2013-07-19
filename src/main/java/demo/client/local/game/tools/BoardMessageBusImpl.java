package demo.client.local.game.tools;

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

public class BoardMessageBusImpl implements BoardMessageBus {

  private MessageBus messageBus = ErraiBus.get();

  /* (non-Javadoc)
   * @see demo.client.local.game.BoardMessageBus#sendScoreUpdate(demo.client.shared.ScoreTracker, demo.client.shared.Player)
   */
  @Override
  public void sendScoreUpdate(ScoreTracker scoreTracker, Player target) {
    ScoreEvent event = new ScoreEvent(scoreTracker, target);
    MessageBuilder.createMessage("Relay").command(Command.UPDATE_SCORE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
  }

  /* (non-Javadoc)
   * @see demo.client.local.game.BoardMessageBus#sendMoveUpdate(demo.client.shared.model.BoardModel, demo.client.shared.Player)
   */
  @Override
  public void sendMoveUpdate(BoardModel state, Player player) {
    MoveEvent event = new MoveEvent(state, player, Client.getInstance().getGameRoom().getId());
    MessageBuilder.createMessage("Relay").command(Command.MOVE_UPDATE).withValue(event).noErrorHandling()
            .sendNowWith(messageBus);
    System.out.println("move update sent to relay");
  }

  @Override
  public void sendPauseUpdate(Player player) {
    MessageBuilder.createMessage("Relay").command(Command.GAME_KEEP_ALIVE).withValue(player).noErrorHandling()
            .sendNowWith(messageBus);
    System.out.println("pause update sent");
  }

}
