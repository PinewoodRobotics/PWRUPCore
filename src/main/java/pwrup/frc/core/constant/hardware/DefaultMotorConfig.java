package pwrup.frc.core.constant.hardware;

import pwrup.frc.core.hardware.motor.test.MotorTesterConfig;

public class DefaultMotorConfig {

  public static MotorTesterConfig MotorTesterConfig() {
    var config = new MotorTesterConfig();

    config.setMCurrentEpsilon(5);
    config.setMRPMFloor(2000);
    config.setMRPMEpsilon(500);
    config.setMRPMSupplier(null);
    config.setMRunTimeSec(4.0);
    config.setMWaitTimeSec(2.0);
    config.setMRunOutputPercentage(0.5);

    return config;
  }
}
