package pwrup.frc.core.hardware.motor.test;

import java.util.function.DoubleSupplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotorTesterConfig {

  private double mCurrentFloor = 5;
  private double mRPMFloor = 2000;

  private double mCurrentEpsilon = 5.0;
  private double mRPMEpsilon = 500;
  private DoubleSupplier mRPMSupplier = null;

  private double mRunTimeSec = 4.0;
  private double mWaitTimeSec = 2.0;
  private double mRunOutputPercentage = 0.5;
}
