package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.enterprise.client.cdi.api.Conversational;

@Portable
@Conversational
public class Player {

	private int id;
	private String nick;
	private int gameId;
	
	public Player() {
		this(0, "user", 0);
	}
	
	public Player(String nick) {
		this(0, nick, 0);
	}
	
	public Player(int id, String nick) {
		this(id, nick, 0);
	}
	
	public Player(int id, String nick, int gameId) {
		this.id = id;
		this.nick = nick;
		this.gameId = gameId;
	}
	
	public boolean hasRegistered() {
		return id != 0;
	}
	
	public boolean isPlaying() {
		return gameId != 0;
	}
	
	public int getGameId() {
		return gameId;
	}
	
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	/*
	 * True iff two player objects have the same id.
	 */
	public boolean equals(Player other) {
		return other != null && this.getId() == other.getId();
	}
	
	
}
