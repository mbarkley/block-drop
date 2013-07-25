package demo.client.shared.game.model;

/**
 * A square-shaped block in Block Drop.
 * 
 * Note: This class overrides BlockModel.rotate and BlockModel.unrotate with empty methods, since
 * the square block has -- effectively -- no rotation.
 */
public class SquareBlockModel extends BlockModel {

  public static final int CODE = 5;

  /**
   * Create a SquareBlock instance.
   */
  public SquareBlockModel() {
    // Assign unique id.
    super(generateId());

    setOffsets(new int[][] { new int[] { 0, 0 }, // Top-left square.
        new int[] { 1, 0 }, // Bottom-left square.
        new int[] { 1, 1 }, // Bottom-right square.
        new int[] { 0, 1 } // Top-right square.
            }, getCode());
  }

  @Override
  public double getCentreRowDiff() {
    return -0.5;
  }

  @Override
  public double getCentreColDiff() {
    return -0.5;
  }

  @Override
  public void rotate() {
    // Do nothing.
    return;
  }

  @Override
  public void unrotate() {
    // Do nothing.
    return;
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
