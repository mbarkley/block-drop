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

  @Override
  public void onClick(ClickEvent event) {
    System.out.println("PlayerPanel called with source: " + event.getSource().getClass());
    Player model = ((PlayerPanel) event.getSource()).getModel();

    Lobby page = Lobby.getInstance();
    page.togglePlayerSelection(model);
  }

}
