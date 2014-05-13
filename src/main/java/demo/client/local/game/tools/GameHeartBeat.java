package demo.client.local.game.tools;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import com.google.gwt.user.client.Timer;

import demo.client.local.lobby.Client;
import demo.client.shared.message.Command;

/**
 * A {@link Timer Timer} subclass for sending {@link Command#GAME_KEEP_ALIVE keep alive} messages to
 * the server.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class GameHeartBeat extends Timer {

  private MessageBus messageBus = ErraiBus.get();
  private boolean activated = false;

  @Override
  public void scheduleRepeating(int milliseconds) {
    activated = true;
    super.scheduleRepeating(milliseconds);
  }

  @Override
  public void run() {
    MessageBuilder.createMessage("Relay").command(Command.GAME_KEEP_ALIVE).withValue(Client.getInstance().getPlayer())
            .noErrorHandling().sendNowWith(messageBus);
  }

  @Override
  public void cancel() {
    activated = false;
    super.cancel();
  }

  /**
   * Check if this timer is currently scheduled to repeat.
   * 
   * @return True iff this timer is currently scheduled to repeat.
   */
  public boolean isRepeating() {
    return activated;
  }
}
