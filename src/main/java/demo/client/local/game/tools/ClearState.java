package demo.client.local.game.tools;

/**
 * The state of a row clearing operation used to control animation speed. A row flashes three times
 * before disappearing permanantly.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public enum ClearState {
  /**
   * The initial state before any row clearing has been drawn.
   */
  START,
  /**
   * State before the first undrawing of full rows.
   */
  FIRST_UNDRAW,
  /**
   * State before the first redrawing of full rows.
   */
  FIRST_REDRAW,
  /**
   * State before the second undrawing of full rows.
   */
  SECOND_UNDRAW,
  /**
   * State before the second redrawing of full rows.
   */
  SECOND_REDRAW,
  /**
   * State before the third undrawing of full rows.
   */
  THIRD_UNDRAW,
  /**
   * State before the third redrawing of full rows.
   */
  THIRD_REDRAW,
  /**
   * State before the last undrawing of full rows.
   */
  LAST_UNDRAW,
  /**
   * State before remaining blocks are dropped to fill vacant rows which were just cleared.
   */
  DROPPING;

  /**
   * This many calls getNextState must happen before the next state is given (for pacing animation).
   */
  public static final int CYCLES = 8;

  private int counter = 0;

  private void setCounter(int value) {
    counter = value;
  }

  /**
   * Based on the current state, get the next state.
   * 
   * @return If {@link ClearState#getCounter() getCounter()} == {@link ClearState#CYCLES CYCLES},
   *         this value will be the next state. Otherwise this value will be the same state
   *         enumeration with an incremented counter.
   */
  public ClearState getNextState() {
    ClearState retVal = this;
    if (counter == CYCLES || this == START) {
      counter = 0;
      switch (this) {
      case START:
        retVal = FIRST_UNDRAW;
        break;
      case FIRST_UNDRAW:
        retVal = FIRST_REDRAW;
        break;
      case FIRST_REDRAW:
        retVal = SECOND_UNDRAW;
        break;
      case SECOND_UNDRAW:
        retVal = SECOND_REDRAW;
        break;
      case SECOND_REDRAW:
        retVal = THIRD_UNDRAW;
        break;
      case THIRD_UNDRAW:
        retVal = THIRD_REDRAW;
        break;
      case THIRD_REDRAW:
        retVal = LAST_UNDRAW;
        break;
      case LAST_UNDRAW:
        retVal = DROPPING;
        break;
      case DROPPING:
        retVal = START;
        break;
      default:
        break;
      }
    }
    else {
      counter += 1;
    }

    retVal.setCounter(counter);
    return retVal;
  }

  /**
   * Get value of the counter used to control the flow of state changes.
   * 
   * @return When this value equals {@link ClearState#CYCLES CYCLES}, the next call to
   *         {@link ClearState#getNextState() getNextState()} will return the next state.
   */
  public int getCounter() {
    return counter;
  }
}
