package pwrup.frc.core.geometry;

import edu.wpi.first.math.geometry.Translation2d;

public class TranslationMath {

  public static double getNorm2(Translation2d translation) {
    return (translation.getX() *
        translation.getX() +
        translation.getY() *
            translation.getY());
  }

  /**
   * Deadbands joystick input, then scales it from the deadband to 1. Ask Jared
   * for clarification.
   *
   * @param input    the joystick input, [0, 1]
   * @param deadband ignores the input if it is less than this value, [0, 1]
   * @param minValue adds this value if the input overcomes the deadband, [0, 1]
   * @return the return value, [0, 1]
   */
  public static double deadband(
      double input,
      double deadband,
      double minValue) {
    double output;
    double m = (1.0 - minValue) / (1.0 - deadband);

    if (Math.abs(input) < deadband) {
      output = 0;
    } else if (input > 0) {
      output = m * (input - deadband) + minValue;
    } else {
      output = m * (input + deadband) - minValue;
    }

    return output;
  }

  /**
   * Apply a minimum threshold to a value.
   * 
   * @param value    the value to apply the minimum threshold to
   * @param minValue the minimum threshold
   * @return the value if abs(value) is greater than the minimum threshold, 0
   *         otherwise
   */
  public static double applyMinimumThreshold(double value, double minValue) {
    if (Math.abs(value) < minValue) {
      return 0;
    }

    return value;
  }
}
