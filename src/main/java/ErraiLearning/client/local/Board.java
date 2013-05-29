package ErraiLearning.client.local;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.bus.client.api.base.MessageBuilder;
import org.jboss.errai.bus.client.api.messaging.Message;
import org.jboss.errai.bus.client.api.messaging.MessageBus;
import org.jboss.errai.bus.client.api.messaging.MessageCallback;
import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
import org.jboss.errai.common.client.protocols.MessageParts;
import org.jboss.errai.ui.nav.client.local.Page;

import ErraiLearning.client.shared.Game;
import ErraiLearning.client.shared.InvalidMoveException;
import ErraiLearning.client.shared.Move;
import ErraiLearning.client.shared.Player;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

@Page
public class Board extends Composite {
	
	public class GameMessageCallback implements MessageCallback {

		@Override
		public void callback(Message message) {
			if ("validate-move".equals(message.getCommandType())) {
				validateMoveCallback(message);
			}
		}

		private void validateMoveCallback(Message message) {
			Move move = message.get(Move.class, MessageParts.Value);
			
			if (move.getPlayerId() == TTTClient.getInstance().getPlayer().getId()) {
				if (move.isValidated()) {
					if (!game.validateLastMove(move)) {
						//TODO: Rollback move. Possibly resync game state with server.
					}
				} else {
					//TODO: Show message to user that move was rejected.
					// Possibly try and resolve with server.
				}
			} else {
				if (move.isValidated()) {
					try {
						game.makeMove(move.getPlayerId(), move.getRow(), move.getCol());
						if (!game.validateLastMove(move)) {
							//TODO: Error handling.
						}

						if (game.getPlayerX().getId() == move.getPlayerId())
							setTileToX(move.getRow(), move.getCol());
						else
							setTileToO(move.getRow(), move.getCol());
					} catch (InvalidMoveException e) {
						e.printStackTrace();
						//TODO: Show message to user/try to resolve error.
					}
				} // If !move.isValidated() then we may ignore this.
			}
		}

	}

	public class TileClickEvent implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			System.out.println(TTTClient.getInstance().getNickname()+": ClickHandler activated.");
			Player activePlayer = TTTClient.getInstance().getPlayer();
			
			Tile clickedTile = ((Tile) event.getSource());
			
			// Try to make move in model and catch if invalid.
			try {
				game.makeMove(activePlayer.getId(), clickedTile.getRow(), clickedTile.getColumn());
				System.out.println(TTTClient.getInstance().getNickname()+": Move was valid.");
			} catch (InvalidMoveException e) {
				System.out.println(TTTClient.getInstance().getNickname()+": Move was invalid.");
				//TODO: Add friendly error message for user in GUI.
				return;
			}
			
			// Display move on this client's page.
			if (game.getPlayerX().equals(activePlayer))
				setTileToX(clickedTile.getRow(), clickedTile.getColumn());
			else
				setTileToO(clickedTile.getRow(), clickedTile.getColumn());
			
			System.out.println(TTTClient.getInstance().getNickname()+": Tile changed.");
			
			// Send message to server (to be relayed to other client).
			MessageBuilder.createMessage()
			.toSubject("Relay")
			.command("publish-move")
			.withValue(game.getLastMove())
			.noErrorHandling()
			.sendNowWith(dispatcher);
			
			System.out.println(TTTClient.getInstance().getNickname()+": Move sent.");
		}

	}

	public static final String primaryTileStyle = "tile";
	public static final String unusedTileStyle = "unused-tile";
	public static final String xTileStyle = "x-tile";
	public static final String oTileStyle = "o-tile";
	
	private Game game = null;
	
	private VerticalPanel boardPanel = new VerticalPanel();
	
	@Inject private RequestDispatcher dispatcher;
	@Inject private MessageBus messageBus;
	
	public Board() {
		//String nickname = TTTClient.getInstance() != null ? TTTClient.getInstance().getNickname() : "Client";
		String nickname = TTTClient.getInstance().getNickname();
		System.out.println(nickname+": Board constructor called.");
		
		game = TTTClient.getInstance().getGame();
		System.out.println(TTTClient.getInstance().getNickname()+": Game object passed to board.");
		
		initWidget(boardPanel);
	}
	
	@PostConstruct
	public void setupBoard() {
		// Subscribe to game channel, which will be used by server and clients to communicate moves.
		messageBus.subscribe("Game"+game.getGameId(), new GameMessageCallback());
		
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
