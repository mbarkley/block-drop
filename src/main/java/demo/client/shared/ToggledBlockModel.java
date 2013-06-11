package demo.client.shared;

/*
 * This class overrides the rotate method of BlockModel for shapes which do not
 * rotate repeatedly by 90-degrees, but rather rotate by 90 and then -90 degrees
 * in an alternating fashion.
 */
public class ToggledBlockModel extends BlockModel {

	private boolean isRotated = false;
	
	public ToggledBlockModel(int id) {
		// Assign unique id.
		super(id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see demo.client.shared.BlockModel#rotate()
	 */
	@Override
	public void rotate() {
		if (isRotated) {
			super.unrotate();
			isRotated = false;
		} else {
			super.rotate();
			isRotated = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see demo.client.shared.BlockModel#unrotate()
	 */
	@Override
	public void unrotate() {
		this.rotate();
	}
}
