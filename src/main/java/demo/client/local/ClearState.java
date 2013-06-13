package demo.client.local;

/*
 * For tracking state while clearing rows.
 */
public enum ClearState {
	START,			// Initial state.
	FIRST_UNDRAW,	// Undrawing and redrawing used to create flashing effect.
	FIRST_REDRAW,
	SECOND_UNDRAW,
	SECOND_REDRAW,
	THIRD_UNDRAW,
	THIRD_REDRAW,
	LAST_UNDRAW,
	DROPPING;		// Move remaining blocks down to bottom of screen.

	private static final int CYCLES = 8;

	private int counter = 0;

	public void setCounter(int value) {
		counter = value;
	}
	
	public ClearState getNextState() {
		ClearState retVal = this;
		if (counter == CYCLES || this == START) {
			counter = 0;
			switch(this) {
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
		} else {
			counter += 1;
		}
		
		retVal.setCounter(counter);
		return retVal;
	}

	public int getCounter() {
		return counter;
	}
}
