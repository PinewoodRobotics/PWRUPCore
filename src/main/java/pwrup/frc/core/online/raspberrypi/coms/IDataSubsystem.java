package pwrup.frc.core.online.raspberrypi.coms;

public interface IDataSubsystem {
  public byte[] getRawConstructedProtoData();

  public String getPublishTopic();
}
