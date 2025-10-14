package pwrup.frc.core.online.raspberrypi;

import java.util.function.Consumer;

import com.google.protobuf.InvalidProtocolBufferException;

import autobahn.client.AutobahnClient;
import autobahn.client.NamedCallback;
import proto.status.PiStatusOuterClass.LogMessage;

public class PiComs {
    private final Consumer<String> logger;

    public PiComs(AutobahnClient client, String piLoggingTopic, Consumer<String> logger) {
        this.logger = logger;
        client.subscribe(piLoggingTopic, NamedCallback.FromConsumer(this::subscription));
    }

    private void subscription(byte[] message) {
        LogMessage logMessage;
        try {
            logMessage = LogMessage.parseFrom(message);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return;
        }

        logger.accept(logMessage.getPrefix() + " " + logMessage.getPiName() + ": " + logMessage.getMessage());
    }
}
