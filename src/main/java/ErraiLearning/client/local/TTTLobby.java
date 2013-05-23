package ErraiLearning.client.local;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.nav.client.local.Page;

import ErraiLearning.client.shared.LobbyRequest;
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
@ApplicationScoped
public class TTTLobby extends Composite {
	
	private VerticalPanel vPanel = new VerticalPanel();
	private HorizontalPanel buttonPanel = new HorizontalPanel();
	private Button joinButton = new Button("Join Lobby");
	
	private Map<Integer, Player> localLobby = new HashMap<Integer, Player>();
	private String nickname;
	private Player player;
	
	@Inject
	private Event<LobbyRequest> lobbyRequest;
	@Inject
	private Navigation nav;
	
	
	public TTTLobby() {
		
		System.out.println("TTTLobby constructor called.");
		
		initWidget(vPanel);
		
		buttonPanel.add(joinButton);
		vPanel.add(buttonPanel);
		
		System.out.println("Is the composite object attached: " + this.isAttached());

		String initialValue = "Foobar";
		String msg = "Please select a username.";
		nickname = Window.prompt(msg, initialValue);
		
		if (nickname == null)
			nickname = "default";

		System.out.println("User selected " + nickname + " as their nickname.");
	}
	
	@PostConstruct
	public void postSetup() {
		
		System.out.println("Debug method starting.");
		System.out.println("Is the composite object visible: " + this.isVisible());
		System.out.println("Is the composite object attached: " + this.isAttached());
		System.out.println("Has it ever been attached: " + this.isOrWasAttached());
		System.out.println("Is the RootPanel attached: " + RootPanel.get().isAttached());
		System.out.println("Is the RootPanel visible: " + RootPanel.get().isVisible());
		System.out.println("The parent object of this composite object: " + this.getParent());
		
		joinButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				joinLobby();
			}
		});
		
		RootPanel.get().add(nav.getContentPanel());
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
	
	public void loadPlayer(@Observes Player player) {
		this.player = player;
		System.out.println("Client: Player object received.");
	}
	
	public void joinLobby() {
		LobbyRequest request = new LobbyRequest(nickname);
		lobbyRequest.fire(request);
		System.out.println("LobbyRequest fired.");
	}
}
