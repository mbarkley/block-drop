package demo.server;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Service;

import demo.client.shared.Command;
import demo.client.shared.ExitMessage;
import demo.client.shared.GameRoom;
import demo.client.shared.Invitation;
import demo.client.shared.LobbyUpdate;
import demo.client.shared.LobbyUpdateRequest;
import demo.client.shared.Player;
import demo.client.shared.RegisterRequest;
import demo.client.shared.ScoreEvent;
import demo.client.shared.ScoreTracker;
import demo.client.shared.model.MoveEvent;

/*
 * A class for facilitating games between clients over a network.
 * 
 * This class responds to fires events to and catches events from clients, maintains
 * the list of games and players in the lobby, and also uses a message bus to act as
 * a relay between clients.
 */
@ApplicationScoped
@Service("Relay")
public class Server implements MessageCallback {

  /* A map of game ids to games that are currently in progress. */
  private Map<Integer, GameRoom> games = new HashMap<Integer, GameRoom>();
  /* A map of player ids to players that are currently in the lobby. */
  private Map<Integer, Player> players = new HashMap<Integer, Player>();
  /* This value is incremented to assign unique player ids. */
  private int curPlayerId = 1;
  /* This value is incremented to assign unique game ids. */
  private int curGameId = 1;

  /* Used for receiving messages from clients. */
  @Inject
  private MessageBus messageBus;
  /* Used for sending message to clients. */
  @Inject
  private RequestDispatcher dispatcher;
  /* Used for sending clients their registered player information. */
  @Inject
  private Event<Player> playerRegistration;
  /* Used for sending lobby updates to clients. */
  @Inject
  private Event<LobbyUpdate> lobbyUpdate;

  /* For debugging only. */
  private int debugId;
  /* For debugging only. */
  private static int curDebugId = 1;

  /* For debugging only. */
  private static synchronized int nextDebugId() {
    return curDebugId++;
  }

  /*
   * Create a Server for hosting the lobby and tic-tac-toe games. This object is meant to be used as
   * a singleton.
   */
  public Server() {
    debugId = nextDebugId();
    System.out.println("Server" + debugId + ": Server object is constructed.");
  }

  /*
   * Get a unique id for a player.
   * 
   * @return A positive integer that is unique to a player.
   */
  public synchronized int nextPlayerId() {
    return curPlayerId++;
  }

  /*
   * Get a unique id for a game.
   * 
   * @return A positive integer that is unique to a game.
   */
  public synchronized int nextGameId() {
    return curGameId++;
  }

  /*
   * Register a player by assigning them an id and then fire this information back to the client.
   * 
   * @param request The request containing the Player object to be registered.
   */
  public void registerPlayer(@Observes RegisterRequest request) {
    Player player = request.getPlayer();
    // If the player has not been registered, give them an id.
    if (!request.hasRegistered()) {
      player.setId(nextPlayerId());
    }
    // Otherwise they are likely going from a game to the lobby, so just reset their game id.
    else {
      player.setGameId(0);
    }

    // Put the player in the lobby if not already.
    if (!players.containsKey(player.getId())) {
      players.put(player.getId(), player);
    }

    // For debugging.
    System.out.println("Server" + debugId + ": Player registered.");

    // Send the registered player back to the client.
    playerRegistration.fire(player);
  }

  /*
   * Respond to a lobbyUpdateRequest by sending a copy of the lobby to the client. This method
   * should only be invoked by the Errai Framework in response to a Event<LobbyUpdateRequest> fired
   * from a client.
   */
  public void handleLobbyUpdateRequest(@Observes LobbyUpdateRequest lobbyUpdateRequest) {
    sendLobbyList();
  }

  /*
   * Respond to an invitation by relaying it to the appropriate client. This method should only be
   * invoked by the Errai Framework in response to an Event<Invitation> fired from a client.
   */
  public void handleInvitation(@Observes Invitation invitation) {
    // For debugging.
    System.out.println("Server" + debugId + ": Invitation received from " + invitation.getHost().getName());

    // Make game room
    GameRoom room = new GameRoom();
    room.setId(nextGameId());
    games.put(room.getId(), room);
    invitation.setGameId(room.getId());
    addPlayerToGame(invitation.getHost(), room.getId());

    for (Player guest : invitation.getGuests()) {
      System.out.println("Server" + debugId + ": Attempting to relay invitation to " + "Client" + guest.getId());
      // Relay invitation to appropriate client.
      Invitation targetedInvite = new Invitation(invitation, guest);
      MessageBuilder.createMessage().toSubject("Client" + guest.getId()).command(Command.INVITATION)
              .withValue(targetedInvite).noErrorHandling().sendNowWith(dispatcher);
    }

    sendLobbyList();
  }

  private void addPlayerToGame(Player player, int gameId) {
    players.remove(player.getId());
    games.get(gameId).addPlayer(player);
    MessageBuilder.createMessage().toSubject("Client" + player.getId()).command(Command.JOIN_GAME)
            .withValue(games.get(gameId)).noErrorHandling().sendNowWith(dispatcher);
    ScoreTracker scoreTracker = games.get(gameId).getScoreTracker(player);
    updateScoreLocal(scoreTracker);
    updateScoreRemote(new ScoreEvent(scoreTracker));
  }

  /*
   * Fire an Event<LobbyUpdate> to connected clients. This should result in clients refreshing their
   * lobby lists.
   */
  public void sendLobbyList() {
    lobbyUpdate.fire(new LobbyUpdate(players, games));
    // For debugging.
    System.out.println("Server" + debugId + ": Lobby list sent.");
  }

  /*
   * @see
   * org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client
   * .api.messaging.Message)
   */
  @Override
  public void callback(Message message) {
    switch (Command.valueOf(message.getCommandType())) {
    case JOIN_GAME:
      Invitation invitation = message.getValue(Invitation.class);
      addPlayerToGame(invitation.getTarget(), invitation.getGameId());
      sendLobbyList();
      break;
    case LEAVE_GAME:
      ExitMessage exitMessage = message.getValue(ExitMessage.class);
      removePlayerFromGame(exitMessage.getPlayer(), exitMessage.getGame().getId());
      // Clean up empty games.
      if (games.get(exitMessage.getGame().getId()).isEmpty())
        games.remove(exitMessage.getGame().getId());
      sendLobbyList();
      break;
    case UPDATE_SCORE:
      ScoreEvent scoreEvent = message.getValue(ScoreEvent.class);
      updateScoreLocal(scoreEvent.getScoreTracker());
      updateScoreRemote(scoreEvent);
      break;
    case MOVE_UPDATE:
      System.out.println("move update received");
      MoveEvent moveEvent = message.getValue(MoveEvent.class);
      broadcastMove(moveEvent);
      System.out.println("move update relayed");
      break;
    case INVITATION:
      break;
    default:
      break;
    }
  }

  private void broadcastMove(MoveEvent moveEvent) {
    MessageBuilder.createMessage("Game" + moveEvent.getGameId()).command(Command.MOVE_UPDATE).withValue(moveEvent)
            .noErrorHandling().sendNowWith(messageBus);
  }

  private void updateScoreRemote(ScoreEvent event) {
    MessageBuilder.createMessage("Game" + event.getScoreTracker().getGameId()).command(Command.UPDATE_SCORE)
            .withValue(event).noErrorHandling().sendNowWith(dispatcher);
  }

  private void updateScoreLocal(ScoreTracker value) {
    GameRoom room = games.get(value.getGameId());
    room.updateScoreTracker(value);
  }

  private void removePlayerFromGame(Player player, int gameId) {
    games.get(gameId).removePlayer(player.getId());
  }
}
