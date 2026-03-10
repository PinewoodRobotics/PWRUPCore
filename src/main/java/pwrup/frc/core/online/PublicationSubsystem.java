package pwrup.frc.core.online;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import autobahn.client.AutobahnClient;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import pwrup.frc.core.proto.IDataClass;

public class PublicationSubsystem extends SubsystemBase {

  private final Map<String, IDataClass> dataClasses;
  private static PublicationSubsystem self;
  private static AutobahnClient client;

  /**
   * Executor for offloading publish calls off the main robot thread. Uses a
   * bounded
   * queue to avoid unbounded growth when websocket is slow; drops oldest when
   * full.
   */
  private static final ThreadPoolExecutor publishExecutor = new ThreadPoolExecutor(
      4, 4, 0L, TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<>(64),
      r -> {
        var t = new Thread(r, "PublicationSubsystem-publish");
        t.setDaemon(true);
        return t;
      },
      new ThreadPoolExecutor.DiscardOldestPolicy());

  public static PublicationSubsystem GetInstance(AutobahnClient client, IDataClass... dataClasses) {
    if (self == null) {
      self = new PublicationSubsystem(client, dataClasses);
    }

    return self;
  }

  public static PublicationSubsystem GetInstance(AutobahnClient client) {
    return PublicationSubsystem.GetInstance(client, new IDataClass[] {});
  }

  public PublicationSubsystem(AutobahnClient client, IDataClass... dataClasses) {
    PublicationSubsystem.client = client;
    this.dataClasses = new LinkedHashMap<>();
    for (var dataClass : dataClasses) {
      this.dataClasses.put(dataClass.getClass().getName(), dataClass);
    }
  }

  public static void addDataClass(IDataClass dataClass) {
    GetInstance(PublicationSubsystem.client).dataClasses.put(dataClass.getClass().getName(), dataClass);
  }

  public static void addDataClasses(IDataClass... dataClasses) {
    var instance = GetInstance(PublicationSubsystem.client);
    Arrays.stream(dataClasses)
        .forEach(dataClass -> instance.dataClasses.put(dataClass.getClass().getName(), dataClass));
  }

  @Override
  public void periodic() {
    for (var dataClass : dataClasses.values()) {
      final byte[] data = dataClass.getRawConstructedProtoData();
      final String topic = dataClass.getPublishTopic();
      publishExecutor.execute(() -> PublicationSubsystem.client.publish(topic, data));
    }
  }

  public static void ClearAll() {
    self.dataClasses.clear();
  }
}
