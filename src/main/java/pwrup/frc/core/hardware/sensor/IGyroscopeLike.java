package pwrup.frc.core.hardware.sensor;

import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Rotation3d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.math.geometry.Translation3d;
import org.wpilib.math.kinematics.ChassisVelocities;

public interface IGyroscopeLike {

  public ChassisVelocities getVelocity();

  public ChassisVelocities getAcceleration();

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
