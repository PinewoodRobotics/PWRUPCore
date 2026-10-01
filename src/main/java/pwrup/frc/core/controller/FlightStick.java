package pwrup.frc.core.controller;

import java.util.List;
import org.wpilib.command3.button.JoystickButton;
import org.wpilib.driverstation.Joystick;

public class FlightStick extends Joystick {

  /**
   * @implNote the default button enum for the Flightstick
   */
  public enum ButtonEnum {
    A(0),
    B(1),
    X(2),
    Y(3),
    B5(4),
    B6(5),
    B7(6),
    B8(7),
    LEFTSLIDERUP(8),
    LEFTSLIDERDOWN(9),
    RIGHTSLIDERUP(10),
    RIGHTSLIDERDOWN(11),
    MYSTERYBUTTON(12),
    MYSTERYBUTTON2(13),
    SCROLLPRESS(14),
    B16(15),
    B17(16),
    TRIGGER(17),
    B19(18),
    XBOX(19),
    SCREENSHARE(20),
    UPLOAD(21);

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
      return ("ButtonEnum{" + "name='" + name() + '\'' + ", intValue=" + value + '}');
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
    JOYSTICKY(0),
    JOYSTICKX(1),
    JOYSTICKROTATION(2),
    H2X(3),
    H2Y(4),
    LEFTSLIDER(5),
    SCROLLWHEEL(6),
    RIGHTSLIDER(7);

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
      return ("ButtonEnum{" + "name='" + name() + '\'' + ", intValue=" + value + '}');
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
   * @throws NoChannelFoundException if the channel is invalid that means that
   *                                 some code upstairs is buggy and needs to be
   *                                 fixed
   */
  public FlightStick(int port) {
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

  public JoystickButton A() {
    return new JoystickButton(FlightStick.this, FlightStick.ButtonEnum.A.value);
  }

  public JoystickButton B() {
    return new JoystickButton(FlightStick.this, FlightStick.ButtonEnum.B.value);
  }

  public JoystickButton X() {
    return new JoystickButton(FlightStick.this, FlightStick.ButtonEnum.X.value);
  }

  public JoystickButton Y() {
    return new JoystickButton(FlightStick.this, FlightStick.ButtonEnum.Y.value);
  }

  public JoystickButton B5() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B5.value);
  }

  public JoystickButton B6() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B6.value);
  }

  public JoystickButton B7() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B7.value);
  }

  public JoystickButton B8() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B8.value);
  }

  public JoystickButton leftSliderUp() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.LEFTSLIDERUP.value);
  }

  public JoystickButton leftSliderDown() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.LEFTSLIDERDOWN.value);
  }

  public JoystickButton rightSliderUp() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.RIGHTSLIDERUP.value);
  }

  public JoystickButton rightSliderDown() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.RIGHTSLIDERDOWN.value);
  }

  public JoystickButton mysteryButton() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.MYSTERYBUTTON.value);
  }

  public JoystickButton mysteryButton2() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.MYSTERYBUTTON2.value);
  }

  public JoystickButton scrollPress() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.SCROLLPRESS.value);
  }

  public JoystickButton B16() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B16.value);
  }

  public JoystickButton B17() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B17.value);
  }

  public JoystickButton trigger() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.TRIGGER.value);
  }

  public JoystickButton B19() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.B19.value);
  }

  public JoystickButton XBOX() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.XBOX.value);
  }

  public JoystickButton screenshare() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.SCREENSHARE.value);
  }

  public JoystickButton upload() {
    return new JoystickButton(
        FlightStick.this,
        FlightStick.ButtonEnum.UPLOAD.value);
  }

  /**
   * @orientation +x => forward, -x => backward
   * @return the x value of the joystick in the custom coordinate system outlined
   *         above
   */
  public double getJoystickX() {
    return -FlightStick.this.getRawAxis(FlightStick.AxisEnum.JOYSTICKX.value);
  }

  /**
   * @orientation +y => left, -y => right
   * @return the y value of the joystick in the custom coordinate system outlined
   *         above
   */
  public double getJoystickY() {
    return -FlightStick.this.getRawAxis(FlightStick.AxisEnum.JOYSTICKY.value);
  }

  public double getJoystickRotation() {
    return -FlightStick.this.getRawAxis(
        FlightStick.AxisEnum.JOYSTICKROTATION.value);
  }

  public double getH2X() {
    return FlightStick.this.getRawAxis(FlightStick.AxisEnum.H2X.value);
  }

  public double getH2Y() {
    return FlightStick.this.getRawAxis(FlightStick.AxisEnum.H2Y.value);
  }

  public double getLeftSlider() {
    return FlightStick.this.getRawAxis(FlightStick.AxisEnum.LEFTSLIDER.value);
  }

  public double getScrollWheel() {
    return FlightStick.this.getRawAxis(FlightStick.AxisEnum.SCROLLWHEEL.value);
  }

  public double getRightSlider() {
    return FlightStick.this.getRawAxis(FlightStick.AxisEnum.RIGHTSLIDER.value);
  }
}
