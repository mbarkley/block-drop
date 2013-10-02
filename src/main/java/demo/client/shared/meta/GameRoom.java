package demo.client.shared.meta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;

/**
 * A game room containing information on players and their scores. Used for storing and transmitting
 * game room data.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Bindable
@Portable
public class GameRoom {

  private int id;
  private Map<Integer, Player> players;
  private Map<Integer, ScoreTracker> scoreTrackers;

  /**
   * Create an empty game room.
   */
  public GameRoom() {
    players = new ConcurrentHashMap<Integer, Player>();
    scoreTrackers = new ConcurrentHashMap<Integer, ScoreTracker>();
  }

  /**
   * Get the players in this room.
   * 
   * @return A map of player ids to players for players in the room.
   */
  public Map<Integer, Player> getPlayers() {
    return players;
  }

  /**
   * Set the players in this room.
   * 
   * @param players
   *          A map of player ids to players for players in the room.
   */
  public void setPlayers(Map<Integer, Player> players) {
    this.players = players;
  }

  /**
   * Add a player to this room.
   * 
   * @param player
   *          The player to be added.
   */
  public void addPlayer(Player player) {
    players.put(player.getId(), player);
    ScoreTracker scoreTracker = new ScoreTracker();
    scoreTracker.setPlayer(player);
    scoreTracker.setGameId(getId());
    scoreTrackers.put(player.getId(), scoreTracker);
  }

  /**
   * Get a player by their id.
   * 
   * @param id
   *          The id of the player to be retrieved.
   * @return The player in this room with the given id, or {@code null} if no such player exists.
   */
  public Player getPlayer(int id) {
    return players.get(id);
  }

  /**
   * Remove a player by their id.
   * 
   * @param id
   *          The id of the player to be removed.
   * @return The removed player, or {@code null} if no such player exists.
   */
  public Player removePlayer(int id) {
    return players.remove(id);
  }

  /**
   * Get the id of this room.
   * 
   * @return The unique id of this room.
   */
  public int getId() {
    return id;
  }

  /**
   * Set the id of this room.
   * 
   * @param id
   *          A unique id for this room.
   */
  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object other) {
    return other != null && other instanceof GameRoom && ((GameRoom) other).getId() == this.getId();
  }

  /**
   * Check if this room is empty.
   * 
   * @return True iff this room is empty.
   */
  public boolean isEmpty() {
    return players.size() == 0;
  }

  /**
   * Get the score trackers of all the players in this room.
   * 
   * @return A map of player ids to score trackers of those players.
   */
  public Map<Integer, ScoreTracker> getScoreTrackers() {
    return scoreTrackers;
  }

  /**
   * Set the score trackers of the players in this room.
   * 
   * @param scoreTrackers
   *          A map of player ids to their score trackers.
   */
  public void setScoreTrackers(Map<Integer, ScoreTracker> scoreTrackers) {
    this.scoreTrackers = scoreTrackers;
  }

  /**
   * Get the score tracker associated with the given player.
   * 
   * @param player
   *          A player in this room.
   * @return The score tracker of the given player.
   */
  public ScoreTracker getScoreTracker(Player player) {
    return scoreTrackers.get(player.getId());
  }

  /**
   * Update a players score from the given score tracker.
   * 
   * @param value
   *          A score tracker for a player already in this room.
   */
  public void updateScoreTracker(ScoreTracker value) {
    getScoreTracker(value.getPlayer()).setScore(value.getScore());
  }
}
