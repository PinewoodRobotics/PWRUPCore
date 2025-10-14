package pwrup.frc.core.online.raspberrypi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import autobahn.client.Address;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import pwrup.frc.core.constant.RaspberryPiConstants;

/**
 * Represents a Raspberry Pi device with communication capabilities for managing
 * processes
 * and configuration via HTTP and Autobahn protocols.
 * 
 * <p>
 * This class provides methods to start, stop, and configure processes running
 * on a
 * Raspberry Pi. It extends {@link Address} to support Autobahn communication
 * and includes
 * HTTP-based control endpoints for process management.
 * </p>
 * 
 * @param <P> the enum type representing processes that can be run on the
 *            Raspberry Pi.
 *            While not strictly required, using an enum simplifies process
 *            management
 *            and ensures type safety. The enum must implement
 *            {@code toString()} to
 *            provide the process name.
 */
@Getter
public class RaspberryPi<P extends Enum<P>> extends Address {

  private static final HttpClient client = HttpClient
      .newBuilder()
      .connectTimeout(Duration.ofSeconds(2))
      .build();

  private static final Gson gson = new GsonBuilder().create();

  private final String comsAddress;
  private final List<P> processesToRun;

  /**
   * Constructs a RaspberryPi instance with specified network addresses and
   * processes.
   * 
   * @param ipv6           the IPv6 address of the Raspberry Pi
   * @param portAutobahn   the port number for Autobahn communication
   * @param portComs       the port number for HTTP communication/commands
   * @param processesToRun variable arguments of process enum values to be managed
   *                       on this Pi
   */
  public RaspberryPi(String ipv6, int portAutobahn, int portComs, P... processesToRun) {
    super(ipv6, portAutobahn);

    this.comsAddress = "http://" + ipv6 + ":" + portComs;
    this.processesToRun = List.of(processesToRun);
  }

  /**
   * Constructs a RaspberryPi instance using an existing Autobahn address.
   * 
   * @param addressAutobahn the Autobahn {@link Address} containing host and port
   *                        information
   * @param portComs        the port number for HTTP communication/commands
   * @param processesToRun  variable arguments of process enum values to be
   *                        managed on this Pi
   */
  public RaspberryPi(Address addressAutobahn, int portComs, P... processesToRun) {
    this(addressAutobahn.getHost(), addressAutobahn.getPort(), portComs, processesToRun);
  }

  /**
   * Constructs a RaspberryPi instance using an existing Autobahn address and
   * default
   * communication port.
   * 
   * @param addressAutobahn the Autobahn {@link Address} containing host and port
   *                        information
   * @param processesToRun  variable arguments of process enum values to be
   *                        managed on this Pi
   */
  public RaspberryPi(Address addressAutobahn, P... processesToRun) {
    this(addressAutobahn, RaspberryPiConstants.DEFAULT_PORT_COM, processesToRun);
  }

  /**
   * Constructs a RaspberryPi instance using default ports for both Autobahn and
   * communication protocols.
   * 
   * @param ipv6           the IPv6 address of the Raspberry Pi
   * @param processesToRun variable arguments of process enum values to be managed
   *                       on this Pi
   */
  public RaspberryPi(String ipv6, P... processesToRun) {
    this(ipv6, RaspberryPiConstants.DEFAULT_PORT_AUTOB, RaspberryPiConstants.DEFAULT_PORT_COM,
        processesToRun);
  }

