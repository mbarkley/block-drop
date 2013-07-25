package demo.client.local.game.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * An object containing dimensions of components in Block Drop, including {@link Square squares},
 * {@link BoardCanvas board canvases}, and {@link BoardModel board models}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class Size {

  /**
   * The enumerated size categories, used to retrieve an appropriate {@link Size size} instance from
   * {@link Size#getSize(SizeCategory) getSize}.
   * 
   * @author mbarkley <mbarkley@redhat.com>
   * 
   */
  public enum SizeCategory {
    /**
     * The main canvases size category.
     */
    MAIN,
    /**
     * The upcoming piece display canvases size category.
     */
    NEXT,
    /**
     * The opponent canvas size category.
     */
    OPPONENT
  }

  /** The height of the board in squares. */
  public static final int HEIGHT = 15;
  /** The width of the board in squares. */
  public static final int WIDTH = 10;
  /** Canvas coordinate-space height (in pixels). */
  public static final int MAIN_COORD_HEIGHT = 780;
  /** Canvas coordinate-space width (in pixels). */
  public static final int MAIN_COORD_WIDTH = 520;
  /** The width and height of each square in this block (in pixels). */
  public static final int MAIN_BLOCK_SIZE = MAIN_COORD_HEIGHT / HEIGHT;
  /** The height of the opponent canvas (in pixels). */
  public static final int OPP_COORD_HEIGHT = 390;
  /** The width of the opponent canvas (in pixels). */
  public static final int OPP_COORD_WIDTH = 260;
  /** The height of the upcoming piece canvas (in pixels). */
  public static final int NEXT_COORD_HEIGHT = MAIN_BLOCK_SIZE * 5;
  /** The width of the upcoming piece canvas (in pixels). */
  public static final int NEXT_COORD_WIDTH = MAIN_BLOCK_SIZE * 5;

  private static Map<SizeCategory, Size> cachedSizes = new HashMap<SizeCategory, Size>();

  static {
    sanityCheck();
  }

  // Perform sanity checks to make sure size values have been set reasonably.
  private static void sanityCheck() {
    // Dimensions in tiles divide dimensions in pixels.
    assert MAIN_COORD_HEIGHT % HEIGHT == 0 && MAIN_COORD_WIDTH % WIDTH == 0;
    // Block size should divide height and width HEIGHT and WIDTH times, respectively
    assert MAIN_COORD_HEIGHT / HEIGHT == MAIN_BLOCK_SIZE && MAIN_COORD_WIDTH / WIDTH == MAIN_BLOCK_SIZE;
  }

  /**
   * Get a Size instance with constant size values of the appropriate values for the specified
   * category.
   * 
   * @param category
   *          Used to determine the constant values assigned to the returned Size instance.
   * @return A Size instance.
   */
  public static Size getSize(SizeCategory category) {
    if (!cachedSizes.containsKey(category)) {
      cachedSizes.put(category, new Size(category));
    }
    return cachedSizes.get(category);
  }

  /**
   * The height of a canvas of this instance's {@link Size.SizeCategory size category}.
   */
  public final int COORD_HEIGHT;
  /**
   * The width of a canvas of this instance's {@link Size.SizeCategory size category}.
   */
  public final int COORD_WIDTH;
  /**
   * The width of a block of this instance's {@link Size.SizeCategory size category}.
   */
  public final int BLOCK_SIZE;

  private Size(SizeCategory category) {
    switch (category) {
    case MAIN:
      COORD_HEIGHT = MAIN_COORD_HEIGHT;
      COORD_WIDTH = MAIN_COORD_WIDTH;
      BLOCK_SIZE = MAIN_BLOCK_SIZE;
      break;
    case NEXT:
      COORD_HEIGHT = NEXT_COORD_HEIGHT;
      COORD_WIDTH = NEXT_COORD_WIDTH;
      BLOCK_SIZE = MAIN_BLOCK_SIZE;
      break;
    case OPPONENT:
      COORD_HEIGHT = OPP_COORD_HEIGHT;
      COORD_WIDTH = OPP_COORD_WIDTH;
      BLOCK_SIZE = OPP_COORD_HEIGHT / HEIGHT;
      break;
    default:
      COORD_HEIGHT = 0;
      COORD_WIDTH = 0;
      BLOCK_SIZE = 0;
      break;
    }
  }

}
