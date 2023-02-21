package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class RobotConstants {

    public static class DrivetrainConstants {
        public static final double WHEEL_DISTANCE = Units.feetToMeters(1.0);

        public static final double MAX_TRANS_SPEED = 3.5; // 2.5 meters per second
        public static final double MAX_ROT_SPEED = UniversalConstants.PI2 * 3.5; // 1.5 rotations per second
        public static final double MAX_MODULE_SPEED = Units.feetToMeters(14.5); // 14.5 feet per second
        public static final double MAX_TRANS_ACCEL = 1.0;
        public static final double MAX_ROT_ACCEL = 1.5;

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
            public static final double ELEVATOR_PID_P = 2.55;
            public static final double ELEVATOR_PID_I = 0.0;
            public static final double ELEVATOR_PID_D = 0.0;
            public static final double ELEVATOR_PID_F = 0.1;

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
            public static final double ROTARY_ARM_PID_P = 0.3;
            public static final double ROTARY_ARM_PID_I = 0.0;
            public static final double ROTARY_ARM_PID_D = 0.0;
            public static final double ROTARY_ARM_PID_F = 0.02;

            public static final double ROTARY_ARM_PID_I_ZONE = 0.0;
            public static final double ROTARY_ARM_PID_D_FILTER = 0.0;

            public static final double ROTARY_ARM_PID_OUTPUT_MIN = -0.5;
            public static final double ROTARY_ARM_PID_OUTPUT_MAX = 0.5;

            public static final double ROTARY_ARM_SMART_MOTION_MAX_ACCEL = 0.1;
            public static final double ROTARY_ARM_SMART_MOTION_MAX_SPEED = 2.0;

            public static final double ARM_GEAR_RATIO = 96.0 / 1.0;
            public static final double MAVCODER_RATIO = 58.0 / 26.0;

            public static final double MIN_ARM_ANGLE = -120.0;
            public static final double MAX_ARM_ANGLE = 0.0;
        }
        
        public static enum ArmSetpoints {
            LOW_CUBE(Rotation2d.fromDegrees(-54), Units.inchesToMeters(13.780), false),
            HIGH_CUBE(Rotation2d.fromDegrees(-75), Units.inchesToMeters(38.189), false),
            GROUND_CUBE(Rotation2d.fromDegrees(-120), Units.inchesToMeters(5.906), false),
            HYBRID_CUBE(Rotation2d.fromDegrees(0), Units.inchesToMeters(0.000), false),
    
            LOW_CONE(Rotation2d.fromDegrees(-54), Units.inchesToMeters(13.780), false),
            HIGH_CONE(Rotation2d.fromDegrees(-75), Units.inchesToMeters(38.189), false),
            GROUND_CONE(Rotation2d.fromDegrees(-102.5), Units.inchesToMeters(0.000), false),
            HUMAN_PLAYER_CONE(Rotation2d.fromDegrees(-75), Units.inchesToMeters(35.433), false),
            HYBRID_CONE(Rotation2d.fromDegrees(-100), Units.inchesToMeters(0.000), false),
    
            
            HOME(Rotation2d.fromDegrees(-15), Units.inchesToMeters(0.000), false),
            SAFE_ARM(Rotation2d.fromDegrees(-40), Units.inchesToMeters(0.000), false);
            
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

    public static class ClawConstants {

    }
}
