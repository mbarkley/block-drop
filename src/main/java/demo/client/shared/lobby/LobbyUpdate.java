package demo.client.shared.lobby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.GameRoom;
import demo.client.shared.meta.Player;

/*
 * A portable JavaBean for transmitting lists of playerMap in lobby and gameMap in progress.
 */
@Portable
public class LobbyUpdate {

  /* A map player ids to Player objects (of playerMap in lobby). */
  private Map<Integer, Player> playerMap = null;
  /* A map of game ids to Game objects (of gameMap in progress). */
  private Map<Integer, GameRoom> gameMap = null;

  /*
   * A default no-arg constructor for automated bean construction. Users should prefer the
   * constructor taking Map<Integer,Player> and Map<Integer,Game> parameters.
   */
  public LobbyUpdate() {
  }

  /*
   * Construct a LobbyUpdate instance with the current playerMap in the lobby and current gameMap in
   * progress.
   * 
   * @param playerMap A map of player ids to playerMap, for all the playerMap in the lobby.
   * 
   * @param gameMap A map of game ids to gameMap, for all the currently in progress gameMap.
   */
  public LobbyUpdate(Map<Integer, Player> players, Map<Integer, GameRoom> games) {
    this.playerMap = players;
    this.gameMap = games;
  }

  /*
   * Get a map of the current playerMap in the lobby.
   * 
   * @return Return a map of player ids to playerMap for the playerMap currently in the lobby.
   */
  public Map<Integer, Player> getPlayerMap() {
    return playerMap;
  }

  /*
   * Set the map of playerMap currently in the lobby.
   * 
   * @param playerMap A map of player ids to playerMap for the playerMap currently in the lobby.
   */
  public void setPlayerMap(Map<Integer, Player> players) {
    this.playerMap = players;
  }

  /*
   * Set the map of gameMap currently in progress.
   * 
   * @param gameMap A map of game ids to gameMap of the gameMap currently in progress.
   */
  public void setGameMap(Map<Integer, GameRoom> games) {
    this.gameMap = games;
  }

  /*
   * Get the map of gameMap currently in progress.
   * 
   * @return Return a map of game ids to gameMap of the gameMap currently in progress.
   */
  public Map<Integer, GameRoom> getGameMap() {
    return gameMap;
  }
  
  public List<Player> getPlayers() {
    return getterHelper(playerMap);
  }
  
  public List<GameRoom> getGames() {
    return getterHelper(gameMap);
  }
  
  private <T> List<T> getterHelper(Map<Integer, T> map) {
    List<T> retVal = new ArrayList<T>();
    
    for (T p : map.values()) {
      retVal.add(p);
    }
    
    return retVal;
  }
}
