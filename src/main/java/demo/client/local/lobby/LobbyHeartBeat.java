package demo.client.local.lobby;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;

import com.google.gwt.user.client.Timer;

import demo.client.local.Client;
import demo.client.shared.message.Command;

/**
 * A {@link Timer timer} for sending {@link Command#LOBBY_KEEP_ALIVE keep alive} messages to the
 * server.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class LobbyHeartBeat extends Timer {
  
  private Client client;
  
  public LobbyHeartBeat(Client client) {
    this.client = client;
  }

  @Override
  public void run() {
    MessageBuilder.createMessage("Relay").command(Command.LOBBY_KEEP_ALIVE).withValue(client.getPlayer())
            .noErrorHandling().sendNowWith(ErraiBus.get());
  }

}
