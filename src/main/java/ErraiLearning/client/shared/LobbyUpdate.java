package ErraiLearning.client.shared;

import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class LobbyUpdate {

	private Map<Integer,Player> players = null;
	private Map<Integer,Game> games = null;
	
	public LobbyUpdate() {}
	
	public LobbyUpdate(Map<Integer, Player> players, Map<Integer, Game> games) {
		this.players = players;
		this.games = games;
	}

	public Map<Integer,Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(Map<Integer,Player> players) {
		this.players = players;
	}
	
	public void setGames(Map<Integer,Game> games) {
		this.games = games;
	}
	
	public Map<Integer,Game> getGames() {
		return games;
	}
}
