package demo.client.shared.model;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.Player;

@Portable
public class MoveEvent {

  private BoardModel state;
  private Player player;
  private int gameId;
  
  public MoveEvent() {}

  public MoveEvent(BoardModel state, Player player, int gameId) {
    this.setState(state);
    this.setPlayer(player);
    this.setGameId(gameId);
  }

  public BoardModel getState() {
    return state;
  }

  public void setState(BoardModel state) {
    this.state = state;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  
}
