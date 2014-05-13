package demo.client.local.lobby;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import demo.client.shared.meta.Player;

/**
 * A ClickHandler for {@link PlayerPanel player panels}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class PlayerPanelClickHandler implements ClickHandler {
  
  private Lobby lobby;

  public PlayerPanelClickHandler(Lobby lobby) {
    this.lobby = lobby;
  }

  @Override
  public void onClick(ClickEvent event) {
    Player model = ((PlayerPanel) event.getSource()).getModel();

    lobby.togglePlayerSelection(model);
  }

}
