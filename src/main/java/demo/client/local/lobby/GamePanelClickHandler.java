package demo.client.local.lobby;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import demo.client.shared.meta.GameRoom;

/**
 * A ClickHandler for {@link GamePanel game panels}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class GamePanelClickHandler implements ClickHandler {
  
  private Lobby lobby;

  public GamePanelClickHandler(Lobby lobby) {
    this.lobby = lobby;
  }

  @Override
  public void onClick(ClickEvent event) {
    GameRoom model = ((GamePanel) event.getSource()).getModel();

    lobby.toggleGameSelection(model);
    lobby.joinSelectedGame();
  }

}
