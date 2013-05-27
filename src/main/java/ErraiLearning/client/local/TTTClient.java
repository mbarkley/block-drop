package ErraiLearning.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.Navigation;

import ErraiLearning.client.shared.Invitation;
import ErraiLearning.client.shared.LobbyUpdateRequest;
import ErraiLearning.client.shared.Player;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class TTTClient {
	
	private String nickname;
	private Player player;
	
	@Inject	private Navigation nav;
	
	/* For debugging only. */
	private static int curDebugId = 1;
	private int debugId;
	private static synchronized int nextDebugId() { return curDebugId++; }
	
	private static TTTClient instance = null;
	
	public static TTTClient getInstance() {
		return instance;
	}
	
	public TTTClient() {
		
		debugId = nextDebugId();
		System.out.println(nickname+": TTTClient constructor called.");
		
		String initialValue = "Foobar";
		String msg = "Please select a username.";
		nickname = Window.prompt(msg, initialValue);
		
		if (nickname == null)
			nickname = "default";

		System.out.println(nickname+": User selected " + nickname + " as their nickname.");
		
		instance = this;
	}
	
	@PostConstruct
	public void postSetup() {
		RootPanel.get().add(nav.getContentPanel());
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public String getNickname() {
		return nickname;
	}
}
