package pwrup.frc.core.geometry;

public class RotationMath {

  /**
   * Wraps a value to the range [-0.5, 0.5].
   * First adds 0.5 to shift range to [0,1], then uses modulo to wrap,
   * and shifts back by 0.5.
   *
   * @param in The input value to wrap
   * @return The wrapped value in range [-0.5, 0.5]
   */
  public static double putWithinHalfToHalf(double in) {
    return ((in + 0.5) % 1) - 0.5;
  }

  /**
   * Wraps an angle to the range [-180, 180] degrees.
   * First adds 180 to shift range, then uses modulo to wrap,
   * and shifts back by 180.
   *
   * @param angle The input angle in degrees
   * @return The wrapped angle in range [-180, 180] degrees
   */
  public static double wrapTo180(double angle) {
    double newAngle = ((angle + 180) % 360 + 360) % 360;
    return newAngle - 180;
  }

  /**
   * Calculates the smallest angle difference between two angles, normalized to
   * -180 to 180 degrees.
   * First normalizes both input angles to -180 to 180 range, then finds the
   * shortest path between them.
   *
   * @param angle1 First angle in degrees
   * @param angle2 Second angle in degrees
   * @return The absolute smallest angle difference between the angles, in range
   *         [0,180]
   */
  public static double angleDifference180(double angle1, double angle2) {
    // Normalize angles to -180 to 180 range
    angle1 = ((angle1 + 180) % 360) - 180;
    angle2 = ((angle2 + 180) % 360) - 180;

    double diff = Math.abs(angle1 - angle2);

    return Math.min(diff, 360 - diff);
  }
}
