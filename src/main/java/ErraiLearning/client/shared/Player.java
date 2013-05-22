package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Player {

	private int id;
	private String nick;
	private boolean playing = false;
	
	public Player() {}
	
	public Player(String nick) {
		id = 0; // Constructor for client. Id must be assigned by server.
		this.nick = nick;
	}
	
	public Player(int id, String nick) {
		this.id = id;
		this.nick = nick;
	}
	
	public boolean isPlaying() {
		return playing;
	}
	public void setPlaying(boolean playing) {
		this.playing = playing;
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
	
	
}
