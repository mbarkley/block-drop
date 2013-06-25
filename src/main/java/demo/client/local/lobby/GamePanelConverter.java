package demo.client.local.lobby;

import java.util.Map;

import org.jboss.errai.databinding.client.api.Converter;

import demo.client.shared.Player;

public class GamePanelConverter implements Converter<Map<Integer,Player>, String> {

  @Override
  public Map<Integer, Player> toModelValue(String widgetValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toWidgetValue(Map<Integer, Player> modelValue) {
    return String.valueOf(modelValue.size());
  }

}
