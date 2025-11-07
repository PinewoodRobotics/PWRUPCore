package pwrup.frc.core.online.raspberrypi.discovery;

import java.util.Map;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PiInfo {

  private final String name;
  private final String hostname;
  private final String hostnameLocal;
  private final Optional<Integer> autobahnPort;
  private final Optional<Integer> watchdogPort;

  @Override
  public String toString() {
    return ("PiInfo{" +
        "name='" +
        name +
        '\'' +
        ", hostname=" +
        hostname +
        ", hostnameLocal=" +
        hostnameLocal +
        ", autobahnPort=" +
        autobahnPort +
        ", watchdogPort=" +
        watchdogPort +
        '}');
  }
}
