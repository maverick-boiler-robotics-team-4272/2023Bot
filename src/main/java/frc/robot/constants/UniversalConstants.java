package frc.robot.constants;

import edu.wpi.first.math.util.Units;

public class UniversalConstants {
    public static final double PI2 = Math.PI * 2;
    
    public static final double FIELD_WIDTH_METERS = Units.feetToMeters(54.0) + Units.inchesToMeters(3.25);
    public static final double FIELD_LENGTH_METERS = Units.feetToMeters(26.0) + Units.inchesToMeters(3.5);
    public static final double FIELD_HALF_WIDTH = FIELD_WIDTH_METERS / 2.0;
    public static final double FIELD_HALF_LENGTH = FIELD_LENGTH_METERS / 2.0;

    public static final boolean IS_PRACTICE_BOT = false;
}
