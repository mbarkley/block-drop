package demo.client.shared;

import java.util.Iterator;

/*
 * The base class for a falling block in a Block Drop BoardModel.
 */
public class BlockModel {
	
	/*
	 * For iterating through the positions of squares in a single block.
	 */
	public class SquareIterator implements Iterator<Integer[]> {

		/* Index of next position to return. */
		private int next;
		
		/*
		 * Create a SquareIterator.
		 * 
		 * @param offsets The offsets of each square to iterate through.
		 */
		public SquareIterator() {
			super();
			next = 0;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return next < offsets.length;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Integer[] next() {
			Integer[] res = new Integer[] {
					new Integer(offsets[next][0]),
					new Integer(offsets[next][1])};
			next++;
			return res;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static final int BASIC_CODE = 1;
	
	private static int idGen = 1;

	/* 
	 * An array of pairs. Represents the offset positions of each tile in this block
	 * from the central position.
	 */
	private int[][] offsets;
	/* A unique id for identifying this block. */
	private int id;
	
	/*
	 * Create a basic BlockModel consisting of one square.
	 */
	public BlockModel() {
		this(generateId());

		// Creates array {{0,0}}, so single square with no offset.
		offsets = new int[1][2];
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
		this.offsets = offsets;
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
		return new Iterable<Integer[]>() {
			@Override
			public Iterator<Integer[]> iterator() {
				return new SquareIterator();
			}
		};
	}
	
	/*
	 * Rotate the offsets of the squares in this block by 90 degrees clockwise.
	 * Blocks that do not rotate around a central square should override this method.
	 */
	public void rotateClockwise() {
		for (int[] offset : offsets) {
			// Calculate new offsets (calculation derived from rotation matrix)
			int newRowOffset = -1 * offset[1];
			int newColOffset = offset[0];
			
			// Assign new offsets.
			offset[0] = newRowOffset;
			offset[1] = newColOffset;
		}
	}
}
