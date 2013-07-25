package demo.client.shared.game.model;

/**
 * A model of non-empty squares of non-active blocks on a {@link BoardModel board model}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class BackgroundBlockModel extends BlockModel {

  public BackgroundBlockModel() {
    super(generateId());
  }
}
