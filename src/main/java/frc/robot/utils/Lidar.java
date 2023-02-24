package frc.robot.utils;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycle;

public class Lidar {
    private DutyCycle dutyCycle;

    public Lidar(int port) {
        dutyCycle = new DutyCycle(new DigitalInput(port));
    }

    public double getRawDutyCycle() {
        return dutyCycle.getOutput();
    }

    public double getMillimeters() {
        return getRawDutyCycle() * 4000.0;
    }

    public double getCentimeters() {
        return getMillimeters() / 10.0;
    }

    public double getInches() {
        return Units.metersToInches(getMillimeters() / 1000.0);
    }

    public double getFeet() {
        return Units.metersToFeet(getMillimeters() / 1000.0);
    }

    public double getMeters() {
        return getMillimeters() / 1000.0;
    }
}
