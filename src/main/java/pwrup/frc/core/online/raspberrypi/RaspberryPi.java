package pwrup.frc.core.online.raspberrypi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

@AllArgsConstructor
@Getter
public class RaspberryPi<P extends Enum<P>> {

  private static final HttpClient client = HttpClient
      .newBuilder()
      .connectTimeout(Duration.ofSeconds(2))
      .build();

  private static final Gson gson = new GsonBuilder().create();

  private final String name;
  private final String address;
  private final List<P> processesToRun;

  public boolean setConfig(String rawJsonConfig) {
    Map<String, String> configPayload = Map.of("config", rawJsonConfig);
    String jsonBody = gson.toJson(configPayload);

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(address + "/set/config"))
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
                name +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to set config for " + name);
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
      System.err.println("No processes to start for " + name);
      return false;
    }

    Map<String, List<String>> processPayload = Map.of(
        "process_types",
        processesToRun.stream().map(P::toString).toList());
    String jsonBody = gson.toJson(processPayload);

    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(address + "/start/process"))
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
                name +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to start processes for " + name);
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
        .uri(URI.create(address + "/stop/process"))
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
                name +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to stop processes for " + name);
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
        .uri(URI.create(address + "/stop/process"))
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
                name +
                " - Status: " +
                response.statusCode());
        return false;
      }
    } catch (IOException | InterruptedException e) {
      System.err.println("Failed to stop process " + process + " for " + name);
      e.printStackTrace();
      return false;
    }

    return true;
  }
}
