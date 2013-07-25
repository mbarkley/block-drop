package demo.client.local.game.tools;

/**
 * Used for controlling the flow of events occuring in a game loop.
 * 
 * How this object is used:
 * 
 * <pre>
 * while(true) {
 *  if (pacer.isReady()) {
 *   // do something
 *  }
 *  // whatever else
 *  pacer.increment()
 * }
 * </pre>
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class Pacer {

  private boolean[] track;
  private boolean initial;

  /**
   * Create a pacer instance.
   * 
   * @param size
   *          The length of this pacer's track. Calls to {@link Pacer#isReady() isReady()} will
   *          return false until the pacer is incremented to the last spot in the track.
   * @param initial
   *          If true, {@link Pacer#isReady() isReady()} will also return true when on the first
   *          spot of the track.
   */
  public Pacer(int size, boolean initial) {
    track = new boolean[size];
    this.initial = initial;
  }

  /**
   * Create a pacer instance.
   * 
   * @param size
   *          The length of this pacer's track. Calls to {@link Pacer#isReady() isReady()} will
   *          return false until the pacer is incremented to the last spot in the track.
   */
  public Pacer(int size) {
    this(size, true);
  }

  /**
   * Check if a paced action should be conducted now.
   * 
   * @return True if an action that is being paced should be conducted.
   */
  public boolean isReady() {
    int i;
    for (i = 0; i < track.length; i++) {
      if (!track[i]) {
        break;
      }
    }
    return i == track.length || (initial && i == 1);
  }

  /**
   * Reset the pacer track back to the initial state.
   */
  public void clear() {
    for (int i = 0; i < track.length; i++) {
      track[i] = false;
    }
  }

  /**
   * Increment the pacer track. This method will do nothing if the pacer is already at the end of
   * its track.
   */
  public void increment() {
    int i = 0;
    while (i < track.length && track[i]) {
      i++;
    }

    if (i != track.length) {
      track[i] = true;
    }
  }

}
