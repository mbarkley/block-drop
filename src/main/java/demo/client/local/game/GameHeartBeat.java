package demo.client.local.game;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.MessageBus;

import com.google.gwt.user.client.Timer;

import demo.client.local.lobby.Client;
import demo.client.shared.Command;

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
    System.out.println("Game keep alive sent");
  }

  @Override
  public void cancel() {
    activated = false;
    super.cancel();
  }
  
  public boolean isRepeating() {
    return activated;
  }
}
