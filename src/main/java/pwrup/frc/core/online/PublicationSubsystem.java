package pwrup.frc.core.online;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import autobahn.client.AutobahnClient;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import pwrup.frc.core.proto.IDataClass;

public class PublicationSubsystem extends SubsystemBase {

  private final List<IDataClass> dataClasses;
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
    this.dataClasses = new ArrayList<>(List.of(dataClasses));
  }

  public static void addDataClass(IDataClass dataClass) {
    GetInstance(PublicationSubsystem.client).dataClasses.add(dataClass);
  }

  public static void addDataClasses(IDataClass... dataClasses) {
    GetInstance(PublicationSubsystem.client).dataClasses.addAll(List.of(dataClasses));
  }

  @Override
  public void periodic() {
    for (var dataClass : dataClasses) {
      final byte[] data = dataClass.getRawConstructedProtoData();
      final String topic = dataClass.getPublishTopic();
      publishExecutor.execute(() -> PublicationSubsystem.client.publish(topic, data));
    }
  }
}
