package pwrup.frc.core.online.raspberrypi.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class PiDiscoveryUtil {

  private static final String SERVICE_TYPE = "_watchdog._udp.local.";

  /**
   * Discovers Pis advertising the SERVICE_TYPE on the local network.
   * 
   * @param timeoutSeconds how long to wait for responses
   * @return list of discovered PiInfo objects
   * @throws IOException
   * @throws InterruptedException
   */

  /*
   * _info = ServiceInfo(
   * TYPE_,
   * f"{hostname}.{TYPE_}",
   * addresses=addresses,
   * port=9999,
   * server=hostname_local,
   * properties={
   * "hostname": hostname,
   * "hostname_local": hostname_local,
   * "system_name": system_name,
   * "watchdog_port": system_config.watchdog.port,
   * "autobahn_port": system_config.autobahn.port,
   * },
   * )
   */
  public static List<PiInfo> discover(int timeoutSeconds)
      throws IOException, InterruptedException {
    List<PiInfo> found = new CopyOnWriteArrayList<>();
    InetAddress addr = InetAddress.getLocalHost();
    try (JmDNS jmdns = JmDNS.create(addr)) {
      ServiceListener listener = new ServiceListener() {
        @Override
        public void serviceAdded(ServiceEvent event) {
          // trigger resolution
          jmdns.requestServiceInfo(event.getType(), event.getName(), 1000);
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
          // optionally handle removals
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
          ServiceInfo info = event.getInfo();
          var props = getServicePropertiesRaw(info);

          // Get hostname with fallbacks: property -> server -> first host address
          String hostname = props.get("hostname");
          if (hostname == null || hostname.isEmpty()) {
            hostname = info.getServer();
          }

          if (hostname == null || hostname.isEmpty()) {
            var hostAddresses = info.getHostAddresses();
            if (hostAddresses != null && hostAddresses.length > 0) {
              hostname = hostAddresses[0];
            }
          }

          if (hostname == null || hostname.isEmpty()) {
            return;
          }

          found.add(new PiInfo(props.get("system_name"), hostname, props.get("hostname_local"),
              props.get("autobahn_port") != null ? Optional.of(Integer.parseInt(props.get("autobahn_port")))
                  : Optional.empty(),
              props.get("watchdog_port") != null ? Optional.of(Integer.parseInt(props.get("watchdog_port")))
                  : Optional.empty()));
        }
      };

      jmdns.addServiceListener(SERVICE_TYPE, listener);
      // wait
      Thread.sleep(timeoutSeconds * 1000L);
      jmdns.removeServiceListener(SERVICE_TYPE, listener);
    }

    return found;
  }

  private static Map<String, String> getServicePropertiesRaw(ServiceInfo info) {
    Map<String, String> props = new HashMap<>();
    for (String key : Collections.list(info.getPropertyNames())) {
      props.put(key, info.getPropertyString(key));
    }

    return props;
  }
}
