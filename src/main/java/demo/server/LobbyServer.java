package demo.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.server.annotations.Service;

import demo.client.shared.lobby.Invitation;
import demo.client.shared.lobby.LobbyUpdate;
import demo.client.shared.lobby.LobbyUpdateRequest;
import demo.client.shared.lobby.RegisterRequest;
import demo.client.shared.message.Command;
import demo.client.shared.message.ExitMessage;
import demo.client.shared.message.MoveEvent;
import demo.client.shared.message.ScoreEvent;
import demo.client.shared.meta.GameRoom;
import demo.client.shared.meta.Player;
import demo.client.shared.meta.ScoreTracker;

/**
 * A class for facilitating games between clients over a network.
 * 
 * This class responds to, fires events to, and catches events from clients; maintains the list of
 * games and players in the lobby; and also uses a message bus to act as a relay between clients.
 */
@ApplicationScoped
@Service("Relay")
public class LobbyServer implements MessageCallback {

  private static final long LOBBY_TIMEOUT = 20000;
  private static final long GAME_TIMEOUT = 10000;

  /** A map of game ids to games that are currently in progress. */
  private Map<Integer, GameRoom> games = new ConcurrentHashMap<Integer, GameRoom>();

  /** A map of player ids to lobbyPlayers that are currently in the lobby. */
  private Map<Integer, Player> lobbyPlayers = new ConcurrentHashMap<Integer, Player>();

  /** Heart beat timestamps of players in lobby. */
  private Map<Integer, Long> lobbyHeartBeats = new ConcurrentHashMap<Integer, Long>();

  /** Heart beat timestamps of players in games. */
  private Map<Player, Long> gameHeartBeats = new ConcurrentSkipListMap<Player, Long>();

  /** This value is incremented to assign unique player ids. */
  private int curPlayerId = 1;

  /** This value is incremented to assign unique game ids. */
  private int curGameId = 1;

  /** Used for receiving messages from clients. */
  @Inject
  private MessageBus messageBus;

  /** Used for sending clients their registered player information. */
  @Inject
  private Event<Player> playerRegistration;

  /** Used for sending lobby updates to clients. */
  @Inject
  private Event<LobbyUpdate> lobbyUpdate;

  private Timer lobbyTimer;
  private Timer gameTimer;

