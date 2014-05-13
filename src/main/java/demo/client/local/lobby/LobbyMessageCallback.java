package demo.client.local.lobby;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.protocols.MessageParts;

import com.google.gwt.user.client.Window;

import demo.client.local.Client;
import demo.client.shared.lobby.Invitation;
import demo.client.shared.message.Command;
import demo.client.shared.meta.GameRoom;

/**
 * A class for handling lobby updates and invitations from the server.
 */
public class LobbyMessageCallback implements MessageCallback {

  /* For sending messages to the server. */
  private RequestDispatcher dispatcher = ErraiBus.getDispatcher();

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

  /**
   * Start a game with another player after a successfully accepted invitation.
   */
  private void startGameCallback(GameRoom room) {
    Client.getInstance().setGameRoom(room);
    Lobby.getInstance().goToBoard();
  }

  /**
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