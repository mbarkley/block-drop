package demo.client.local.game;


class Size {
  
  /* The height of the board in squares. */
  static final int HEIGHT = 15;
  /* The width of the board in squares. */
  static final int WIDTH = 10;
  /* Canvas coordinate-space height (in pixels). */
  static final int MAIN_COORD_HEIGHT = 780;
  /* Canvas coordinate-space width (in pixels). */
  static final int MAIN_COORD_WIDTH = 520;
  /* The width and height of each square in this block (in pixels). */
  static final int BLOCK_SIZE = MAIN_COORD_HEIGHT / HEIGHT;
  static final int NEXT_COORD_HEIGHT = BLOCK_SIZE * 5;
  static final int NEXT_COORD_WIDTH = BLOCK_SIZE * 5;
  
  static {
    sanityCheck();
  }
  
  private static void sanityCheck() {
    // Dimensions in tiles divide dimensions in pixels.
    assert MAIN_COORD_HEIGHT % HEIGHT == 0 && MAIN_COORD_WIDTH % WIDTH == 0;
    // Block size should divide height and width HEIGHT and WIDTH times, respectively
    assert MAIN_COORD_HEIGHT / HEIGHT == BLOCK_SIZE && MAIN_COORD_WIDTH / WIDTH == BLOCK_SIZE;
  }

}
