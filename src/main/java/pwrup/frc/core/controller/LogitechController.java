package pwrup.frc.core.controller;

import java.util.List;
import org.wpilib.command3.button.JoystickButton;
import org.wpilib.driverstation.Joystick;

/**
 * @author goofball06
 * @co-author godbrigero
 */
public class LogitechController extends Joystick {

  /**
   * @implNote the default button enum for the logitec controller
   */
  public enum ButtonEnum {
    X(0),
    A(1),
    B(2),
    Y(3),
    LEFTBUTTON(4),
    RIGHTBUTTON(5),
    LEFTTRIGGER(6),
    RIGHTTRIGGER(7),
    BACKBUTTON(8),
    STARTBUTTON(9),
    LEFTJOYSTICKBUTTON(10),
    RIGHTJOYSTICKBUTTON(11);

    public int value;

    /**
     * @param val for enum setting
     */
    ButtonEnum(int val) {
      this.value = val;
    }

    /**
     * @apiNote stringifies the enum
     */
    @Override
    public String toString() {
      return (
        "ButtonEnum{" + "name='" + name() + '\'' + ", intValue=" + value + '}'
      );
    }

    /**
     * @apiNote DO NOT SET VALUES! THIS IS ONLY FOR CUSTOM CONFIGS!
     * @param newVal the new value of the enum
     */
    public void setValue(int newVal) {
      this.value = newVal;
    }
  }

  /**
   * @apiNote this is for axis channels in a controller for the logitec
   */
  public enum AxisEnum {
    LEFTJOYSTICKX(0),
    LEFTJOYSTICKY(1),
    RIGHTJOYSTICKX(2),
    RIGHTJOYSTICKY(3);

    public int value;

    /**
     * @param val for setting the port
     */
    AxisEnum(int val) {
      this.value = val;
    }

    /**
     * @apiNote stringifies the enum
     */
    @Override
    public String toString() {
      return (
        "ButtonEnum{" + "name='" + name() + '\'' + ", intValue=" + value + '}'
      );
    }

    /**
     * @apiNote DO NOT SET VALUES! THIS IS ONLY FOR CUSTOM CONFIGS!
     * @param newVal the new value of the enum
     */
    public void setValue(int newVal) {
      this.value = newVal;
    }
  }

  /**
   * @param port the port of the controller
   * @throws NoChannelFoundException if the channel is invalid that means that some code upstairs is buggy and needs to be fixed
   */
  public LogitechController(int port) /*throws NoChannelFoundException*/{
    super(port);
    /*if (port < 0) {
      throw new NoChannelFoundException(port);
    }*/
  }

  /**
   * @param values new values
   */
  public void setValues(List<Integer> values) {
    for (int i = 0; i < values.size(); i++) {
      if (i < 12) {
        ButtonEnum.values()[i].setValue(values.get(i));
      } else {
        AxisEnum.values()[i].setValue(values.get(i));
      }
    }
  }

  public JoystickButton X() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.X.value
    );
  }

  public JoystickButton A() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.A.value
    );
  }

  public JoystickButton B() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.B.value
    );
  }

  public JoystickButton Y() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.Y.value
    );
  }

  public JoystickButton leftButton() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.LEFTBUTTON.value
    );
  }

  public JoystickButton rightButton() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.RIGHTBUTTON.value
    );
  }

  public JoystickButton leftTrigger() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.LEFTTRIGGER.value
    );
  }

  public JoystickButton rightTrigger() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.RIGHTTRIGGER.value
    );
  }

  public JoystickButton backButton() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.BACKBUTTON.value
    );
  }

  public JoystickButton startButton() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.STARTBUTTON.value
    );
  }

  public JoystickButton leftJoystickButton() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.LEFTJOYSTICKBUTTON.value
    );
  }

  public JoystickButton rightJoystickButton() {
    return new JoystickButton(
      LogitechController.this,
      LogitechController.ButtonEnum.RIGHTJOYSTICKBUTTON.value
    );
  }

  public double getLeftJoystickX() {
    return LogitechController.this.getRawAxis(
        LogitechController.AxisEnum.LEFTJOYSTICKX.value
      );
  }

  public double getLeftJoystickY() {
    return LogitechController.this.getRawAxis(
        LogitechController.AxisEnum.LEFTJOYSTICKY.value
      );
  }

  public double getRightJoystickX() {
    return LogitechController.this.getRawAxis(
        LogitechController.AxisEnum.RIGHTJOYSTICKX.value
      );
  }

  public double getRightJoystickY() {
    return LogitechController.this.getRawAxis(
        LogitechController.AxisEnum.RIGHTJOYSTICKY.value
      );
  }
}
