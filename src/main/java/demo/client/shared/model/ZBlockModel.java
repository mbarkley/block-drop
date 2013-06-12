package demo.client.shared.model;

/*
 * A Z-shaped block in Block Drop.
 */
public class ZBlockModel extends ToggledBlockModel {

	public static final int CODE = 7;
	
	/*
	 * Create a ZBlockModel instance.
	 */
	public ZBlockModel() {
		// Assign unique id.
		super(generateId());
		
		setOffsets(new int[][] {
				new int[] {0,0},	// Anchor of block.
				new int[] {1,0},	// Bottom square.
				new int[] {0,1},	// Middle-right square.
				new int[] {-1,1}	// Top square.
		}, getCode());
	}
	
	public double getCentreColDiff() {
		return -0.5;
	}
	
	public static int getCode() {
		return CODE;
	}
}
