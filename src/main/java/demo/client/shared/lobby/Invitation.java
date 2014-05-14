package demo.client.shared.lobby;

import java.util.HashSet;
import java.util.Set;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.Player;

/**
 * A portable bean for transmitting invitations to games between clients.
 */
@Portable
public class Invitation {

  /** The player who initially created this invitation */
  private Player host;
  /** All the players invited by the host player */
  private Set<Player> guests;
  /** The unique id of this game */
  private int gameId;
  /** A target used for relaying a specific invitation to a particular guest */
  private Player target;

  /**
   * Create a blank invitation.
   */
  public Invitation() {
    host = null;
    guests = new HashSet<Player>();
    target = null;
    setGameId(0);
  }

  /**
   * Create a new invitation from a pre-existing invitation instance.
   * 
   * @param original
   *          The invitation to clone.
   * @param newTarget
   *          The target of this cloned invitation.
   */
  public Invitation(Invitation original, Player newTarget) {
    setHost(original.getHost());
    setGuests(original.getGuests());
    setGameId(original.getGameId());
    setTarget(newTarget);
  }

  /**
   * Get the original sender of this invitation.
   * 
   * @return The original sender of this invitation.
   */
  public Player getHost() {
    return host;
  }

  /**
   * Set the original sender of this invitation.
   * 
   * @param player
   *          The original sender of this invitation.
   */
  public void setHost(Player player) {
    host = player;
  }

  /**
   * Get the guests to be invited by this invitation.
   * 
   * @return The players to be invited by this invitation.
   */
  public Set<Player> getGuests() {
    return guests;
  }

  /**
   * Set the guests to be invited by this invitation.
   * 
   * @param selected
   *          The guests to be invited by this invitation.
   */
  public void setGuests(Set<Player> selected) {
    guests = selected;
  }

  /**
   * Get the id of the game created by this invitation.
   * 
   * @return The id of the {@link demo.client.shared.meta.GameRoom#getId() game room} associated
   *         with this invitation, or 0 if no game room has been created yet.
   */
  public int getGameId() {
    return gameId;
  }

  /**
   * Set the id of the game created by this invitation. This should be assigned by the
   * {@link demo.server.LobbyServer server}.
   * 
   * @param gameId
   *          The id of the game room created for this invitation.
   */
  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  /**
   * Get the target of this invitation.
   * 
   * @return The player to whom this particular invitation will be routed.
   */
  public Player getTarget() {
    return target;
  }

  /**
   * Set the target of this invitation.
   * 
   * @param target
   *          The player to whom this particular invitation will be routed.
   */
  public void setTarget(Player target) {
    this.target = target;
  }

}
