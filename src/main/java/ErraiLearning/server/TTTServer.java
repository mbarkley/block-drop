package ErraiLearning.server;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import ErraiLearning.client.shared.Player;
import ErraiLearning.client.shared.TTTGame;

@ApplicationScoped
public class TTTServer {

	private Map<Integer,TTTGame> games = new HashMap<Integer,TTTGame>();
	
	private Map<Integer,Player> players = new HashMap<Integer,Player>();
	
	private int curId = 1;
	
	public TTTServer() {
		System.out.println("TTTServer object is constructed.");
	}
	
}
