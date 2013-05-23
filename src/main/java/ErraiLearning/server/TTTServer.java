package ErraiLearning.server;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import ErraiLearning.client.shared.LobbyRequest;
import ErraiLearning.client.shared.Player;
import ErraiLearning.client.shared.TTTGame;

@ApplicationScoped
public class TTTServer {

	private Map<Integer,TTTGame> games = new HashMap<Integer,TTTGame>();
	private Map<Integer,Player> players = new HashMap<Integer,Player>();
	private int curId = 1;
	
	@Inject
	private Event<Player> lobbyRegister;
	
	public TTTServer() {
		System.out.println("TTTServer object is constructed.");
	}
	
	public void registerUser(@Observes LobbyRequest request) {
		System.out.println("Registering user...");
	}
	
	public synchronized int getCurId() {
		return ++curId;
	}
	
	public void registerPlayer(@Observes LobbyRequest request) {
		int id = getCurId();
		Player player = new Player(id, request.getNickname());
		
		players.put(id, player);
		System.out.println("Player registered.");
		
		lobbyRegister.fire(player);
	}
}
