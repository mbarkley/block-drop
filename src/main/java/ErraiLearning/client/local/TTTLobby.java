package ErraiLearning.client.local;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;

import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import ErraiLearning.client.shared.LobbyUpdate;
import ErraiLearning.client.shared.Player;

@EntryPoint
public class TTTLobby {
	
	private VerticalPanel vPanel = new VerticalPanel();
	
	private Map<Integer, Player> localLobby = new HashMap<Integer, Player>();
	
	@PostConstruct
	public void buildUI() {
		RootPanel.get().add(vPanel);
	}
	
	public void updateLobby(@Observes LobbyUpdate update) {
		localLobby = update.getLobby();
		
		for (Entry<Integer, Player> entry : localLobby.entrySet()) {
			vPanel.add(new Label(entry.getValue().getNick()));
		}
	}
}
