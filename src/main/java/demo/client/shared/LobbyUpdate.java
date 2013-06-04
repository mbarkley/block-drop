package demo.client.shared;

import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;

import ErraiLearning.client.shared.Player;

/*
 * A portable JavaBean for transmitting lists of players in lobby and games in progress.
 */
@Portable
public class LobbyUpdate {

	/* A map player ids to Player objects (of players in lobby). */
	private Map<Integer,Player> players = null;
	/* A map of game ids to Game objects (of games in progress). */
	private Map<Integer,GameRoom> games = null;
	
	/*
	 * A default no-arg constructor for automated bean construction. Users should prefer
	 * the constructor taking Map<Integer,Player> and Map<Integer,Game> parameters.
	 */
	public LobbyUpdate() {}
	
	/*
	 * Construct a LobbyUpdate instance with the current players in the lobby and current
	 * games in progress.
	 * 
	 * @param players A map of player ids to players, for all the players in the lobby.
	 * @param games A map of game ids to games, for all the currently in progress games.
	 */
	public LobbyUpdate(Map<Integer, Player> players, Map<Integer, GameRoom> games) {
		this.players = players;
		this.games = games;
	}

	/*
	 * Get a map of the current players in the lobby.
	 * 
	 * @return Return a map of player ids to players for the players currently in the lobby.
	 */
	public Map<Integer,Player> getPlayers() {
		return players;
	}
	
	/*
	 * Set the map of players currently in the lobby.
	 * 
	 * @param players A map of player ids to players for the players currently in the lobby.
	 */
	public void setPlayers(Map<Integer,Player> players) {
		this.players = players;
	}
	
	/*
	 * Set the map of games currently in progress.
	 * 
	 * @param games A map of game ids to games of the games currently in progress.
	 */
	public void setGames(Map<Integer,GameRoom> games) {
		this.games = games;
	}

	/*
	 * Get the map of games currently in progress.
	 * 
	 * @return Return a map of game ids to games of the games currently in progress.
	 */
	public Map<Integer,GameRoom> getGames() {
		return games;
	}
}
