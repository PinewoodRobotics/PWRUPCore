package pwrup.frc.core.online.raspberrypi;

import java.util.ArrayList;
import java.util.List;

import autobahn.client.Address;

public class PiNetwork<P extends Enum<P>> {

    private final List<RaspberryPi<P>> raspberryPis;

    public PiNetwork(List<RaspberryPi<P>> raspberryPis) {
        this.raspberryPis = raspberryPis;
    }

    public PiNetwork() {
        this.raspberryPis = new ArrayList<>();
    }

    /**
     * Add a Raspberry Pi to the network. Note that the newer RaspberryPis will be
     * added to the end of the list.
     * 
     * @param raspberryPi the Raspberry Pi to add
     */
    public void add(RaspberryPi<P> raspberryPi) {
        raspberryPis.add(raspberryPi);
    }

    public void add(String ipv6, int portAutobahn, int portComs, P... processesToRun) {
        add(new RaspberryPi<P>(ipv6, portAutobahn, portComs, processesToRun));
    }

    public void add(Address addressAutobahn, int portComs, P... processesToRun) {
        add(new RaspberryPi<P>(addressAutobahn, portComs, processesToRun));
    }

    public void add(Address addressAutobahn, P... processesToRun) {
        add(new RaspberryPi<P>(addressAutobahn, processesToRun));
    }

    public void add(String ipv6, P... processesToRun) {
        add(new RaspberryPi<P>(ipv6, processesToRun));
    }

    /**
     * Get the main Raspberry Pi.
     * 
     * @return the main Raspberry Pi (the first one in the list always)
     */
    public RaspberryPi<P> getMainPi() {
        return raspberryPis.get(0);
    }

    public boolean startAllPis() {
        return raspberryPis.stream().allMatch(RaspberryPi::startProcesses);
    }

    public boolean stopAllPis() {
        return raspberryPis.stream().allMatch(RaspberryPi::stopProcesses);
    }

    public boolean setConfig(String rawConfig) {
        return raspberryPis.stream().allMatch(raspberryPi -> raspberryPi.setConfig(rawConfig));
    }

    public boolean restartAllPis() {
        return stopAllPis() && startAllPis();
    }
}
