package ErraiLearning.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class RegisterRequest {

	private String nickname = "";
	
	public RegisterRequest() {}
	
	public RegisterRequest(String nickname) {
		this.setNickname(nickname);
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
