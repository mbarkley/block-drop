package demo.client.shared.model;

/*
 * A T-shaped block in Block Drop.
 */
public class TBlockModel extends BlockModel {

	/*
	 * Create a TBlockModel instance.
	 */
	public TBlockModel() {
		// Assign unique id.
		super(generateId());
		
		// Block starts as upside down T.
		setOffsets(new int[][] {
			new int[] {0,0}, 	// Centre of T
			new int[] {-1,0}, 	// Stem of T
			new int[] {0,-1}, 	// Right-top of T
			new int[] {0,1} 	// Left top of T
		});
	}
}
