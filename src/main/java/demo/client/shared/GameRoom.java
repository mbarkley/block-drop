package demo.client.shared;

import java.util.HashMap;
import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

@Bindable
@Portable
public class GameRoom {

  private int id;
  private Map<Integer, Player> players;

  public Map<Integer, Player> getPlayers() {
    return players;
  }

  public void setPlayers(Map<Integer, Player> players) {
    this.players = players;
  }

  public GameRoom() {
    players = new HashMap<Integer, Player>();
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
  
  public boolean equals(Object other) {
    return other != null && other instanceof GameRoom && ((GameRoom) other).getId() == this.getId();
  }

  public boolean isEmpty() {
    return players.size() == 0;
  }
}
