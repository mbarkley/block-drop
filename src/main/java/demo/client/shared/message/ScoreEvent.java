package demo.client.shared.message;

import org.jboss.errai.common.client.api.annotations.Portable;

import demo.client.shared.meta.Player;
import demo.client.shared.meta.ScoreTracker;

@Portable
public class ScoreEvent {

  private ScoreTracker scoreTracker;
  private Player target;

  public ScoreEvent() {
  }

  public ScoreEvent(ScoreTracker scoreTracker, Player target) {
    this.setScoreTracker(scoreTracker);
    this.setTarget(target);
  }

  public ScoreEvent(ScoreTracker scoreTracker) {
    this(scoreTracker, null);
  }

  public ScoreTracker getScoreTracker() {
    return scoreTracker;
  }

  public void setScoreTracker(ScoreTracker scoreTracker) {
    this.scoreTracker = scoreTracker;
  }

  public Player getTarget() {
    return target;
  }

  public void setTarget(Player target) {
    this.target = target;
  }
}
