package demo.client.shared;

import java.util.HashMap;
import java.util.Map;


public class GameRoom {

  private int id;
  private Map<Integer,Player> players;
  
  public GameRoom() {
    players = new HashMap<Integer,Player>();
  }
  
  public void addPlayer(Player player) {
    players.put(player.getId(), player);
  }
  
  public Player getPlayer(int id) {
    return players.get(id);
  }
  
  public Player removePlayer(int id) {
    return players.remove(id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
