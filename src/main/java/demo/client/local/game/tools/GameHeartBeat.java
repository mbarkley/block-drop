package demo.client.local.game.tools;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;

import com.google.gwt.user.client.Timer;

import demo.client.local.Client;
import demo.client.shared.message.Command;

/**
 * A {@link Timer Timer} subclass for sending {@link Command#GAME_KEEP_ALIVE keep alive} messages to
 * the server.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class GameHeartBeat extends Timer {

  private boolean activated = false;
  private Client client;
  
  public GameHeartBeat(Client client) {
    this.client = client;
  }

  @Override
  public void scheduleRepeating(int milliseconds) {
    activated = true;
    super.scheduleRepeating(milliseconds);
  }

  @Override
  public void run() {
    MessageBuilder.createMessage("Relay").command(Command.GAME_KEEP_ALIVE).withValue(client.getPlayer())
            .noErrorHandling().sendNowWith(ErraiBus.get());
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
