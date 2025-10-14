package pwrup.frc.core.online.raspberrypi;

import java.util.function.Consumer;

import com.google.protobuf.InvalidProtocolBufferException;

import autobahn.client.AutobahnClient;
import autobahn.client.NamedCallback;
import proto.status.PiStatusOuterClass.LogMessage;

public class PrintPiLogs {
    public static void ToSystemOut(AutobahnClient client, String piLoggingTopic) {
        client.subscribe(piLoggingTopic, NamedCallback.FromConsumer((byte[] message) -> {
            LogMessage logMessage;
            try {
                logMessage = LogMessage.parseFrom(message);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                return;
            }

            System.out.println(logMessage.getPrefix() + " " + logMessage.getPiName() + ": " + logMessage.getMessage());
        }));
    }
}