  /**
   * Sets the configuration for the Raspberry Pi by sending a JSON configuration
   * string
   * to the Pi's configuration endpoint.
   * 
   * <p>
   * This method sends an HTTP POST request to the {@code /set/config} endpoint
   * with
   * the provided JSON configuration. The request has a timeout of 5 seconds.
   * </p>
   * 
   * @param rawJsonConfig the JSON configuration string to be set on the Raspberry
   *                      Pi
   * @return {@code true} if the configuration was successfully set (HTTP 200
   *         response),
   *         {@code false} otherwise
   */
  public boolean setConfig(String rawJsonConfig) {
    Map<String, String> configPayload = Map.of("config", rawJsonConfig);
    String jsonBody = gson.toJson(configPayload);

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(comsAddress + "/set/config"))
        .timeout(Duration.ofSeconds(5))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    try {
      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        System.err.println(
            "Failed to set config for " +
                comsAddress +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to set config for " + comsAddress);
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Starts all configured processes on the Raspberry Pi.
   * 
   * <p>
   * This method sends an HTTP POST request to the {@code /start/process} endpoint
   * with a list of process types to start. The process types are determined by
   * the
   * {@code toString()} method of the process enum.
   * </p>
   * 
   * <p>
   * <strong>Important:</strong> The process enum must implement
   * {@code toString()}
   * to return the correct process type name expected by the Raspberry Pi.
   * </p>
   * 
   * <p>
   * This method blocks until the processes have been started or the request times
   * out
   * (5 seconds).
   * </p>
   * 
   * @return {@code true} if all processes were successfully started (HTTP 200
   *         response),
   *         {@code false} if there are no processes to start or the request
   *         failed
   */
  public boolean startProcesses() {
    if (processesToRun.isEmpty()) {
      System.err.println("No processes to start for " + comsAddress);
      return false;
    }

    Map<String, List<String>> processPayload = Map.of(
        "process_types",
        processesToRun.stream().map(P::toString).toList());
    String jsonBody = gson.toJson(processPayload);

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(comsAddress + "/start/process"))
        .timeout(Duration.ofSeconds(5))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    try {
      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        System.err.println(
            "Failed to start processes for " +
                comsAddress +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to start processes for " + comsAddress);
      e.printStackTrace();

      return false;
    }

    return true;
  }

  /**
   * Stops all configured processes on the Raspberry Pi.
   * 
   * <p>
   * This method sends an HTTP POST request to the {@code /stop/process} endpoint
   * with a list of process types to stop. The process types are determined by the
   * {@code toString()} method of the process enum.
   * </p>
   * 
   * <p>
   * This method blocks until the processes have been stopped or the request times
   * out
   * (5 seconds).
   * </p>
   * 
   * @return {@code true} if all processes were successfully stopped (HTTP 200
   *         response),
   *         {@code false} otherwise
   */
  public boolean stopProcesses() {
    Map<String, List<String>> processPayload = Map.of(
        "process_types",
        processesToRun.stream().map(P::toString).toList());
    String jsonBody = gson.toJson(processPayload);

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(comsAddress + "/stop/process"))
        .timeout(Duration.ofSeconds(5))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    try {
      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        System.err.println(
            "Failed to stop processes for " +
                comsAddress +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to stop processes for " + comsAddress);
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /**
   * Stops a specific process on the Raspberry Pi and removes it from the list of
   * processes to run.
   * 
   * <p>
   * This method sends an HTTP POST request to the {@code /stop/process} endpoint
   * to stop the specified process. If successful, the process is removed from the
   * internal list of processes managed by this instance.
   * </p>
   * 
   * <p>
   * This method blocks until the process has been stopped or the request times
   * out
   * (5 seconds).
   * </p>
   * 
   * @param process the process enum value to stop
   * @return {@code true} if the process was successfully stopped (HTTP 200
   *         response),
   *         {@code false} otherwise
   */
  public boolean stopProcess(P process) {
    processesToRun.remove(process);
    Map<String, List<String>> processPayload = Map.of(
        "process_types",
        List.of(process.toString()));
    String jsonBody = gson.toJson(processPayload);

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(comsAddress + "/stop/process"))
        .timeout(Duration.ofSeconds(5))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();

    try {
      HttpResponse<String> response = client.send(
          request,
          HttpResponse.BodyHandlers.ofString());
      if (response.statusCode() != 200) {
        System.err.println(
            "Failed to stop process " +
                process +
                " for " +
                comsAddress +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to stop process " + process + " for " + comsAddress);
      e.printStackTrace();
      return false;
    }

    return true;
  }
}
