package demo.client.shared.game.model;

/**
 * A T-shaped block.
 */
public class TBlockModel extends BlockModel {

  public static final int CODE = 6;

  /**
   * Create a TBlockModel instance.
   */
  public TBlockModel() {
    // Assign unique id.
    super(generateId());

    // Block starts as upside down T.
    setOffsets(new int[][] { new int[] { 0, 0 }, // Centre of T
        new int[] { -1, 0 }, // Stem of T
        new int[] { 0, -1 }, // Right-top of T
        new int[] { 0, 1 } // Left top of T
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
