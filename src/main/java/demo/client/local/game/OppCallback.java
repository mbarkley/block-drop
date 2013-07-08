package demo.client.local.game;

import java.util.Map;
import java.util.TreeMap;

import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;

import demo.client.local.lobby.Client;
import demo.client.shared.Command;
import demo.client.shared.GameRoom;
import demo.client.shared.Player;
import demo.client.shared.ScoreEvent;
import demo.client.shared.model.MoveEvent;

public class OppCallback implements MessageCallback {

  private Map<Player, OppController> oppControllers;
  private ControllableBoardDisplay boardDisplay;

  public OppCallback(ControllableBoardDisplay boardDisplay) {
    this.oppControllers = new TreeMap<Player, OppController>();
    this.boardDisplay = boardDisplay;

    GameRoom game = Client.getInstance().getGameRoom();
    for (Player player : game.getPlayers().values()) {
      this.oppControllers.put(player, new OppController(boardDisplay));
    }
    this.oppControllers.get(Client.getInstance().getPlayer()).setActive(true);
    this.oppControllers.get(Client.getInstance().getPlayer()).startGame();
  }

  @Override
  public void callback(Message message) {
    Command command = Command.valueOf(message.getCommandType());
    switch (command) {
    case MOVE_UPDATE:
      MoveEvent moveEvent = message.getValue(MoveEvent.class);
      oppControllers.get(moveEvent.getPlayer()).addState(moveEvent.getState());
      break;
    case UPDATE_SCORE:
      ScoreEvent scoreEvent = message.getValue(ScoreEvent.class);
      if (!oppControllers.containsKey(scoreEvent.getScoreTracker().getPlayer())) {
        oppControllers.put(scoreEvent.getScoreTracker().getPlayer(), new OppController(boardDisplay));
      }
      break;
    case SWITCH_OPPONENT:
      Player player = message.getValue(Player.class);
      for (OppController controller : oppControllers.values()) {
        if (controller.isActive()) {
          controller.setActive(false);
        }
      }
      oppControllers.get(player).setActive(true);
      oppControllers.get(player).startGame();
      break;
    default:
      break;
    }
  }

}
