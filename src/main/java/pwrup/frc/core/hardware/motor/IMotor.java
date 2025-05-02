package pwrup.frc.core.hardware.motor;

public interface IMotor<MotorConfig> {
  public void setSpeed(double speed);

  default void setSpeed(double speed, double voltage) {
    setSpeed(speed);
    setVoltage(voltage);
  }

  public void setVoltage(double voltage);

  public double getMotorCurrent();

  public void setMotorConfig(MotorConfig config);

  public void savePreviousConfig();
}
