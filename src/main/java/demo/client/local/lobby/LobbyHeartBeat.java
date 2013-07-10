package demo.client.local.lobby;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import com.google.gwt.user.client.Timer;

import demo.client.shared.Command;

public class LobbyHeartBeat extends Timer {

  private MessageBus messageBus = ErraiBus.get();

  @Override
  public void run() {
    MessageBuilder.createMessage("Relay").command(Command.LOBBY_KEEP_ALIVE).withValue(Client.getInstance().getPlayer())
            .noErrorHandling().sendNowWith(messageBus);
    System.out.println("LobbyHeartBeat sent");
  }

}
