package demo.client.shared.model;

/*
 * A model for an L-shaped block in Block Drop.
 */
public class LBlockModel extends BlockModel {
	
	public static final int CODE = 8;
	
	/*
	 * Create a LBlockModel instance.
	 */
	public LBlockModel() {
		// Assign unique id.
		super(generateId());
		
		// Assign square offsets.
		setOffsets(new int[][] {
				new int[] {-1,0}, // The top of the L
				new int[] {0,0}, // The middle of the L
				new int[] {1,0}, // The corner of the L
				new int[] {1,1} // The tail of the L
		}, getCode());
	}
	
	public double getCentreColDiff() {
		return -0.5;
	}

	public static int getCode() {
		return CODE;
	}
}
