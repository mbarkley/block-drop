package ErraiLearning.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import ErraiLearning.client.shared.Game;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@Page
public class Board extends Composite {
	
	private Game game = null;
	
	private VerticalPanel boardPanel = new VerticalPanel();
	
	public Board() {
		String nickname = TTTClient.getInstance() != null ? TTTClient.getInstance().getNickname() : "Client";
		System.out.println(nickname+": Board constructor called.");
		
		game = TTTClient.getInstance().getGame();
		System.out.println(TTTClient.getInstance().getNickname()+": Game object passed to board.");
		
		initWidget(boardPanel);
	}
	
	@PostConstruct
	public void createBoard() {
		for (int i = 0; i < 3; i++)
			boardPanel.add(new HorizontalPanel());
	}

}
