package demo.client.shared;

import java.util.ArrayList;
import java.util.List;

/*
 * The base class for a falling block in a Block Drop BoardModel.
 */
public class BlockModel {
	
	public static final int BASIC_CODE = 1;
	
	private static int idGen = 1;

	/* 
	 * An array of pairs. Represents the offset positions of each tile in this block
	 * from the central position.
	 */
	private List<Integer[]> offsets;
	/* A unique id for identifying this block. */
	private int id;
	
	/*
	 * Create a basic BlockModel consisting of one square.
	 */
	public BlockModel() {
		this(generateId());

		// Creates array {{0,0}}, a single square with no offset.
		offsets = new ArrayList<Integer[]>();
		offsets.add(new Integer[] {0,0});
	}
	
	/*
	 * Initializes BlockModel id. All subclasses should invoke this constructor
	 * using the return value of generateId() as the argument.
	 */
	protected BlockModel(int id) {
		this.id = id;
	}
	
	/*
	 * Set the offsets of the squares in this block from the blocks main position.
	 * The position of a single square on the board is calculated as main position + offset.
	 * 
	 * @param offsets An array of integer pairs, representing row and column offsets
	 * from this blocks main position.
	 */
	protected void setOffsets(int[][] offsets) {
		this.offsets = new ArrayList<Integer[]>();
		
		for (int i = 0; i < offsets.length; i++) {
			this.offsets.add(new Integer[] {offsets[i][0], offsets[i][1]});
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other.getClass() == BlockModel.class && this.getId() == ((BlockModel) other).getId();
	}
	
	/*
	 * Get the id of this BlockModel.
	 * 
	 * @return The id of this BlockModel.
	 */
	public int getId() {
		return id;
	}

	protected static int generateId() {
		return idGen++;
	}

	/*
	 * Get the integer representing this type of block on the board.
	 * 
	 * @return An integer representing this type of block on the board.
	 */
	public int getCode() {
		return BASIC_CODE;
	}
	
	public Iterable<Integer[]> getIterator() {
		return offsets;
	}
	
	/*
	 * Rotate the offsets of the squares in this block by 90 degrees clockwise.
	 * Blocks that do not rotate around a central square should override this method.
	 */
	public void rotate() {
		for (Integer[] offset : offsets) {
			// Calculate new offsets (calculation derived from rotation matrix by 90 degrees)
			int newRowOffset = -1 * offset[1];
			int newColOffset = offset[0];
			
			// Assign new offsets.
			offset[0] = newRowOffset;
			offset[1] = newColOffset;
		}
	}
	
	/*
	 * Reverse the effect of a call to this.rotate.
	 */
	public void unrotate() {
		for (Integer[] offset : offsets) {
			// Calculate new offsets (calculation derived from rotation matrix by -90 degrees).
			int newRowOffset = offset[1];
			int newColOffset = -1 * offset[0];
			
			// Assign new offsets.
			offset[0] = newRowOffset;
			offset[1] = newColOffset;
		}
	}
}
