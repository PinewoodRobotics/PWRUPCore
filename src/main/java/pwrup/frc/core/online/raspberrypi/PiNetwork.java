package pwrup.frc.core.online.raspberrypi;

import java.util.ArrayList;
import java.util.Arrays;
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

    public void add(RaspberryPi<P> raspberryPi) {
        raspberryPis.add(raspberryPi);
    }

    public void add(String address, String name, P... processesToRun) {
        add(new RaspberryPi<P>(address, name, Arrays.asList(processesToRun)));
    }

    public void add(String address, P... processesToRun) {
        add(new RaspberryPi<P>(address, address, Arrays.asList(processesToRun)));
    }

    public void add(Address address, String name, P... processesToRun) {
        add(new RaspberryPi<P>(toAddressString(address), name, Arrays.asList(processesToRun)));
    }

    public void add(Address address, P... processesToRun) {
        add(new RaspberryPi<P>(toAddressString(address), address.toString(), Arrays.asList(processesToRun)));
    }

    private String toAddressString(Address address) {
        return "http://" + address.getHost() + ":" + address.getPort();
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
