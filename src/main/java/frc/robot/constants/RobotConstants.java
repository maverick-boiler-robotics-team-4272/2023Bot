package frc.robot.constants;

import edu.wpi.first.math.util.Units;

public class RobotConstants {

    public static class DrivetrainConstants {
        public static final double WHEEL_DISTANCE = Units.feetToMeters(1.0);

        public static final double MAX_TRANS_SPEED = 2.5; // 2.5 meters per second
        public static final double MAX_ROT_SPEED = UniversalConstants.PI2 * 3.5; // 1.5 rotations per second
        public static final double MAX_MODULE_SPEED = Units.feetToMeters(14.5); // 14.5 feet per second

        public static class SwerveModuleConstants {
            public static final double WHEEL_RADIUS = 2.0;
            public static final double DRIVE_RATIO = 6.75;
            public static final double STEER_RATIO = 150.0 / 7.0;
            public static final double MODULE_ROTATION_DEADZONE = 4.0;
        
            public static final double DRIVE_P = 0.003596;
            public static final double DRIVE_I = 0.0;
            public static final double DRIVE_D = 0.0;
            public static final double DRIVE_F = 0.6;
        
            public static final double STEER_P = 0.01;
            public static final double STEER_I = 0.0001;
            public static final double STEER_D = 0.0;
            public static final double STEER_F = 0.0;
        }
    }

    public static class ElevatorConstants {
        public static final double ELEVATOR_PID_P = 1.0;
        public static final double ELEVATOR_PID_I = 0.0;
        public static final double ELEVATOR_PID_D = 0.0;
    }

    public static class RotaryArmConstants {
        public static final double ARM_PID_P = 1.0;
        public static final double ARM_PID_I = 0.0;
        public static final double ARM_PID_D = 0.0;
    }

    public static class ClawConstants {

    }
}
