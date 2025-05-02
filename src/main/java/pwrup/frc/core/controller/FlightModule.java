package pwrup.frc.core.controller;

public class FlightModule {

  public final FlightStick leftFlightStick;
  public final FlightStick rightFlightStick;

  /**
   * Creates a FlightModule, composed of two FlightSticks
   * @param leftPort The left FlightStick Port
   * @param rightPort The Right FlightStick Port
   */
  public FlightModule(int leftPort, int rightPort) {
    leftFlightStick = new FlightStick(leftPort);
    rightFlightStick = new FlightStick(rightPort);
  }

  public FlightModule(FlightStick left, FlightStick right) {
    leftFlightStick = left;
    rightFlightStick = right;
  }
}
