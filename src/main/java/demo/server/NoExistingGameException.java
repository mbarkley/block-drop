package demo.server;

/**
 * An exception thrown if a move is sent to the server with an unrecognized game id.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@SuppressWarnings("serial")
public class NoExistingGameException extends Exception {
}
