package demo.client.shared.game.model;

/**
 * A model for an L-shaped block model.
 */
public class LBlockModel extends BlockModel {

  public static final int CODE = 8;

  /**
   * Create a LBlockModel instance.
   */
  public LBlockModel() {
    // Assign unique id.
    super(generateId());

    // Assign square offsets.
    setOffsets(new int[][] { new int[] { -1, 0 }, // The top of the L
        new int[] { 0, 0 }, // The middle of the L
        new int[] { 1, 0 }, // The corner of the L
        new int[] { 1, 1 } // The tail of the L
            }, getCode());
  }

  /**
   * Get the code identifying this type of block model.
   * 
   * @return The code identifying this type of block model.
   */
  public static int getCode() {
    return CODE;
  }
}
