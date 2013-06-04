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

/*
 * A container class for a tic-tac-toe client.
 */
@EntryPoint
@ApplicationScoped
public class TTTClient {
	
	/* The user's chosen nickname. */
	private String nickname = null;
	/* The player object associated with this user. */
	private Player player = null;
	/* The game this user is currently in (or null if not in a game). */
	private Game game = null;
	
	/* For the Errai NavigationUI. */
	@Inject	private Navigation nav;
	
	/* For debugging only. */
	private static int curDebugId = 1;
	/* For debugging only. */
	private int debugId;
	/* For debugging only. */
	private static synchronized int nextDebugId() { return curDebugId++; }
	
	/* For accessing the singleton instance of this object. */
	private static TTTClient instance = null;
	
	/*
	 * Get the singleton instance of this client.
	 */
	public static TTTClient getInstance() {
		return instance;
	}
	
	/*
	 * Create a TTTClient object. TTTClient should be a singleton, but this is not enforced.
	 * This constructor should only be called once.
	 */
	public TTTClient() {
		// For debugging.
		debugId = nextDebugId();
		System.out.println("Client"+debugId+": TTTClient constructor called.");
		
		instance = this;
	}
	
	/*
	 * Prompt the user for a nickname and attach the navigation panel.
	 */
	@PostConstruct
	public void postSetup() {
		String initialValue = "Foobar";
		String msg = "Please select a username.";
		nickname = Window.prompt(msg, initialValue);
		
		if (nickname == null)
			nickname = "default";

		// For debugging.
		System.out.println(nickname+": User selected " + nickname + " as their nickname.");
		
		RootPanel.get().add(nav.getContentPanel());
	}
	
	/*
	 * Check if this client has registered a player object with the server.
	 * 
	 * @return True iff this client has registered a player with the server.
	 */
	public boolean hasRegisteredPlayer() {
		return player != null;
	}
	
	/*
	 * Get the player object associated with this user.
	 * 
	 * @return The player object associated with this user.
	 */
	public Player getPlayer() {
		return player;
	}
	
	/*
	 * Set the player object associated with this user.
	 * 
	 * @param player The new player object to be associated with this user.
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/*
	 * Get the nickname of this player.
	 * 
	 * @return The nickname of this player.
	 */
	public String getNickname() {
		return nickname;
	}

	/*
	 * Get the game object representing the game this client is currently in.
	 * 
	 * @return The game object representing the game this client is currently in.
	 */
	public Game getGame() {
		return game;
	}

	/*
	 * Set the game object representing the game this client is currently in.
	 * 
	 * @param game The game object representing the game this client is currently in.
	 */
	public void setGame(Game game) {
		this.game = game;
	}
}
