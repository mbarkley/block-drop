package demo.client.local.game;

import java.util.HashMap;
import java.util.Map;


class Size {
  
  public enum SizeCategory {
    MAIN, NEXT, OPPONENT
  }

  /* The height of the board in squares. */
  static final int HEIGHT = 15;
  /* The width of the board in squares. */
  static final int WIDTH = 10;
  /* Canvas coordinate-space height (in pixels). */
  static final int MAIN_COORD_HEIGHT = 780;
  /* Canvas coordinate-space width (in pixels). */
  static final int MAIN_COORD_WIDTH = 520;
  /* The width and height of each square in this block (in pixels). */
  static final int MAIN_BLOCK_SIZE = MAIN_COORD_HEIGHT / HEIGHT;
  static final int OPP_COORD_HEIGHT = 390;
  static final int OPP_COORD_WIDTH = 260;
  static final int NEXT_COORD_HEIGHT = MAIN_BLOCK_SIZE * 5;
  static final int NEXT_COORD_WIDTH = MAIN_BLOCK_SIZE * 5;
  
  private static Map<SizeCategory, Size> cachedSizes = new HashMap<SizeCategory, Size>();
  
  static {
    sanityCheck();
  }
  
  private static void sanityCheck() {
    // Dimensions in tiles divide dimensions in pixels.
    assert MAIN_COORD_HEIGHT % HEIGHT == 0 && MAIN_COORD_WIDTH % WIDTH == 0;
    // Block size should divide height and width HEIGHT and WIDTH times, respectively
    assert MAIN_COORD_HEIGHT / HEIGHT == MAIN_BLOCK_SIZE && MAIN_COORD_WIDTH / WIDTH == MAIN_BLOCK_SIZE;
  }
  
  public static Size getSize(SizeCategory category) {
    if (!cachedSizes.containsKey(category)) {
      cachedSizes.put(category, new Size(category));
    }
    return cachedSizes.get(category);
  }
  
  final int COORD_HEIGHT;
  final int COORD_WIDTH;
  final int BLOCK_SIZE;
  
  private Size(SizeCategory category) {
    switch(category) {
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
