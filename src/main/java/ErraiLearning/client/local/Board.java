package ErraiLearning.client.local;

import javax.annotation.PostConstruct;

import org.jboss.errai.ui.nav.client.local.Page;

import ErraiLearning.client.shared.Game;
import ErraiLearning.client.shared.InvalidMoveException;
import ErraiLearning.client.shared.Player;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@Page
public class Board extends Composite {
	
	public class TileClickEvent implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			System.out.println(TTTClient.getInstance().getNickname()+": ClickHandler activated.");
			Player activePlayer = TTTClient.getInstance().getPlayer();
			
			Tile clickedTile = ((Tile) event.getSource());
			
			try {
				game.makeMove(activePlayer.getId(), clickedTile.getRow(), clickedTile.getColumn());
			} catch (InvalidMoveException e) {
				System.out.println(TTTClient.getInstance().getNickname()+": Move was invalid.");
				//TODO: Add friendly error message for user in GUI.
				return;
			}
			
			if (game.getFirstPlayer().equals(activePlayer))
				setTileToX(clickedTile.getRow(), clickedTile.getColumn());
			else
				setTileToO(clickedTile.getRow(), clickedTile.getColumn());
			
			System.out.println(TTTClient.getInstance().getNickname()+": Move successfully made.");
		}

	}

	public static final String primaryTileStyle = "tile";
	public static final String unusedTileStyle = "unused-tile";
	public static final String xTileStyle = "x-tile";
	public static final String oTileStyle = "o-tile";
	
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
		for (int i = 0; i < 3; i++) {
			HorizontalPanel hPanel = new HorizontalPanel();
			boardPanel.add(hPanel);
			for (int j = 0; j < 3; j++) {
				Tile tile = new Tile(i, j);
				
				tile.setStylePrimaryName(primaryTileStyle);
				tile.setStyleName(unusedTileStyle, true);
				tile.addClickHandler(new TileClickEvent());
				
				hPanel.add(tile);
			}
		}
	}
	
	/*
	 * Replaces secondary tile style with given style.
	 */
	private void setTileStyleSecondary(int row, int col, String style) {
		SimplePanel tile = (SimplePanel) ((HorizontalPanel) boardPanel.getWidget(row)).getWidget(col);
		
		// Clear all styles
		tile.setStyleName("");
		
		tile.setStylePrimaryName(primaryTileStyle);
		tile.setStyleName(style, true);
	}
	
	public void setTileToX(int row, int col) {
		setTileStyleSecondary(row, col, xTileStyle);
	}
	
	public void setTileToO(int row, int col) {
		setTileStyleSecondary(row, col, oTileStyle);
	}
	
	public void resetTile(int row, int col) {
		setTileStyleSecondary(row, col, "");
	}

}
