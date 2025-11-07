package pwrup.frc.core.online.raspberrypi;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ConstrainedProcess<P extends Enum<P> & Comparable<P> & WeightedProcess> {
    private final List<String> pisToRunOn;
    private final P processToRun;

    public ConstrainedProcess(P processToRun, String... pisToRunOn) {
        this.processToRun = processToRun;
        this.pisToRunOn = List.of(pisToRunOn);
    }

    public boolean equalsToProcess(P process) {
        return processToRun.equals(process);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstrainedProcess<?>) {
            return ((ConstrainedProcess<?>) obj).processToRun.equals(processToRun);
        }

        return false;
    }
}
