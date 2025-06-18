package pwrup.frc.core.online.raspberrypi.discovery;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PiInfo {

  private final String name;
  private final String[] addresses;
  private final int port;
  private final Map<String, String> properties;

  @Override
  public String toString() {
    return (
      "PiInfo{" +
      "name='" +
      name +
      '\'' +
      ", addresses=" +
      String.join(",", addresses) +
      ", port=" +
      port +
      ", properties=" +
      properties +
      '}'
    );
  }
}
