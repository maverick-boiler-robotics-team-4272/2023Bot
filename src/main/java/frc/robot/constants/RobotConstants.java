package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

import static frc.robot.constants.UniversalConstants.IS_PRACTICE_BOT;
public class RobotConstants {

    public static final int NOMINAL_VOLTAGE = 11;
    public static final int DEFAULT_CURRENT_LIMIT = 20;

    public static class DrivetrainConstants {
        public static final double WHEEL_DISTANCE = Units.feetToMeters(1.0);

        public static final double MAX_TRANS_SPEED = 4.0; // 4.0 meters per second
        public static final double MAX_ROT_SPEED = UniversalConstants.PI2 * 2.0; // 1.5 rotations per second
        public static final double MAX_MODULE_SPEED = Units.feetToMeters(14.5); // 14.5 feet per second
        public static final double MAX_TRANS_ACCEL = 1.0;
        public static final double MAX_ROT_ACCEL = 1.5;

        public static final double FRONT_LEFT_OFFSET = IS_PRACTICE_BOT ? 112.0 : 210.0;
        public static final double FRONT_RIGHT_OFFSET = IS_PRACTICE_BOT ? 93.0 : 344.0;
        public static final double BACK_LEFT_OFFSET = IS_PRACTICE_BOT ? 227.0 : 86.0;
        public static final double BACK_RIGHT_OFFSET = IS_PRACTICE_BOT ? 317.0 : 250.0;

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

    public static class ArmSubsystemConstants {

        public static class ElevatorConstants {
            public static final double ELEVATOR_PID_P = 3.5;
            public static final double ELEVATOR_PID_I = 0.0;
            public static final double ELEVATOR_PID_D = 0.0;
            public static final double ELEVATOR_PID_F = 0.8;

            public static final double ELEVATOR_PID_I_ZONE = 0.0;
            public static final double ELEVATOR_PID_D_FILTER = 0.0;

            public static final double ELEVATOR_PID_OUTPUT_MIN = -0.25;
            public static final double ELEVATOR_PID_OUTPUT_MAX = 0.6;

            public static final double SPROCKET_REV_TO_IN_RATIO = 1.92 * Math.PI;
            public static final double MOTOR_TO_SPROCKET_RATIO = 8.0 / 1.0;
            public static final double CASCADE_RATIO = 2.0 / 1.0;

            public static final double ELEVATOR_SMART_MOTION_MAX_ACCEL = 0.1;
            public static final double ELEVATOR_SMART_MOTION_MAX_SPEED = 0.5;

            // Hardware max is 49 inches, choosing 46 inches for safety
            public static final double MAX_ELEVATOR_DISTANCE = Units.inchesToMeters(46.0);
            public static final double MIN_ELEVATOR_DISTANCE = 0.0;
        }

        public static class RotaryArmConstants {
            public static final double ROTARY_ARM_OFFSET = IS_PRACTICE_BOT ? 304.0 : 307.0;

            public static final double ROTARY_ARM_PID_P = IS_PRACTICE_BOT ? 0.011 : 0.012;
            public static final double ROTARY_ARM_PID_I = IS_PRACTICE_BOT ? 0.001 : 0.001;
            public static final double ROTARY_ARM_PID_D = IS_PRACTICE_BOT ? 0.0 : 0.0;
            public static final double ROTARY_ARM_PID_F = IS_PRACTICE_BOT ? 0.05 : 0.04;

            public static final double ROTARY_ARM_PID_I_ZONE = 12.0;
            public static final double ROTARY_ARM_PID_D_FILTER = 0.0;

            public static final double ROTARY_ARM_PID_OUTPUT_MIN = -0.5;
            public static final double ROTARY_ARM_PID_OUTPUT_MAX = 0.5;

            public static final double ROTARY_ARM_SMART_MOTION_MAX_ACCEL = 0.1;
            public static final double ROTARY_ARM_SMART_MOTION_MAX_SPEED = 2.0;

            public static final double ARM_GEAR_RATIO = 96.0 / 1.0;

            public static final double MIN_ARM_ANGLE = -120.0;
            public static final double MAX_ARM_ANGLE = 15.0;
        }
        
        public static enum ArmSetpoints {
            MID_CUBE(Rotation2d.fromDegrees(-80), Units.inchesToMeters(0.000), false),
            HIGH_CUBE(Rotation2d.fromDegrees(-35), Units.inchesToMeters(30.000), false),
            GROUND_CUBE(Rotation2d.fromDegrees(6), Units.inchesToMeters(4), false),
            HYBRID_CUBE(Rotation2d.fromDegrees(0), Units.inchesToMeters(0.000), false),
    
            MID_CONE(Rotation2d.fromDegrees(-51), Units.inchesToMeters(14.000), false),
            HIGH_CONE(Rotation2d.fromDegrees(-25), Units.inchesToMeters(41.000), false),
            GROUND_CONE(Rotation2d.fromDegrees(-10), Units.inchesToMeters(0.000), false),
            HUMAN_PLAYER_CONE(Rotation2d.fromDegrees(-25), Units.inchesToMeters(37.000), false),
            HYBRID_CONE(Rotation2d.fromDegrees(-100), Units.inchesToMeters(0.000), false),
    
            LAUNCH_CUBE(Rotation2d.fromDegrees(-50), Units.inchesToMeters(0.000), false),
            
            HOME(Rotation2d.fromDegrees(-108), Units.inchesToMeters(0.000), false),
            SAFE_ARM(Rotation2d.fromDegrees(-60), Units.inchesToMeters(0.000), false),
            ZERO(Rotation2d.fromDegrees(0), 0, false),
            TEST_FORWARD(Rotation2d.fromDegrees(-20), 0, false),
            TEST_BACKWARD(Rotation2d.fromDegrees(-90), 0, false);
            
            public final Rotation2d armAngle;
            public final double elevatorHeightMeters;
            public final boolean safetyOverride;
            private ArmSetpoints(Rotation2d armAngle, double elevatorHeightMeters, boolean safetyOverride) {
                this.armAngle = armAngle;
                this.elevatorHeightMeters = elevatorHeightMeters;
                this.safetyOverride = safetyOverride;
            }
    
            public static boolean isSetpointSafe(ArmSetpoints setpoint) {
                return setpoint.armAngle.getDegrees() > SAFE_ARM.armAngle.getDegrees() || setpoint.safetyOverride;
            }
        }
    }

    public static class IntakeConstants {
        public static final int CONE_FRONT_LIMIT = 20;
        public static final int CONE_BACK_LIMIT = 20;
        
        public static final int CUBE_FRONT_LIMIT = 25;
        public static final int CUBE_BACK_LIMIT = 8;

        public static final double CUBE_BACK_SPEED_MULT = 0.6;
    }
}
