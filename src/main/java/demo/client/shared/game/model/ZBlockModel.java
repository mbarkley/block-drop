package demo.client.shared.game.model;

/**
 * A Z-shaped block.
 */
public class ZBlockModel extends ToggledBlockModel {

  public static final int CODE = 7;

  /**
   * Create a ZBlockModel instance.
   */
  public ZBlockModel() {
    // Assign unique id.
    super(generateId());

    setOffsets(new int[][] { new int[] { 0, 0 }, // Anchor of block.
        new int[] { 1, 0 }, // Bottom square.
        new int[] { 0, 1 }, // Middle-right square.
        new int[] { -1, 1 } // Top square.
            }, getCode());
  }

  @Override
  public double getCentreColDiff() {
    return -0.5;
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
