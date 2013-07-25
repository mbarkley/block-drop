package demo.client.shared.message;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.Player;
import demo.client.shared.meta.ScoreTracker;

/**
 * A portable bean for sending {@link Command#UPDATE_SCORE score updates} between server and
 * clients.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Portable
public class ScoreEvent {

  private ScoreTracker scoreTracker;
  private Player target;

  /**
   * A default no-arg constructor for proxying.
   */
  public ScoreEvent() {
  }

  /**
   * Create a ScoreEvent.
   * 
   * @param scoreTracker
   *          The score tracker of the player with the updated score.
   * @param target
   *          The targeted player to sabotage or {@code null}.
   */
  public ScoreEvent(ScoreTracker scoreTracker, Player target) {
    this.setScoreTracker(scoreTracker);
    this.setTarget(target);
  }

  /**
   * Create a ScoreEvent with no target.
   * 
   * @param scoreTracker
   *          The score tracker of the player with the updated score.
   */
  public ScoreEvent(ScoreTracker scoreTracker) {
    this(scoreTracker, null);
  }

  /**
   * Get the score tracker associated with this event.
   * 
   * @return The score tracker associated with this event.
   */
  public ScoreTracker getScoreTracker() {
    return scoreTracker;
  }

  /**
   * Set the score tracker associated with this event.
   * 
   * @param scoreTracker
   *          The score tracker to be associated with this event.
   */
  public void setScoreTracker(ScoreTracker scoreTracker) {
    this.scoreTracker = scoreTracker;
  }

  /**
   * Get the target of this score event.
   * 
   * @return The target of this event, or {@code null}.
   */
  public Player getTarget() {
    return target;
  }

  /**
   * Set the target of this event.
   * 
   * @param target
   *          A player to target, or {@code null}.
   */
  public void setTarget(Player target) {
    this.target = target;
  }
}
