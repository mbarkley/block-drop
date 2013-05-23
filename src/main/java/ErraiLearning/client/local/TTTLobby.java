package ErraiLearning.client.local;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import ErraiLearning.client.shared.LobbyRequest;
import ErraiLearning.client.shared.LobbyUpdate;
import ErraiLearning.client.shared.Player;

import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

@Page(role=DefaultPage.class)
public class TTTLobby extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private Button joinButton = new Button("Join Lobby");
	
	private Map<Integer, Player> localLobby = new HashMap<Integer, Player>();
	
	@Inject
	private Event<LobbyRequest> lobbyRequest;
	
	public TTTLobby() {
		
		System.out.println("TTTLobby constructor called.");
		
		initWidget(vPanel);
		
		buttonPanel.add(joinButton);
		vPanel.add(buttonPanel);

		String initialValue = "Foobar";
		String msg = "Please select a username.";
		String nickname = Window.prompt(msg, initialValue);
		
		if (nickname == null)
			nickname = "default";

		System.out.println("User selected " + nickname + " as their nickname.");
		
		//TODO: Figure out why this doesn't work.
		vPanel.addHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				System.out.println("Testing load event.");
			}
		},
		LoadEvent.getType());
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		System.out.println("TTTLobby object attached into browser.");
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		System.out.println("TTTLobby object loaded into browser.");
	}
	
	public void updateLobby(@Observes LobbyUpdate update) {
		localLobby = update.getLobby();
		
		for (Entry<Integer, Player> entry : localLobby.entrySet()) {
			vPanel.add(new Label(entry.getValue().getNick()));
		}
	}
	
	void joinLobby(String nickname) {
		LobbyRequest request = new LobbyRequest(nickname);
		lobbyRequest.fire(request);
		System.out.println("LobbyRequest fired.");
	}
}
