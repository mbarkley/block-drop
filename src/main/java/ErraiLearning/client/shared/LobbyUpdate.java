package ErraiLearning.client.shared;

import java.util.ArrayList;
import java.util.Map;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class LobbyUpdate {

	private Map<Integer,Player> lobby;
	
	public LobbyUpdate() {}
	
	public LobbyUpdate(Map<Integer,Player> lobbyMap) {
		lobby = lobbyMap;
	}

	public Map<Integer,Player> getLobby() {
		return lobby;
	}
}
