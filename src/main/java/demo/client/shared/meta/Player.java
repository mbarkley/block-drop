package demo.client.shared.meta;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

/**
 * A portable bean class for storing and transmitting information identifying players of a Game.
 */
@Portable
@Conversational
@Bindable
public class Player implements Comparable<Player> {

  /**
   * The id of this player for identifying different instances representing the same player between
   * the server and client. An id of 0 is given if this player has not yet received an id from the
   * server.
   */
  private int id;
  /** The nickname of this player to be displayed to other users. */
  private String name;
  /** The id of the game this player is in, or 0 if the player is in the lobby. */
  private int gameId;

  /**
   * A default no-arg constructor for automated bean creation.
   */
  public Player() {
    this(0, "user", 0);
  }

  /**
   * Create a player with a non-default nickname.
   * 
   * @param nick
   *          The nickname of the player.
   */
  public Player(String nick) {
    this(0, nick, 0);
  }

  /**
   * Create a player with a non-default nickname and non-default id.
   * 
   * @param id
   *          This player's id. Should be positive, or 0 if this player has not yet received an id
   *          from the server.
   * 
   * @param nick
   *          The nickname of this player.
   */
  public Player(int id, String nick) {
    this(id, nick, 0);
  }

  /**
   * Create a player with no default values.
   * 
   * @param id
   *          This player's id. Should be positive, or 0 if this player has not yet received an id
   *          from the server.
   * 
   * @param nick
   *          The nickname of this player.
   * 
   * @param gameId
   *          The id of the game the user is in. Should be a positive integer or 0 if the user is
   *          not in any game.
   */
  public Player(int id, String nick, int gameId) {
    this.id = id;
    this.name = nick;
    this.gameId = gameId;
  }

  /**
   * Check if the user has been registered with the server.
   * 
   * @return True iff the user has been registered with the server (i.e. has a non-zero, positive
   *         id).
   */
  public boolean hasRegistered() {
    return id != 0;
  }

  /**
   * Check if the player is currently in a game.
   * 
   * @return True iff the player is currently in a game (i.e. has a non-zero gameId).
   */
  public boolean isPlaying() {
    return gameId != 0;
  }

  /**
   * Get the id of the game the user is in.
   * 
   * @return The id of the game the user is in, or 0 if the user is not in a game.
   */
  public int getGameId() {
    return gameId;
  }

  /**
   * Set the id of the game the user is in.
   * 
   * @param gameId
   *          The id of the game the user is in. Should be a positive integer or 0 if the user is
   *          not in any game.
   */
  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  /**
   * Get the player's nickname.
   * 
   * @return The nickname of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * Set the player's nickname.
   * 
   * @param name
   *          The nickname of the player.
   */
  public void setName(String nick) {
    this.name = nick;
  }

  /**
   * Get this player's id.
   * 
   * @return This player's id.
   */
  public int getId() {
    return id;
  }

  /**
   * Set this player's id.
   * 
   * @param id
   *          This player's id. Should be positive, or 0 if this player has not yet received an id
   *          from the server.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Check if this is the same player as other. This method is safe if other == null.
   * 
   * @param other
   *          Another player object to be compared.
   * 
   * @return True iff this player and other refer to the same player (i.e. have the same id).
   */
  public boolean equals(Object other) {
    return other != null && other instanceof Player && this.getId() == ((Player) other).getId();
  }

  @Override
  public int compareTo(Player o) {
    return getId() - o.getId();
  }
}
