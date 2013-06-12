package demo.client.shared.model;

/*
 * An S-Shaped block in Block Drop.
 */
public class SBlockModel extends ToggledBlockModel {

	public static final int CODE = 4;
	
	/*
	 * Create an SBlockModel instance.
	 */
	public SBlockModel() {
		// Assign unique id.
		super(generateId());
		
		setOffsets(new int[][] {
				new int[] {0,0},	// Anchor of block.
				new int[] {1,0},	// Bottom square.
				new int[] {0,-1},	// Left-middle of square.
				new int[] {-1,-1}	// Top of square.
		}, getCode());
	}
	
	public double getCentreColDiff() {
		return 0.5;
	}
	
	public static int getCode() {
		return CODE;
	}
}
