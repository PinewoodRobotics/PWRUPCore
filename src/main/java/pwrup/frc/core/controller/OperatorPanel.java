package pwrup.frc.core.controller;

import java.util.List;
import org.wpilib.command3.button.JoystickButton;
import org.wpilib.driverstation.Joystick;

public class OperatorPanel extends Joystick {

  /**
   * @implNote the default button enum for the OperatorPanel
   */
  public enum ButtonEnum {
    GREENBUTTON(0),
    REDBUTTON(1),
    BLACKBUTTON(2),
    METALSWITCHDOWN(3),
    TOGGLEWHEELMIDDOWN(4),
    TOGGLEWHEELMIDDLE(5),
    TOGGLEWHEELMIDUP(6),
    TOGGLEWHEELUP(7),
    STICKUP(8),
    STICKDOWN(9);

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
    WHEEL(2);

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
  public OperatorPanel(int port) {
    super(port);
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

  public JoystickButton greenButton() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.GREENBUTTON.value
    );
  }

  public JoystickButton redButton() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.REDBUTTON.value
    );
  }

  public JoystickButton blackButton() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.BLACKBUTTON.value
    );
  }

  public JoystickButton metalSwitchDown() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.METALSWITCHDOWN.value
    );
  }

  public JoystickButton toggleWheelMidDown() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.TOGGLEWHEELMIDDOWN.value
    );
  }

  public JoystickButton toggleWheelMiddle() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.TOGGLEWHEELMIDDLE.value
    );
  }

  public JoystickButton toggleWheelMidUp() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.TOGGLEWHEELMIDUP.value
    );
  }

  public JoystickButton toggleWheelUp() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.TOGGLEWHEELUP.value
    );
  }

  public JoystickButton stickUp() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.STICKUP.value
    );
  }

  public JoystickButton stickDown() {
    return new JoystickButton(
      OperatorPanel.this,
      OperatorPanel.ButtonEnum.STICKDOWN.value
    );
  }

  public double getWheel() {
    return OperatorPanel.this.getRawAxis(OperatorPanel.AxisEnum.WHEEL.value);
  }
}
