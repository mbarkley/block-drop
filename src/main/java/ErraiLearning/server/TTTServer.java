package ErraiLearning.server;

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
import org.jboss.errai.bus.server.annotations.Command;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.common.client.protocols.MessageParts;

import ErraiLearning.client.shared.Game;
import ErraiLearning.client.shared.InvalidMoveException;
import ErraiLearning.client.shared.Invitation;
import ErraiLearning.client.shared.LobbyUpdate;
import ErraiLearning.client.shared.LobbyUpdateRequest;
import ErraiLearning.client.shared.Move;
import ErraiLearning.client.shared.Player;
import ErraiLearning.client.shared.RegisterRequest;

/*
 * A class for facilitating tic-tac-toe games between clients over a network.
 * 
 * This class responds to fires events to and catches events from clients, maintains
 * the list of games and players in the lobby, and also uses a message bus to act as
 * a relay between clients.
 */
@ApplicationScoped
@Service("Relay")
public class TTTServer implements MessageCallback {

	/* A map of game ids to games that are currently in progress. */
	private Map<Integer,Game> games = new HashMap<Integer,Game>();
	/* A map of player ids to players that are currently in the lobby. */
	private Map<Integer,Player> players = new HashMap<Integer,Player>();
	/* This value is incremented to assign unique player ids. */
	private int curPlayerId = 1;
	/* This value is incremented to assign unique game ids. */
	private int curGameId = 1;
	
	/* Used for receiving messages from clients. */
	@Inject private MessageBus messageBus;
	/* Used for sending message to clients. */
	@Inject private RequestDispatcher dispatcher;
	/* Used for sending clients their registered player information. */
	@Inject	private Event<Player> playerRegistration;
	/* Used for sending lobby updates to clients. */
	@Inject	private Event<LobbyUpdate> lobbyUpdate;
	
	/* For debugging only. */
	private int debugId;
	/* For debugging only. */
	private static int curDebugId = 1;
	/* For debugging only. */
	private static synchronized int nextDebugId() { return curDebugId++; }
	
	/*
	 * Create a TTTServer for hosting the lobby and tic-tac-toe games. This object
	 * is meant to be used as a singleton.
	 */
	public TTTServer() {
		debugId = nextDebugId();
		System.out.println("Server" + debugId + ": TTTServer object is constructed.");
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
		// Otherwise they are likely going from a game to the lobby, so just reset their game id.
		} else {
			player.setGameId(0);
		}
		
		// Put the player in the lobby.
		players.put(player.getId(), player);
		
		// For debugging.
		System.out.println("Server" + debugId + ": Player registered.");
		
