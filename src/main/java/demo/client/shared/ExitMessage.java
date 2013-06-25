package demo.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class ExitMessage {
  
  private GameRoom game;
  private Player player;
  public Player getPlayer() {
    return player;
  }
  public void setPlayer(Player player) {
    this.player = player;
  }
  public GameRoom getGame() {
    return game;
  }
  public void setGame(GameRoom game) {
    this.game = game;
  }
}
