package demo.client.shared.game.model;

/**
 * An Exception thrown when a {@link BlockModel block model}, which is at least partially located
 * above the top of a {@link BoardModel board model}, can no longer move.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@SuppressWarnings("serial")
public class BlockOverflow extends Exception {
}