		// Send the registered player back to the client.
		playerRegistration.fire(player);
	}
	
	/*
	 * Respond to a lobbyUpdateRequest by sending a copy of the lobby to the client. This method
	 * should only be invoked by the Errai Framework in response to a Event<LobbyUpdateRequest>
	 * fired from a client.
	 */
	public void handleLobbyUpdateRequest(@Observes LobbyUpdateRequest lobbyUpdateRequest) {
		sendLobbyList();
	}
	
	/*
	 * Respond to an invitation by relaying it to the appropriate client. This method should only
	 * be invoked by the Errai Framework in response to an Event<Invitation> fired from a client.
	 */
	public void handleInvitation(@Observes Invitation invitation) {
		// For debugging.
		System.out.println("Server"+debugId+": Invitation received from " 
				+ invitation.getInviter().getNick() 
				+" to " + invitation.getInvitee().getNick()
		);
		System.out.println("Server"+debugId+": Attempting to relay invitation to "
				+"Client"+invitation.getInvitee().getId());
		
		// Relay invitation to appropriate client.
		MessageBuilder.createMessage()
		.toSubject("Client"+invitation.getInvitee().getId())
		.command("invitation")
		.withValue(invitation)
		.noErrorHandling()
		.sendNowWith(dispatcher);
	}

	/*
	 * Fire an Event<LobbyUpdate> to connected clients. This should result in clients refreshing
	 * their lobby lists.
	 */
	public void sendLobbyList() {
		lobbyUpdate.fire(new LobbyUpdate(players, games));
		// For debugging.
		System.out.println("Server"+debugId+": Lobby list sent.");
	}

	/*
	 * This method only exists so that TTTClient can implement the MessageCallback interface.
	 * All callbacks should be handled by an another annotated method.
	 *
	 * (non-Javadoc)
	 * @see org.jboss.errai.bus.client.api.messaging.MessageCallback#callback(org.jboss.errai.bus.client.api.messaging.Message)
	 */
	@Override
	public void callback(Message message) {}

	/*
	 * Relay a response to an invitation to the inviting client. This method should only be invoked by
	 * the Errai Framework.
	 */
	@Command("invitation-response")
	public void invitationRelayCallback(Message message) {
		Invitation invitation = message.get(Invitation.class, MessageParts.Value);

		// For debugging.
		System.out.println("Server"+debugId+": Relaying invitation response from "+invitation.getInvitee().getNick());
		
		if (invitation.isAccepted()) {
			startGame(invitation);
		}
	}
	
	/*
	 * Validate a move submitted from a user. If the move is valid, publish it and transmit the validated
	 * move to both the clients. If the move is invalid, transmit the unvalidated move back to the clients.
	 * 
	 * This method should only be invoked by the Errai Framework.
	 */
	@Command("publish-move")
	public void publishMoveCallback(Message message) {
		Move move = message.get(Move.class, MessageParts.Value);
		
		// For debugging.
		System.out.println("Server"+debugId+": Relaying move from Client"+move.getPlayerId()
				+" to Game"+move.getGameId());
	
		// Postcondition: move.isValidated() == true iff the server accepts the move.
		recordMove(move);
		
		String command;
		// Check if game is over
		if (move.isValidated() && games.get(move.getGameId()).isOver())
			command = "game-over";
		else
			command = "validate-move";
		
		// Send the message back to both players.
		MessageBuilder.createMessage()
		.toSubject("Game"+move.getGameId())
		.command(command)
		.withValue(move)
		.noErrorHandling()
		.sendNowWith(dispatcher);
	}

	/*
	 * Attempt to make move in Game object. If this is successful, the move will be validated so that
	 * move.isValidated() will return true and the game to which the move belongs will reflect the
	 * completion of this move.
	 */
	private void recordMove(Move move) {
		Game game = games.get(move.getGameId());
		
		try {
			if (game == null)
				throw new NoExistingGameException();
			
			game.makeMove(move.getPlayerId(), move.getRow(), move.getCol());
			
		} catch (InvalidMoveException e) {
			//TODO: Handle error where move is somehow invalid.
			return;
		} catch (NoExistingGameException e) {
			//TODO: Handle error where no game exists.
			return;
		}
		
		// Since there were no errors, finish validating the move.
		move.setValidated(true);
		game.validateLastMove(move);
	}

	/*
	 * Start a game of tic-tac-toe between two players referenced in invitation.
	 * 
	 * @param invitation An accepted invitation between two players in this server's lobby.
	 */
	public void startGame(Invitation invitation) {
		// For debugging.
		System.out.println("Server"+debugId+": Begin initiating game.");
		
		Game game = new Game(nextGameId(), invitation.getInviter(), invitation.getInvitee());
		
		// Add game to server game list
		games.put(game.getGameId(), game);
		
		// Remove players from lobby
		players.remove(game.getPlayerX().getId());
		players.remove(game.getPlayerO().getId());
		
		// Update lobby for other players.
		lobbyUpdate.fire(new LobbyUpdate(players, games));
		
		// Message players to start game.
		MessageBuilder.createMessage()
		.toSubject("Client"+game.getPlayerX().getId())
		.command("start-game")
		.withValue(game)
		.noErrorHandling()
		.sendNowWith(dispatcher);
		
		MessageBuilder.createMessage()
		.toSubject("Client"+game.getPlayerO().getId())
		.command("start-game")
		.withValue(game)
		.noErrorHandling()
		.sendNowWith(dispatcher);
		
		// Subscribe to service that clients of this game will use to broadcast moves.
		messageBus.subscribe("Game"+game.getGameId(), this);
		
		// For debugging.
		System.out.println("Server"+debugId+": Game initiated.");
	}
}
