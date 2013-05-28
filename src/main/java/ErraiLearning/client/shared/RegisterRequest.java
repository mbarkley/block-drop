package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisterRequest {

	private Player player = null;
	
	public RegisterRequest() {}
	
	public RegisterRequest(Player player) {
		this.player = player;
	}

	public String getNickname() {
		return player.getNick();
	}
	
	public boolean hasRegistered() {
		return player != null && player.hasRegistered();
	}

	public Player getPlayer() {
		return player;
	}
}
