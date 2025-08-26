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
import lombok.AllArgsConstructor;
import lombok.Getter;
import pwrup.frc.core.constant.RaspberryPiConstants;

@Getter
public class RaspberryPi<P extends Enum<P>> extends Address {

  private static final HttpClient client = HttpClient
      .newBuilder()
      .connectTimeout(Duration.ofSeconds(2))
      .build();

  private static final Gson gson = new GsonBuilder().create();

  private final String comsAddress;
  private final List<P> processesToRun;

  public RaspberryPi(String ipv6, int portAutobahn, int portComs, P... processesToRun) {
    super(ipv6, portAutobahn);

    this.comsAddress = "http://" + ipv6 + ":" + portComs;
    this.processesToRun = List.of(processesToRun);
  }

  public RaspberryPi(Address addressAutobahn, int portComs, P... processesToRun) {
    this(addressAutobahn.getHost(), addressAutobahn.getPort(), portComs, processesToRun);
  }

  public RaspberryPi(Address addressAutobahn, P... processesToRun) {
    this(addressAutobahn, RaspberryPiConstants.DEFAULT_PORT_COM, processesToRun);
  }

  public RaspberryPi(String ipv6, P... processesToRun) {
    this(ipv6, RaspberryPiConstants.DEFAULT_PORT_AUTOB, RaspberryPiConstants.DEFAULT_PORT_COM,
        processesToRun);
  }

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
   * Start the processes on the Raspberry Pi.
   *
   * @apiNote This method will block until the processes are started.
   * @note MAKE SURE TO IMPL THE toString() METHOD FOR THE ENUM!
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
