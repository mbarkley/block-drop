package demo.client.shared;

/*
 * A long straight block in Block Drop.
 */
public class LongBlockModel extends ToggledBlockModel {

	/*
	 * Create a LongBlockModel instance.
	 */
	public LongBlockModel() {
		// Assign unique id.
		super(generateId());
		
		setOffsets(new int[][] {
				new int[] {-1,0},	// Top square.
				new int[] {0,0},	// Anchor of block.
				new int[] {1,0},	// Second-from-bottom square.
				new int[] {2,0}		// Bottom square.
		});
	}
}
