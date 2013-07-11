package demo.client.local.game.gui;

import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

@Templated
public class GameOverPanel extends Composite {

  @DataField
  Element score = DOM.createSpan();
  
  @DataField
  Button play = new Button();
  
  @DataField
  Button lobby = new Button();
  
  @EventHandler("play")
  public void playAgain(ClickEvent e) {
    BoardPage.getInstance().getController().restart();
    setVisible(false);
  }
  
  @EventHandler("lobby")
  public void goToLobby(ClickEvent e) {
    BoardPage.getInstance().goToLobby();
  }

}
