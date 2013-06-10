package demo.client.shared;


/*
 * A model for an L-shaped block in Block Drop.
 */
public class LBlockModel extends BlockModel {

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
		});
	}
}
