package demo.client.local.lobby;

import javax.inject.Inject;

import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

import demo.client.shared.meta.GameRoom;

/**
 * A panel for displaying information about and selecting games in the lobby.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Templated
public class GamePanel extends Composite implements HasModel<GameRoom> {

  @Inject
  @AutoBound
  private DataBinder<GameRoom> model;

  @DataField("num-players")
  @Inject
  @Bound(property = "players", converter = GamePanelConverter.class)
  private Label numPlayers;
  
  @DataField("game-id")
  @Inject
  @Bound(property = "id")
  private Label gameId;

  /**
   * Create a GamePanel.
   */
  public GamePanel() {
    super();
    addDomHandler(new GamePanelClickHandler(), ClickEvent.getType());
  }

  @Override
  public GameRoom getModel() {
    return model.getModel();
  }

  @Override
  public void setModel(GameRoom model) {
    this.model.setModel(model);
  }

}
