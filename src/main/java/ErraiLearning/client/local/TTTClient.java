package ErraiLearning.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.Navigation;

import ErraiLearning.client.shared.Game;
import ErraiLearning.client.shared.Player;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
@ApplicationScoped
public class TTTClient {
	
	private String nickname = null;
	private Player player = null;
	private Game game = null;
	
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
		System.out.println("Client"+debugId+": TTTClient constructor called.");
		
		instance = this;
	}
	
	@PostConstruct
	public void postSetup() {
		String initialValue = "Foobar";
		String msg = "Please select a username.";
		nickname = Window.prompt(msg, initialValue);
		
		if (nickname == null)
			nickname = "default";

		System.out.println(nickname+": User selected " + nickname + " as their nickname.");
		
		RootPanel.get().add(nav.getContentPanel());
	}
	
	public boolean hasRegisteredPlayer() {
		return player != null;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
