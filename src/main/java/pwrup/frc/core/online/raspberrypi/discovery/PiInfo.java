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
  public boolean equals(Object other) {
    if (this == other)
      return true;
    if (other == null || getClass() != other.getClass())
      return false;
    PiInfo piInfo = (PiInfo) other;
    return java.util.Objects.equals(name, piInfo.name) &&
        java.util.Objects.equals(hostname, piInfo.hostname) &&
        java.util.Objects.equals(hostnameLocal, piInfo.hostnameLocal) &&
        java.util.Objects.equals(autobahnPort, piInfo.autobahnPort) &&
        java.util.Objects.equals(watchdogPort, piInfo.watchdogPort);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(name, hostname, hostnameLocal, autobahnPort, watchdogPort);
  }

  @Override
  public String toString() {
    return "PiInfo{" +
        "name='" + name + '\'' +
        ", hostname='" + hostname + '\'' +
        ", hostnameLocal='" + hostnameLocal + '\'' +
        ", autobahnPort=" + autobahnPort +
        ", watchdogPort=" + watchdogPort +
        '}';
  }
}
