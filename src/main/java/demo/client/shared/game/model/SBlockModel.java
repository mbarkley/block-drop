package demo.client.shared.game.model;

/**
 * An S-Shaped block.
 */
public class SBlockModel extends ToggledBlockModel {

  public static final int CODE = 4;

  /**
   * Create an SBlockModel instance.
   */
  public SBlockModel() {
    // Assign unique id.
    super(generateId());

    setOffsets(new int[][] { new int[] { 0, 0 }, // Anchor of block.
        new int[] { 1, 0 }, // Bottom square.
        new int[] { 0, -1 }, // Left-middle of square.
        new int[] { -1, -1 } // Top of square.
            }, getCode());
  }

  @Override
  public double getCentreColDiff() {
    return 0.5;
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
