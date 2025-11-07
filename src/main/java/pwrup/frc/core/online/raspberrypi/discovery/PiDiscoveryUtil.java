package pwrup.frc.core.online.raspberrypi.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

public class PiDiscoveryUtil {

  private static final String SERVICE_TYPE = "_autobahn._udp.local.";

  /**
   * Discovers Pis advertising the SERVICE_TYPE on the local network.
   * 
   * @param timeoutSeconds how long to wait for responses
   * @return list of discovered PiInfo objects
   * @throws IOException
   * @throws InterruptedException
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
          String name = info.getName();
          String[] hosts = info.getHostAddresses();
          int port = info.getPort();
          // copy properties into a regular map
          Map<String, String> props = new HashMap<>();
          for (String key : Collections.list(info.getPropertyNames())) {
            props.put(key, info.getPropertyString(key));
          }
          found.add(new PiInfo(name, hosts, port, props));
        }
      };

      jmdns.addServiceListener(SERVICE_TYPE, listener);
      // wait
      Thread.sleep(timeoutSeconds * 1000L);
      jmdns.removeServiceListener(SERVICE_TYPE, listener);
    }

    return found;
  }
}
