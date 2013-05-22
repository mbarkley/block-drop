package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class LobbyRequest {

	private String nickname = "";
	
	public LobbyRequest() {}
	
	public LobbyRequest(String nickname) {
		this.setNickname(nickname);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
