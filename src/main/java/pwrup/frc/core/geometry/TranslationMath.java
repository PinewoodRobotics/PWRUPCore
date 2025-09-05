package pwrup.frc.core.geometry;

import edu.wpi.first.math.geometry.Translation2d;

public class TranslationMath {

  public static double getNorm2(Translation2d translation) {
    return (translation.getX() *
        translation.getX() +
        translation.getY() *
            translation.getY());
  }

  public static Translation2d scaleToLength(
      Translation2d vector,
      double targetLength) {
    if (vector.getNorm() == 0) {
      return new Translation2d(0, 0); // Avoid divide-by-zero
    }

    return vector.div(vector.getNorm()).times(targetLength);
  }
}
