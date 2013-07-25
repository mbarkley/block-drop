package demo.client.local.lobby;

import java.util.Map;

import org.jboss.errai.databinding.client.api.Converter;

import demo.client.shared.meta.GameRoom;
import demo.client.shared.meta.Player;

/**
 * A class for setting the size of a {@link GameRoom game room} in a {@link GamePanel game panel}.
 * 
 * @author mbarkley <mbarkley@redhat.com>
 * 
 */
public class GamePanelConverter implements Converter<Map<Integer, Player>, String> {

  @Override
  public Map<Integer, Player> toModelValue(String widgetValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toWidgetValue(Map<Integer, Player> modelValue) {
    return String.valueOf(modelValue.size());
  }

}
