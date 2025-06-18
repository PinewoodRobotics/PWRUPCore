package pwrup.frc.core.geometry;

public class RotationMath {

  public static double wrapTo180(double angle) {
    double newAngle = (angle + 180) % 360;
    while (newAngle < 0) {
      newAngle += 360;
    }

    return newAngle - 180;
  }
}
