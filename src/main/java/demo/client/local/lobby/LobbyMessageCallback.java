package demo.client.local.lobby;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.protocols.MessageParts;

import com.google.gwt.user.client.Window;

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
      if (message.getCommandType().equals("invitation"))
        invitationCallback(message);
      else if (message.getCommandType().equals("start-game"))
        startGameCallback(message);
    }

    /*
     * Start a game with another player after a successfully accepted invitation.
     */
    private void startGameCallback(Message message) {
      Client.getInstance().setGame(message.get(GameRoom.class, MessageParts.Value));
      // For debugging.
      System.out.println(Client.getInstance().getNickname() + ": Before board transition.");
      Lobby.getInstance().goToBoard();
      // For debugging.
      System.out.println(Client.getInstance().getNickname() + ": After board transition.");
    }

    /*
     * Prompt the user to respond to an invitation from another client.
     */
    private void invitationCallback(Message message) {
      Invitation invitation = message.get(Invitation.class, MessageParts.Value);

      invitation.setAccepted(Window.confirm("You have been invited to play a game by "
              + invitation.getInviter().getNick() + ". Would you like to accept?"));

      // The server handles the invitation, whether or not it was accepted.
      MessageBuilder.createMessage().toSubject("Relay").command("invitation-response").withValue(invitation)
              .noErrorHandling().sendNowWith(dispatcher);
    }
  }