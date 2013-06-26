package demo.client.local.lobby;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.protocols.MessageParts;

import com.google.gwt.user.client.Window;

import demo.client.shared.Command;
import demo.client.shared.GameRoom;
import demo.client.shared.Invitation;

/*
 * A class for handling lobby updates and invitation from the server.
 */
public class LobbyMessageCallback implements MessageCallback {

  /* For sending messages to the server. */
  private RequestDispatcher dispatcher = ErraiBus.getDispatcher();

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client
   * .api.messaging.Message)
   */
  @Override
  public void callback(Message message) {
    switch (Command.valueOf(message.getCommandType())) {
    case INVITATION:
      invitationCallback(message.get(Invitation.class, MessageParts.Value));
      break;
    case JOIN_GAME:
      startGameCallback(message.get(GameRoom.class, MessageParts.Value));
    default:
      break;
    }
  }

  /*
   * Start a game with another player after a successfully accepted invitation.
   */
  private void startGameCallback(GameRoom room) {
    Client.getInstance().setGameRoom(room);
    // For debugging.
    System.out.println(Client.getInstance().getNickname() + ": Before board transition.");
    Lobby.getInstance().goToBoard();
    // For debugging.
    System.out.println(Client.getInstance().getNickname() + ": After board transition.");
  }

  /*
   * Prompt the user to respond to an invitation from another client.
   */
  private void invitationCallback(Invitation invitation) {
    boolean accepted = Window.confirm("You have been invited to play a game by " + invitation.getHost().getName()
            + ". Would you like to accept?");

    if (accepted) {
      // The server handles the invitation, whether or not it was accepted.
      MessageBuilder.createMessage().toSubject("Relay").command(Command.JOIN_GAME).withValue(invitation)
              .noErrorHandling().sendNowWith(dispatcher);
    }
  }
}