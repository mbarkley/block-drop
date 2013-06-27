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
  private Map<Integer, ScoreTracker> scoreTrackers;
  
  public Map<Integer, Player> getPlayers() {
    return players;
  }

  public void setPlayers(Map<Integer, Player> players) {
    this.players = players;
  }

  public GameRoom() {
    players = new HashMap<Integer, Player>();
    scoreTrackers = new HashMap<Integer, ScoreTracker>();
  }

  public void addPlayer(Player player) {
    players.put(player.getId(), player);
    ScoreTracker scoreTracker = new ScoreTracker();
    scoreTracker.setPlayer(player);
    scoreTracker.setGameId(getId());
    scoreTrackers.put(player.getId(), scoreTracker);
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

  public Map<Integer, ScoreTracker> getScoreTrackers() {
    return scoreTrackers;
  }

  public void setScoreTrackers(Map<Integer, ScoreTracker> scoreTrackers) {
    this.scoreTrackers = scoreTrackers;
  }

  public ScoreTracker getScoreTracker(Player player) {
    return scoreTrackers.get(player.getId());
  }

  public void updateScoreTracker(ScoreTracker value) {
    getScoreTracker(value.getPlayer()).setScore(value.getScore());
  }
}
