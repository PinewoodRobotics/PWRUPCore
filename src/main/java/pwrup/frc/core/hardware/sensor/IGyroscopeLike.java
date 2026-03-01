package pwrup.frc.core.hardware.sensor;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public interface IGyroscopeLike {

  public ChassisSpeeds getVelocity();

  public ChassisSpeeds getAcceleration();

  public Rotation3d getRotation();

  public void resetRotation(Rotation3d newRotation);

  // Default methods

  public default void resetRotation(Rotation2d newRotation) {
    resetRotation(new Rotation3d(0, 0, newRotation.getRadians()));
  }

  public default Rotation2d getRotation2d() {
    return getRotation().toRotation2d();
  }
}
