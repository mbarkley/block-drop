package demo.client.shared.game.model;

/**
 * This class overrides the rotate method of BlockModel for shapes which do not rotate repeatedly by
 * 90-degrees, but rather rotate by 90 and then -90 degrees in an alternating fashion.
 */
public class ToggledBlockModel extends BlockModel {

  private boolean isRotated = false;

  /**
   * Get a ToggledBlockModel instance. This constructor merely invokes the
   * {@linkplain BlockModel#BlockModel(int) BlockModel constructor} so that subclasses of this class
   * may still access it.
   * 
   * @param id
   *          A unique id provided by {@link BlockModel#generateId()}.
   */
  public ToggledBlockModel(int id) {
    // Assign unique id.
    super(id);
  }

  @Override
  public void rotate() {
    if (isRotated) {
      super.unrotate();
      isRotated = false;
    }
    else {
      super.rotate();
      isRotated = true;
    }
  }

  @Override
  public void unrotate() {
    this.rotate();
  }
}
