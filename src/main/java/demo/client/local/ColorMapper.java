package demo.client.local;

import java.util.HashMap;
import java.util.Map;

import demo.client.shared.model.BlockModel;
import demo.client.shared.model.LBlockModel;
import demo.client.shared.model.LongBlockModel;
import demo.client.shared.model.ReverseLBlockModel;
import demo.client.shared.model.SBlockModel;
import demo.client.shared.model.SquareBlockModel;
import demo.client.shared.model.TBlockModel;
import demo.client.shared.model.ZBlockModel;

public class ColorMapper {

  private static final String BASIC_COLOUR = "white";

  private static final String L_BLOCK_COLOUR = "blue";

  private static final String LONG_BLOCK_COLOUR = "green";

  private static final String REVERSE_L_BLOCK_COLOUR = "yellow";

  private static final String S_BLOCK_COLOUR = "purple";

  private static final String SQUARE_BLOCK_COLOUR = "orange";

  private static final String T_BLOCK_COLOUR = "red";

  private static final String Z_BLOCK_COLOUR = "brown";

  private static Map<Integer, String> colourMap;

  static {
    colourMap = new HashMap<Integer, String>();

    colourMap.put(BlockModel.getCode(), BASIC_COLOUR);
    colourMap.put(LBlockModel.getCode(), L_BLOCK_COLOUR);
    colourMap.put(LongBlockModel.getCode(), LONG_BLOCK_COLOUR);
    colourMap.put(ReverseLBlockModel.getCode(), REVERSE_L_BLOCK_COLOUR);
    colourMap.put(SBlockModel.getCode(), S_BLOCK_COLOUR);
    colourMap.put(SquareBlockModel.getCode(), SQUARE_BLOCK_COLOUR);
    colourMap.put(TBlockModel.getCode(), T_BLOCK_COLOUR);
    colourMap.put(ZBlockModel.getCode(), Z_BLOCK_COLOUR);
  }

  public static String codeToColour(int code) {
    return colourMap.get(code);
  }

}