  @PostConstruct
  private void startTimers() {
    lobbyTimer = new Timer();
    lobbyTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        LobbyServer.this.cleanLobby();
      }
    }, 0, LOBBY_TIMEOUT);

    gameTimer = new Timer();
    gameTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        LobbyServer.this.cleanGameRooms();
      }
    }, 0, GAME_TIMEOUT);
  }

  /**
   * Find and remove idle players from game rooms.
   */
  private void cleanGameRooms() {
    long curTime = System.currentTimeMillis();
    final Iterator<GameRoom> iterator = games.values().iterator();
    boolean modified = false;

    while (iterator.hasNext()) {
      final GameRoom game = iterator.next();
      modified = cleanGameRoom(curTime, game) || modified;
      if (game.isEmpty()) {
        iterator.remove();
        modified = true;
      }
    }
    
    if (modified) {
      sendLobbyList();
    }
  }

  private boolean cleanGameRoom(long curTime, GameRoom game) {
    final Iterator<Player> iterator = game.getPlayers().values().iterator();
    boolean modified = false;
    while (iterator.hasNext()) {
      final Player player = iterator.next();
      long heartBeat = gameHeartBeats.containsKey(player) ? gameHeartBeats.get(player) : System.currentTimeMillis();
      if (curTime - heartBeat > GAME_TIMEOUT) {
        iterator.remove();
        gameHeartBeats.remove(player);
        clearGameId(player);
        broadcastPlayerLeftGame(player, game.getId());
        modified = true;
      }
    }
    
    return modified;
  }

  /**
   * Find and remove idle players from the lobby.
   */
  private void cleanLobby() {
    long curTime = System.currentTimeMillis();
    List<Integer> removed = new ArrayList<Integer>();

    for (Entry<Integer, Long> heartBeat : lobbyHeartBeats.entrySet()) {
      if (curTime - heartBeat.getValue() > LOBBY_TIMEOUT) {
        removed.add(heartBeat.getKey());
      }
    }

    if (!removed.isEmpty()) {
      for (final Integer id : removed) {
        lobbyPlayers.remove(id);
        lobbyHeartBeats.remove(id);
      }
      sendLobbyList();
    }
  }

  /**
   * Get a unique id for a player.
   * 
   * @return A positive integer that is unique to a player.
   */
  private synchronized int nextPlayerId() {
    return curPlayerId++;
  }

  /**
   * Get a unique id for a game.
   * 
   * @return A positive integer that is unique to a game.
   */
  private synchronized int nextGameId() {
    return curGameId++;
  }

  /**
   * Register a player by assigning them an id and then fire this information back to the client.
   * 
   * @param request
   *          The request containing the Player object to be registered.
   */
  public void addPlayerToLobby(@Observes RegisterRequest request) {
    Player player = request.getPlayer();
    if (!request.hasRegistered()) {
      registerPlayer(player);
    }
    else {
      clearGameId(player);
    }

    if (!isPlayerInLobby(player)) {
      addPlayerToLobby(player);
    }

    playerRegistration.fire(player);
  }

  private void addPlayerToLobby(Player player) {
    lobbyPlayers.put(player.getId(), player);
    lobbyHeartBeats.put(player.getId(), System.currentTimeMillis());
  }

  private void clearGameId(Player player) {
    player.setGameId(0);
  }

  private void registerPlayer(Player player) {
    player.setId(nextPlayerId());
  }

  private boolean isPlayerInLobby(Player player) {
    return lobbyPlayers.containsKey(player.getId());
  }

  /**
   * Respond to a lobbyUpdateRequest by sending a copy of the lobby to the client. This method
   * should only be invoked by the Errai Framework in response to a
   * {@code Event<LobbyUpdateRequest>} fired from a client.
   */
  public void handleLobbyUpdateRequest(@Observes LobbyUpdateRequest lobbyUpdateRequest) {
    sendLobbyList();
  }

  /**
   * Respond to an invitation by relaying it to the appropriate client. This method should only be
   * invoked by the Errai Framework in response to an {@code Event<Invitation>} fired from a client.
   */
  public void handleInvitation(@Observes Invitation invitation) {
    // Make game room
    GameRoom room = new GameRoom();
    room.setId(nextGameId());
    games.put(room.getId(), room);
    invitation.setGameId(room.getId());
    addPlayerToGame(invitation.getHost(), room.getId());

    for (Player guest : invitation.getGuests()) {
      // Relay invitation to appropriate client.
      Invitation targetedInvite = new Invitation(invitation, guest);
      MessageBuilder.createMessage().toSubject("Client" + guest.getId()).command(Command.INVITATION)
              .withValue(targetedInvite).noErrorHandling().sendNowWith(messageBus);
    }

    sendLobbyList();
  }

  private void addPlayerToGame(Player player, int gameId) {
    if (games.get(gameId) != null) {
      lobbyPlayers.remove(player.getId());
      lobbyHeartBeats.remove(player.getId());
      games.get(gameId).addPlayer(player);
      gameHeartBeats.put(player, System.currentTimeMillis());
      player.setGameId(gameId);
      MessageBuilder.createMessage().toSubject("Client" + player.getId()).command(Command.JOIN_GAME)
      .withValue(games.get(gameId)).noErrorHandling().sendNowWith(messageBus);
      ScoreTracker scoreTracker = games.get(gameId).getScoreTracker(player);
      updateScoreLocal(scoreTracker);
      updateScoreRemote(new ScoreEvent(scoreTracker));
    }
  }

  /**
   * Fire an {@code Event<LobbyUpdate>} to connected clients. This should result in clients
   * refreshing their lobby lists.
   */
  private void sendLobbyList() {
    lobbyUpdate.fire(new LobbyUpdate(lobbyPlayers, games));
  }

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
      removePlayerFromGameAndSendUpdates(exitMessage.getPlayer(), exitMessage.getGame().getId());
      sendLobbyList();
      break;

    case UPDATE_SCORE:
      ScoreEvent scoreEvent = message.getValue(ScoreEvent.class);
      updateScoreLocal(scoreEvent.getScoreTracker());
      updateScoreRemote(scoreEvent);
      break;

    case MOVE_UPDATE:
      MoveEvent moveEvent = message.getValue(MoveEvent.class);
      updateGameRoomHeartBeat(moveEvent.getPlayer());
      broadcastMove(moveEvent);
      break;

    case GAME_KEEP_ALIVE:
      updateGameRoomHeartBeat(message.getValue(Player.class));
      broadcastPause(message.getValue(Player.class));
      break;

    case LOBBY_KEEP_ALIVE:
      updateLobbyHeartBeat(message.getValue(Player.class));
      break;

    case INVITATION:
    default:
      break;
    }
  }

  private void broadcastPause(Player player) {
    MessageBuilder.createMessage("Game" + player.getGameId()).command(Command.GAME_KEEP_ALIVE).withValue(player)
            .noErrorHandling().sendNowWith(messageBus);
  }

  private void updateGameRoomHeartBeat(Player player) {
    gameHeartBeats.put(player, System.currentTimeMillis());
  }

  private void updateLobbyHeartBeat(Player player) {
    lobbyHeartBeats.put(player.getId(), System.currentTimeMillis());
  }

  private void broadcastMove(MoveEvent moveEvent) {
    MessageBuilder.createMessage("Game" + moveEvent.getGameId()).command(Command.MOVE_UPDATE).withValue(moveEvent)
            .noErrorHandling().sendNowWith(messageBus);
  }

  private void updateScoreRemote(ScoreEvent event) {
    MessageBuilder.createMessage("Game" + event.getScoreTracker().getGameId()).command(Command.UPDATE_SCORE)
            .withValue(event).noErrorHandling().sendNowWith(messageBus);
  }

  private void updateScoreLocal(ScoreTracker value) {
    GameRoom room = games.get(value.getGameId());
    room.updateScoreTracker(value);
  }

  private void removePlayerFromGameAndSendUpdates(Player player, int gameId) {
    final GameRoom gameRoom = games.get(gameId);

    removePlayerFromGameMaps(player, gameRoom);
    clearGameId(player);

    if (gameRoom != null) {
      if (gameRoom.isEmpty()) {
        removeGameAndSendUpdate(gameId);
      }
      else {
        broadcastPlayerLeftGame(player, gameId);
      }
    }
  }

  private void broadcastPlayerLeftGame(Player player, int gameId) {
    MessageBuilder.createMessage("Game" + gameId).command(Command.LEAVE_GAME).withValue(player).noErrorHandling()
            .sendNowWith(messageBus);
  }

  private void removeGameAndSendUpdate(int gameId) {
    games.remove(gameId);
    sendLobbyList();
  }

  private void removePlayerFromGameMaps(Player player, GameRoom gameRoom) {
    if (gameRoom != null) {
      gameRoom.removePlayer(player.getId());
    }
    gameHeartBeats.remove(player);
  }
}
