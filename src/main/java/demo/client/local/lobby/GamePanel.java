package demo.client.local.lobby;

import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import com.google.gwt.user.client.ui.Composite;

import demo.client.shared.GameRoom;

@Templated
public class GamePanel extends Composite implements HasModel<GameRoom> {

  @Override
  public GameRoom getModel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setModel(GameRoom model) {
    // TODO Auto-generated method stub

  }

}
