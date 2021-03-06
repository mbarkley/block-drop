package demo.client.local.lobby;

import javax.annotation.PostConstruct;
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

import demo.client.shared.meta.Player;

/**
 * A panel for displaying and selecting a player in the {@link Lobby lobby}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
@Templated
public class PlayerPanel extends Composite implements HasModel<Player> {

  @Inject
  @AutoBound
  private DataBinder<Player> model;

  @DataField
  @Inject
  @Bound
  private Label name;
  
  @Inject
  private Lobby lobby;

  @PostConstruct
  private void init() {
    addDomHandler(new PlayerPanelClickHandler(lobby), ClickEvent.getType());
  }

  @Override
  public Player getModel() {
    return this.model.getModel();
  }

  @Override
  public void setModel(Player model) {
    this.model.setModel(model);
  }
}
