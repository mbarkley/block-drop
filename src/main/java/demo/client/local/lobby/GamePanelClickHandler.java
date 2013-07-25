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

  @Override
  public void onClick(ClickEvent event) {
    System.out.println("GamePanel click event called.");
    GameRoom model = ((GamePanel) event.getSource()).getModel();

    Lobby.getInstance().toggleGameSelection(model);
  }

}
