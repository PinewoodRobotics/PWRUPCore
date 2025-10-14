package pwrup.frc.core.online;

import java.util.ArrayList;
import java.util.List;

import autobahn.client.AutobahnClient;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import pwrup.frc.core.proto.IDataClass;

public class PublicationSubsystem extends SubsystemBase {

    private final List<IDataClass> dataClasses;
    private static PublicationSubsystem self;
    private static AutobahnClient client;

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
            var data = dataClass.getRawConstructedProtoData();
            var topic = dataClass.getPublishTopic();
            PublicationSubsystem.client.publish(topic, data);
        }
    }
}
