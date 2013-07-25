package demo.client.shared.lobby;

import java.util.HashSet;
import java.util.Set;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.Player;

/*
 * A portable JavaBean for transmitting invitations to games between clients.
 */
@Portable
public class Invitation {

  private Player host;
  private Set<Player> guests;
  private int gameId;
  private Player target;

  public Invitation() {
    host = null;
    guests = new HashSet<Player>();
    target = null;
    setGameId(0);
  }
  
  public Invitation(Invitation original, Player newTarget) {
    setHost(original.getHost());
    setGuests(original.getGuests());
    setGameId(original.getGameId());
    setTarget(newTarget);
  }

  public Player getHost() {
    return host;
  }

  public void setHost(Player player) {
    host = player;
  }
  
  public Set<Player> getGuests() {
    return guests;
  }

  public void setGuests(Set<Player> selected) {
    guests = selected;
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
  }

  public Player getTarget() {
    return target;
  }

  public void setTarget(Player target) {
    this.target = target;
  }

}
