package demo.client.local.lobby;

import javax.inject.Inject;

import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.AutoBound;
import org.jboss.errai.ui.shared.api.annotations.Bound;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

import demo.client.shared.Player;

@Templated
public class PlayerPanel extends Composite implements HasModel<Player> {
  
  @Inject
  @AutoBound
  DataBinder<Player> model;
  
  @DataField
  @Inject
  @Bound(property="nick")
  private Label name;

  @Override
  public Player getModel() {
    return this.model.getModel();
  }

  @Override
  public void setModel(Player model) {
    this.model.setModel(model);
  }

}
