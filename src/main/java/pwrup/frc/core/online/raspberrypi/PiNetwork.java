package pwrup.frc.core.online.raspberrypi;

import java.util.ArrayList;
import java.util.List;

import autobahn.client.Address;

/**
 * Manages a network of Raspberry Pi devices with coordinated control
 * capabilities.
 * 
 * <p>
 * This class provides centralized management for multiple {@link RaspberryPi}
 * instances,
 * allowing batch operations such as starting, stopping, and configuring all
 * Raspberry Pis
 * in the network simultaneously.
 * </p>
 * 
 * <p>
 * The first Raspberry Pi added to the network is designated as the "main" Pi
 * and can
 * be retrieved via {@link #getMainPi()}.
 * </p>
 * 
 * @param <P> the enum type representing processes that can be run on the
 *            Raspberry Pis.
 *            While not strictly required, using an enum simplifies process
 *            management
 *            and ensures type safety across all Pis in the network.
 * @see RaspberryPi
 */
public class PiNetwork<P extends Enum<P>> {

    private final List<RaspberryPi<P>> raspberryPis;

    /**
     * Constructs a PiNetwork with an existing list of Raspberry Pis.
     * 
     * @param raspberryPis the list of {@link RaspberryPi} instances to manage
     */
    public PiNetwork(List<RaspberryPi<P>> raspberryPis) {
        this.raspberryPis = raspberryPis;
    }

    /**
     * Constructs an empty PiNetwork.
     * 
     * <p>
     * Raspberry Pis can be added later using the {@link #add} methods.
     * </p>
     */
    public PiNetwork() {
        this.raspberryPis = new ArrayList<>();
    }

    /**
     * Adds a Raspberry Pi to the network.
     * 
     * <p>
     * Raspberry Pis are added to the end of the list. The first Pi added becomes
     * the main Pi and can be retrieved via {@link #getMainPi()}.
     * </p>
     * 
     * @param raspberryPi the {@link RaspberryPi} instance to add to the network
     */
    public void add(RaspberryPi<P> raspberryPi) {
        raspberryPis.add(raspberryPi);
    }

    /**
     * Creates and adds a new Raspberry Pi to the network with specified network
     * addresses
     * and processes.
     * 
     * @param ipv6           the IPv6 address of the Raspberry Pi
     * @param portAutobahn   the port number for Autobahn communication
     * @param portComs       the port number for HTTP communication/commands
     * @param processesToRun variable arguments of process enum values to be managed
     *                       on this Pi
     */
    public void add(String ipv6, int portAutobahn, int portComs, P... processesToRun) {
        add(new RaspberryPi<P>(ipv6, portAutobahn, portComs, processesToRun));
    }

    /**
     * Creates and adds a new Raspberry Pi to the network using an existing Autobahn
     * address.
     * 
     * @param addressAutobahn the Autobahn {@link Address} containing host and port
     *                        information
     * @param portComs        the port number for HTTP communication/commands
     * @param processesToRun  variable arguments of process enum values to be
     *                        managed on this Pi
     */
    public void add(Address addressAutobahn, int portComs, P... processesToRun) {
        add(new RaspberryPi<P>(addressAutobahn, portComs, processesToRun));
    }

    /**
     * Creates and adds a new Raspberry Pi to the network using an existing Autobahn
     * address
     * and default communication port.
     * 
     * @param addressAutobahn the Autobahn {@link Address} containing host and port
     *                        information
     * @param processesToRun  variable arguments of process enum values to be
     *                        managed on this Pi
     */
    public void add(Address addressAutobahn, P... processesToRun) {
        add(new RaspberryPi<P>(addressAutobahn, processesToRun));
    }

    /**
     * Creates and adds a new Raspberry Pi to the network using default ports for
     * both
     * Autobahn and communication protocols.
     * 
     * @param ipv6           the IPv6 address of the Raspberry Pi
     * @param processesToRun variable arguments of process enum values to be managed
     *                       on this Pi
     */
    public void add(String ipv6, P... processesToRun) {
        add(new RaspberryPi<P>(ipv6, processesToRun));
    }

    /**
     * Returns the main Raspberry Pi in the network.
     * 
     * <p>
     * The main Pi is always the first Raspberry Pi that was added to the network.
     * </p>
     * 
     * @return the main {@link RaspberryPi} instance (the first one in the list)
     * @throws IndexOutOfBoundsException if the network is empty
     */
    public RaspberryPi<P> getMainPi() {
        return raspberryPis.get(0);
    }

    /**
     * Starts all processes on all Raspberry Pis in the network.
     * 
     * <p>
     * This method calls {@link RaspberryPi#startProcesses()} on each Pi in the
     * network.
     * All Pis are started in parallel, but this method blocks until all operations
     * complete.
     * </p>
     * 
     * @return {@code true} if all processes on all Pis were successfully started,
     *         {@code false} if any Pi failed to start its processes
     */
    public boolean startAllPis() {
        return raspberryPis.stream().allMatch(RaspberryPi::startProcesses);
    }

    /**
     * Stops all processes on all Raspberry Pis in the network.
     * 
     * <p>
     * This method calls {@link RaspberryPi#stopProcesses()} on each Pi in the
     * network.
     * All Pis are stopped in parallel, but this method blocks until all operations
     * complete.
     * </p>
     * 
     * @return {@code true} if all processes on all Pis were successfully stopped,
     *         {@code false} if any Pi failed to stop its processes
     */
    public boolean stopAllPis() {
        return raspberryPis.stream().allMatch(RaspberryPi::stopProcesses);
    }

    /**
     * Sets the same configuration on all Raspberry Pis in the network.
     * 
     * <p>
     * This method calls {@link RaspberryPi#setConfig(String)} on each Pi in the
     * network
     * with the same configuration string. All configuration operations are
     * performed in
     * parallel, but this method blocks until all operations complete.
     * </p>
     * 
     * @param rawConfig the JSON configuration string to be set on all Raspberry Pis
     * @return {@code true} if the configuration was successfully set on all Pis,
     *         {@code false} if any Pi failed to set the configuration
     */
    public boolean setConfig(String rawConfig) {
        return raspberryPis.stream().allMatch(raspberryPi -> raspberryPi.setConfig(rawConfig));
    }

    /**
     * Restarts all processes on all Raspberry Pis in the network.
     * 
     * <p>
     * This method first stops all processes using {@link #stopAllPis()}, then
     * starts
     * them again using {@link #startAllPis()}. The operations are sequential: all
     * Pis
     * are stopped before any are started.
     * </p>
     * 
     * @return {@code true} if all processes on all Pis were successfully stopped
     *         and restarted,
     *         {@code false} if either the stop or start operation failed for any Pi
     */
    public boolean restartAllPis() {
        return stopAllPis() && startAllPis();
    }
}
