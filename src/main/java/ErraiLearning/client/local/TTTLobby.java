package ErraiLearning.client.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.TransitionTo;

import ErraiLearning.client.shared.LobbyUpdateRequest;
import ErraiLearning.client.shared.RegisterRequest;
import ErraiLearning.client.shared.LobbyUpdate;
import ErraiLearning.client.shared.Player;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@Page(role=DefaultPage.class)
@EntryPoint
public class TTTLobby extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private VerticalPanel lobbyPanel = new VerticalPanel();
	private Button lobbyButton = new Button("Join Lobby");
	private Button gameButton = new Button("Join Game");
	
	private String nickname;
	private Player player;
	/* Shadows lobbyPanel. */
	private List<Player> lobbyList = new ArrayList<Player>();
	
	@Inject	private Event<RegisterRequest> registerRequest;
	@Inject	private Event<LobbyUpdateRequest> lobbyUpdateRequest;
	@Inject	private Navigation nav;
	@Inject private TransitionTo<Board> boardTransition;
	
	/* For debugging only. */
	private static int curDebugId = 1;
	private int debugId;
	private static synchronized int nextDebugId() { return curDebugId++; }
	
	public TTTLobby() {
		
		debugId = nextDebugId();
		System.out.println(nickname+": TTTLobby constructor called.");
		
		initWidget(vPanel);
		
		buttonPanel.add(lobbyButton);
		buttonPanel.add(gameButton);
		gameButton.setVisible(false);
		vPanel.add(buttonPanel);
		vPanel.add(lobbyPanel);
		
		String initialValue = "Foobar";
		String msg = "Please select a username.";
		nickname = Window.prompt(msg, initialValue);
		
		if (nickname == null)
			nickname = "default";

		System.out.println(nickname+": User selected " + nickname + " as their nickname.");
	}
	
	@PostConstruct
	public void postSetup() {
		
		lobbyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				joinLobby();
			}
		});
		
		RootPanel.get().add(nav.getContentPanel());
	}
	
	public void updateLobby(@Observes LobbyUpdate update) {
		
		lobbyList.clear();
		lobbyPanel.clear();
	
		for (Player p : update.getPlayers().values()) {
			lobbyList.add(p);
			
			Button playerButton = new Button(p.getNick());
			lobbyPanel.add(playerButton);
			
			if (p.equals(player))
				playerButton.setVisible(false);
			else
				playerButton.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						
					}
				});
		}
	}
	
	public void loadPlayer(@Observes Player player) {
		this.player = player;
		System.out.println(nickname+": Player object received.");
		
		// Hide join lobby button and display join game button.
		lobbyButton.setVisible(false);
		gameButton.setVisible(true);
		
		requestLobbyUpdate();
	}

	public void requestLobbyUpdate() {
		lobbyUpdateRequest.fire(new LobbyUpdateRequest());
		System.out.println(nickname+": LobbyUpdateRequest fired.");
	}

	public void joinLobby() {
		RegisterRequest request = new RegisterRequest(nickname);
		registerRequest.fire(request);
		System.out.println(nickname+": LobbyRequest fired.");
	}
}
