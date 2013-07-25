package demo.client.shared.message;

/**
 * Commands used for messaging between client and server components.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public enum Command {
  /**
   * For receiving or relaying an invitation.
   */
  INVITATION,
  /**
   * For notifying clients and the server that a player has joined a game.
   */
  JOIN_GAME,
  /**
   * For notifying clients and the server that a player is leaving a game.
   */
  LEAVE_GAME,
  /**
   * For sending, relaying, and receiving score updates. Score updates may also contain targetted
   * players who will be sabotaged. A sabotaged player will have a row of blocks added to the bottom
   * of their board.
   */
  UPDATE_SCORE,
  /**
   * For sending, relaying, and receiving move updates. Move updates are used to view opponents
   * boards remotely.
   */
  MOVE_UPDATE,
  /**
   * For notifying the {@link demo.client.local.game.controllers.SecondaryDisplayControllerImpl
   * SecondaryDisplayController} to switch which opponent is being watched/targeted. Targeted
   * players will be sabotaged when the targeting player scores.
   */
  SWITCH_OPPONENT,
  /**
   * Used to let the server know that a user is still in the lobby. Must be sent periodically or
   * else the the user will be kicked.
   */
  LOBBY_KEEP_ALIVE,
  /**
   * Used to let the server know that a user is still in the game while they are paused, or
   * otherwise not actively playing. Inactive players will be kicked from games if they do not
   * periodically send this.
   */
  GAME_KEEP_ALIVE
}
