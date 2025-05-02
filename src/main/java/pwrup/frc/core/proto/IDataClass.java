package pwrup.frc.core.proto;

public interface IDataClass {
  public byte[] getRawConstructedProtoData();

  public String getPublishTopic();
}
