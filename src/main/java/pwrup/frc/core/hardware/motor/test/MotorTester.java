package pwrup.frc.core.hardware.motor.test;

import java.util.ArrayList;
import pwrup.frc.core.constant.hardware.DefaultMotorConfig;
import pwrup.frc.core.hardware.motor.IMotor;

public class MotorTester<M extends IMotor<C>, C> {

  private M motor;
  private C config;
  private MotorTesterConfig testerConfig;

  public MotorTester(M motor, C config, MotorTesterConfig testerConfig) {
    this.motor = motor;
    this.config = config;
    this.testerConfig = testerConfig;
  }

  public MotorTester(M motor, C config) {
    this(motor, config, DefaultMotorConfig.MotorTesterConfig());
  }

  // TODO: Implement this
  public boolean test() {
    /*
    boolean failure = false;
    System.out.println("////////////////////////////////////////////////");

    ArrayList<Double> currents = new ArrayList<>();
    ArrayList<Double> rpms = new ArrayList<>();

    motor.savePreviousConfig();
    motor.setMotorOutput(config.mMotor, 0.0);

    for (MotorConfig<T> config : motorsToCheck) {
      System.out.println("Checking: " + config.mName);

      setMotorOutput(config.mMotor, checkerConfig.mRunOutputPercentage);
      Timer.delay(checkerConfig.mRunTimeSec);

      // poll the interesting information
      double current = getMotorCurrent(config.mMotor);
      currents.add(current);
      System.out.print("Current: " + current);

      double rpm = Double.NaN;
      if (checkerConfig.mRPMSupplier != null) {
        rpm = checkerConfig.mRPMSupplier.getAsDouble();
        rpms.add(rpm);
        System.out.print(" RPM: " + rpm);
      }
      System.out.print('\n');

      setMotorOutput(config.mMotor, 0.0);

      // perform checks
      if (current < checkerConfig.mCurrentFloor) {
        System.out.println(
          config.mName +
          " has failed current floor check vs " +
          checkerConfig.mCurrentFloor +
          "!!"
        );
        failure = true;
      }
      if (checkerConfig.mRPMSupplier != null) {
        if (rpm < checkerConfig.mRPMFloor) {
          System.out.println(
            config.mName +
            " has failed rpm floor check vs " +
            checkerConfig.mRPMFloor +
            "!!"
          );
          failure = true;
        }
      }

      Timer.delay(checkerConfig.mWaitTimeSec);
    }

    // run aggregate checks

    if (currents.size() > 0) {
      double average = currents
        .stream()
        .mapToDouble(val -> val)
        .average()
        .getAsDouble();

      if (!Util.allCloseTo(currents, average, checkerConfig.mCurrentEpsilon)) {
        System.out.println("Currents varied!!!!!!!!!!!");
        failure = true;
      }
    }

    if (rpms.size() > 0) {
      double average = rpms
        .stream()
        .mapToDouble(val -> val)
        .average()
        .getAsDouble();

      if (!Util.allCloseTo(rpms, average, checkerConfig.mRPMEpsilon)) {
        System.out.println("RPMs varied!!!!!!!!");
        failure = true;
      }
    }

    // restore talon configurations
    restoreConfiguration();

    return !failure; */
    return true;
  }
}
