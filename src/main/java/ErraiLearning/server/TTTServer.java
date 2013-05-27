package ErraiLearning.server;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.bus.client.ErraiBus;
import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.bus.server.annotations.Command;
import org.jboss.errai.bus.server.annotations.Service;
import org.jboss.errai.common.client.protocols.MessageParts;

import com.google.inject.Singleton;

import ErraiLearning.client.shared.Invitation;
import ErraiLearning.client.shared.LobbyUpdate;
import ErraiLearning.client.shared.LobbyUpdateRequest;
import ErraiLearning.client.shared.RegisterRequest;
import ErraiLearning.client.shared.Player;
import ErraiLearning.client.shared.Game;

@ApplicationScoped
@Service("Relay")
public class TTTServer implements MessageCallback {

	private Map<Integer,Game> games = new HashMap<Integer,Game>();
	private Map<Integer,Player> players = new HashMap<Integer,Player>();
	private int curPlayerId = 1;
	private int curGameId = 1;
	
	@Inject private MessageBus messageBus;
	@Inject private RequestDispatcher dispatcher;
	@Inject	private Event<Player> playerRegistration;
	@Inject	private Event<LobbyUpdate> lobbyUpdate;
	
	/* For debugging only. */
	private int debugId;
	private static int curDebugId = 1;
	private static synchronized int nextDebugId() { return curDebugId++; }
	
	public TTTServer() {
		debugId = nextDebugId();
		System.out.println("Server" + debugId + ": TTTServer object is constructed.");
	}
	
	public synchronized int nextPlayerId() {
		return curPlayerId++;
	}
	
	public synchronized int nextGameId() {
		return curGameId++;
	}
	
	public void registerPlayer(@Observes RegisterRequest request) {
		int id = nextPlayerId();
		Player player = new Player(id, request.getNickname(), 0);
		
		players.put(id, player);
		System.out.println("Server" + debugId + ": Player registered.");
		
		playerRegistration.fire(player);
	}
	
	public void handleLobbyUpdateRequest(@Observes LobbyUpdateRequest lobbyUpdateRequest) {
		sendLobbyList();
	}
	
	/*
	 * This event is triggered by an invitation initiation.
	 */
	public void handleInvitation(@Observes Invitation invitation) {
		System.out.println("Server"+debugId+": Invitation received from " 
				+ invitation.getInviter().getNick() 
				+" to " + invitation.getInvitee().getNick()
		);
		System.out.println("Server"+debugId+": Attempting to relay invitation to "
				+"Client"+invitation.getInvitee().getId());
		
		MessageBuilder.createMessage()
		.toSubject("Client"+invitation.getInvitee().getId())
		.command("invitation")
		.withValue(invitation)
		.noErrorHandling()
		.sendNowWith(dispatcher);
	}

	public void sendLobbyList() {
		lobbyUpdate.fire(new LobbyUpdate(players, games));
		System.out.println("Server"+debugId+": Lobby list sent.");
	}

	@Override
	@Command("invitation-response")
	public void callback(Message message) {
		Invitation invitation = message.get(Invitation.class, MessageParts.Value);
	
		System.out.println("Server"+debugId+": Relaying invitation response from "+invitation.getInvitee().getNick());
		
		if (invitation.isAccepted()) {
			startGame(invitation);
		}
	}

	public void startGame(Invitation invitation) {
		System.out.println("Server"+debugId+": Begin initiating game.");
		Game game = new Game(nextGameId(), invitation.getInviter(), invitation.getInvitee());
		
		// Add game to server game list
		games.put(game.getGameId(), game);
		
		// Remove players from lobby
		players.remove(game.getFirstPlayer().getId());
		players.remove(game.getSecondPlayer().getId());
		
		lobbyUpdate.fire(new LobbyUpdate(players, games));
		
		// Message players to start game.
		MessageBuilder.createMessage()
		.toSubject("Client"+game.getFirstPlayer().getId())
		.command("start-game")
		.withValue(game)
		.noErrorHandling()
		.sendNowWith(dispatcher);
		
		MessageBuilder.createMessage()
		.toSubject("Client"+game.getSecondPlayer().getId())
		.command("start-game")
		.withValue(game)
		.noErrorHandling()
		.sendNowWith(dispatcher);
		
		// Subscribe to service that clients of this game will use
		messageBus.subscribe("Game"+game.getGameId(), this);
		
		System.out.println("Server"+debugId+": Game initiated.");
	}
}
