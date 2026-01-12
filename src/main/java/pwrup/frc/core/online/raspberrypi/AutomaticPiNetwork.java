package pwrup.frc.core.online.raspberrypi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import pwrup.frc.core.constant.RaspberryPiConstants;
import pwrup.frc.core.online.raspberrypi.discovery.PiDiscoveryUtil;
import pwrup.frc.core.online.raspberrypi.discovery.PiInfo;

public class AutomaticPiNetwork<P extends Enum<P> & Comparable<P> & WeightedProcess> extends PiNetwork<P> {
  private static final List<ConstrainedProcess<?>> constrainedProcesses = new ArrayList<>();
  private final int timeoutSeconds;
  private final List<P> processesToRun;
  private final int portAutobahn;
  private final int portComs;

  @SafeVarargs
  public AutomaticPiNetwork(int timeoutSeconds, int portAutobahn, int portComs, P... processesToRun) {
    super();
    this.timeoutSeconds = timeoutSeconds;
    this.processesToRun = List.of(processesToRun);
    this.portAutobahn = portAutobahn;
    this.portComs = portComs;
  }

  public AutomaticPiNetwork(int timeoutSeconds, P... processesToRun) {
    this(timeoutSeconds, RaspberryPiConstants.DEFAULT_PORT_AUTOB, RaspberryPiConstants.DEFAULT_PORT_COM,
        processesToRun);
  }

  public void initialize() {
    try {
      var allInfos = PiDiscoveryUtil.discover(timeoutSeconds);
      for (PiInfo piInfo : allInfos) {
        var systemName = piInfo.getName();
        var processesOnPi = new ArrayList<P>();
        for (ConstrainedProcess<?> constrainedProcess : constrainedProcesses) {
          if (systemName != null && constrainedProcess.getPisToRunOn().contains(systemName)) {
            processesOnPi.add((P) constrainedProcess.getProcessToRun());
          }
        }

        var pi = new RaspberryPi<P>(piInfo.getHostname(), piInfo.getAutobahnPort().orElse(portAutobahn),
            piInfo.getWatchdogPort().orElse(portComs));
        for (P process : processesOnPi) {
          pi.addProcess(process);
        }
        add(pi);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to discover Pis", e);
    } catch (InterruptedException e) {
      throw new RuntimeException("Failed to discover Pis", e);
    }

    for (P process : processesToRun) {
      // Skip processes that are explicitly constrained (already assigned above)
      boolean isConstrained = constrainedProcesses
          .stream()
          .anyMatch(cp -> ((ConstrainedProcess<P>) cp).equalsToProcess(process));
      if (isConstrained) {
        continue;
      }

      var targetPi = getPiForProcess(process);
      targetPi.addProcess(process);
    }
  }

  private HashMap<RaspberryPi<P>, Double> splitAssignedWeightByPi() {
    var weightByPi = new HashMap<RaspberryPi<P>, Double>();
    for (var pi : getPis()) {
      double sum = pi.getProcessesToRun().stream().mapToDouble(WeightedProcess::getWeight).sum();
      // include optional base weight if used
      sum += pi.getWeight();
      weightByPi.put(pi, sum);
    }
    return weightByPi;
  }

  /**
   * Splits Raspberry Pis by their assigned total process weight.
   * 
   * <p>
   * Aggregates the weights of all processes (via
   * {@link WeightedProcess#getWeight()})
   * mapped to each {@link RaspberryPi} and returns a mapping of Pi to its total
   * assigned weight.
   * </p>
   *
   * @return a map from RaspberryPi to the sum of weights of its assigned
   *         processes
   */
  // Alias kept for clarity; used by getPiForProcess to consider current loads
  private HashMap<RaspberryPi<P>, Double> splitPisByWeight() {
    return splitAssignedWeightByPi();
  }

  /**
   * Resolves which Raspberry Pi should run the given process.
   * 
   */
  private RaspberryPi<P> getPiForProcess(P process) {
    var weightByPi = splitAssignedWeightByPi();

    // Determine eligible Pis based on constraints (if any)
    List<RaspberryPi<P>> candidates = getPis();
    var maybeConstraint = constrainedProcesses
        .stream()
        .filter(cp -> ((ConstrainedProcess<P>) cp).equalsToProcess(process))
        .findFirst();
    if (maybeConstraint.isPresent()) {
      var allowed = ((ConstrainedProcess<P>) maybeConstraint.get()).getPisToRunOn();
      candidates = candidates
          .stream()
          .filter(pi -> allowed.contains(pi.getHost()))
          .toList();
      if (candidates.isEmpty()) {
        throw new IllegalStateException("No eligible Pis found for constrained process: " + process);
      }
    }

    // Choose the Pi with the least total assigned weight (ties: first)
    RaspberryPi<P> best = null;
    double bestWeight = Double.POSITIVE_INFINITY;
    for (var pi : candidates) {
      double w = weightByPi.getOrDefault(pi, 0.0);
      if (w < bestWeight) {
        bestWeight = w;
        best = pi;
      }
    }

    if (best == null) {
      throw new IllegalStateException("No Pis available to assign process: " + process);
    }
    return best;
  }

  @SafeVarargs
  public static <P extends Enum<P> & Comparable<P> & WeightedProcess> void AddConstrainedProcesses(
      ConstrainedProcess<? extends P>... constrainedProcess) {
    constrainedProcesses.addAll(List.of(constrainedProcess));
  }
}
