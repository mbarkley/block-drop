package demo.client.shared.model;

/*
 * A square-shaped block in Block Drop.
 * 
 * Note: This class overrides BlockModel.rotate and BlockModel.unrotate
 * with empty methods, since the square block has -- effectively -- no rotation.
 */
public class SquareBlockModel extends BlockModel {

  public static final int CODE = 5;

  /*
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

  public double getCentreRowDiff() {
    return -0.5;
  }

  public double getCentreColDiff() {
    return -0.5;
  }

  /*
   * (non-Javadoc)
   * 
   * @see demo.client.shared.BlockModel#rotate()
   */
  @Override
  public void rotate() {
    // Do nothing.
    return;
  }

  public void unrotate() {
    // Do nothing.
    return;
  }

  public static int getCode() {
    return CODE;
  }
}
