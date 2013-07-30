package demo.client.local.game.gui;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

import demo.client.local.game.tools.Size;
import demo.client.local.game.tools.Size.SizeCategory;
import demo.client.shared.game.model.BlockModel;
import demo.client.shared.game.model.SquareModel;

/**
 * A simple wrapper for a HTML5 canvas implementing the ControllableBoardDisplay interface.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class BoardCanvas implements ControllableBoardDisplay {

  private Canvas canvas;

  private SizeCategory sizeCategory;

  public BoardCanvas(Canvas canvas, SizeCategory sizeCategory) {
    this.canvas = canvas;
    this.sizeCategory = sizeCategory;
  }

  @Override
  public void undrawBlock(double x, double y, Block activeBlock) {
    BlockModel model = activeBlock.getModel();
    for (SquareModel square : model.getIterator()) {
      double xOffset = x + Block.indexToCoord(square.getCol(), sizeCategory);
      double yOffset = y + Block.indexToCoord(square.getRow(), sizeCategory);
      canvas.getContext2d().clearRect(xOffset, yOffset, Size.getSize(sizeCategory).getBlockSize(),
              Size.getSize(sizeCategory).getBlockSize());
    }
  }

  @Override
  public void drawBlock(double x, double y, Block activeBlock) {
    activeBlock.draw(x, y, canvas.getContext2d());
  }

  @Override
  public void pause() {
    Context2d context = canvas.getContext2d();
    context.save();
    context.setFont("bold 20px sans-serif");
    context.setFillStyle("white");
    context.setTextAlign("center");
    context.fillText("Paused", canvas.getCoordinateSpaceWidth() / 2, canvas.getCoordinateSpaceHeight() / 3);
  }

  @Override
  public void unpause() {
    canvas.getContext2d().restore();
  }

  @Override
  public void clearBoard() {
    canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
  }

  @Override
  public SizeCategory getSizeCategory() {
    return sizeCategory;
  }

  @Override
  public void gameOver() {
    // TODO: Show something on canvas
  }

}
