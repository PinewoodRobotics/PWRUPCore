package pwrup.frc.core.geometry;

import edu.wpi.first.math.geometry.Translation2d;

public class TranslationMath {

  public static double getNorm2(Translation2d translation) {
    return (
      translation.getX() *
      translation.getX() +
      translation.getY() *
      translation.getY()
    );
  }
}
