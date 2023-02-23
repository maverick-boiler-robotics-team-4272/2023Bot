package frc.robot.constants;

import edu.wpi.first.math.util.Units;

public class UniversalConstants {
    public static final double PI2 = Math.PI * 2;
    public static final int NOMINAL_VOLTAGE = 11;
    
    public static final double FIELD_WIDTH_METERS = Units.feetToMeters(54.0);
    public static final double FIELD_HEIGHT_METERS = Units.feetToMeters(27.0);
    public static final double FIELD_HALF_WIDTH = FIELD_WIDTH_METERS / 2.0;
    public static final double FIELD_HALF_HEIGHT = FIELD_HEIGHT_METERS / 2.0;
}
