package frc.robot.constants;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.utils.ArmSetpoint;

import static frc.robot.constants.UniversalConstants.IS_PRACTICE_BOT;
public class RobotConstants {

    public static final int NOMINAL_VOLTAGE = 11;
    public static final int DEFAULT_CURRENT_LIMIT = 20;

    public static class DrivetrainConstants {
        public static final double WHEEL_DISTANCE = Units.feetToMeters(1.0);

        public static final double MAX_TRANS_SPEED = 4.0; // 4.0 meters per second
        public static final double MAX_ROT_SPEED = UniversalConstants.PI2 * 2.0; // 1.5 rotations per second
        public static final double MAX_MODULE_SPEED = 0.0254 * (14.5 * 12); // 14.5 feet per second

        public static final double MAX_AUTO_SPEED = 4.0;
        public static final double MAX_AUTO_ACCEL = 3.0;

        public static final double FRONT_LEFT_OFFSET = IS_PRACTICE_BOT ? 0.0 : 110.0;
        public static final double FRONT_RIGHT_OFFSET = IS_PRACTICE_BOT ? 0.0 : 91.0;
        public static final double BACK_LEFT_OFFSET = IS_PRACTICE_BOT ? 0.0 : 166.0;
        public static final double BACK_RIGHT_OFFSET = IS_PRACTICE_BOT ? 0.0 : 316.0;

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

            public static final double ELEVATOR_PID_OUTPUT_MIN = -0.5;
            public static final double ELEVATOR_PID_OUTPUT_MAX = 1.0;

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
            // Any offset less than 120 degrees is generally going to be bad
            // To fix this, reset encoder position or chain to make the offset greater than 120
            public static final double ROTARY_ARM_OFFSET = IS_PRACTICE_BOT ? 192.0 : 199.0;

            public static final double ROTARY_ARM_PID_P = IS_PRACTICE_BOT ? 0.012 : 0.014;
            public static final double ROTARY_ARM_PID_I = IS_PRACTICE_BOT ? 0.001 : 0.0;
            public static final double ROTARY_ARM_PID_D = IS_PRACTICE_BOT ? 0.0 : 0.0;
            public static final double ROTARY_ARM_PID_F = IS_PRACTICE_BOT ? 0.05 : 0.045;
            
            public static final double ROTARY_ARM_PID_I_ZONE = 12.0;
            public static final double ROTARY_ARM_PID_D_FILTER = 0.0;

            public static final double ROTARY_ARM_PID_OUTPUT_MIN = -0.5;
            public static final double ROTARY_ARM_PID_OUTPUT_MAX = 0.5;

            public static final double ROTARY_ARM_SMART_MOTION_MAX_ACCEL = 400.0;
            public static final double ROTARY_ARM_SMART_MOTION_MAX_SPEED = 800.0;

            public static final double ARM_GEAR_RATIO = 96.0 / 1.0;

            public static final double MIN_ARM_ANGLE = -120.0;
            public static final double MAX_ARM_ANGLE = 15.0;

            public static double getSafeArmSpeed(ArmSetpoint setpoint) {
                // Physics Math: Uniform acceleration equation
                // omega final squared = omega initial squared + 2 * alpha * delta theta
                // Omega is angular velocity (deg / s)
                // Alpha is angular acceleration (deg / s ^ 2)
                // delta theta is change in angle (deg)
                // Want to profile down to zero, so omega final is zero
                // So, final omega initial is sqrt(2 * alpha * delta theta)
                //
                // Coding Decisions
                // absolute value of the delta theta so we dont need to worry about sign
                // since the angles can sometimes both be negative which leads to possible negatives 
                // under the square root 
                // Also absolute valuing so we can get a velocity that the safe arm can get up
                // to for setpoints on either side of the safe arm.
                // Motion profiling will prevent it from accelerating too fast or getting to high,
                // So absurd values for angles very far away from the safe arm setpoint are not only expected,
                // but also handled
                return Math.sqrt(2 * ROTARY_ARM_SMART_MOTION_MAX_ACCEL * Math.abs(setpoint.getArmAngle().getDegrees() - ArmSetpoints.SAFE_ARM.getArmAngle().getDegrees()));
            }
        }
        
        public static enum ArmSetpoints implements ArmSetpoint {
            MID_CUBE(Rotation2d.fromDegrees(-83), Units.inchesToMeters(0.000), false),
            HIGH_CUBE(Rotation2d.fromDegrees(-35), Units.inchesToMeters(30.000), false),
            GROUND_CUBE(Rotation2d.fromDegrees(6), Units.inchesToMeters(4), false),
            HYBRID_CUBE(Rotation2d.fromDegrees(0), Units.inchesToMeters(0.000), false),

            GROUND_AUTO_CUBE(Rotation2d.fromDegrees(6), Units.inchesToMeters(3), false),

            MID_CONE(Rotation2d.fromDegrees(-51), Units.inchesToMeters(14.000), false),
            //HIGH_CONE(Rotation2d.fromDegrees(-25), Units.inchesToMeters(41.000), false),
            HIGH_CONE(Rotation2d.fromDegrees(-20), Units.inchesToMeters(42.000), false),
            GROUND_CONE(Rotation2d.fromDegrees(-8), Units.inchesToMeters(0.000), false),
            HUMAN_PLAYER_CONE(Rotation2d.fromDegrees(-25), Units.inchesToMeters(37.000), false),
            HYBRID_CONE(Rotation2d.fromDegrees(-100), Units.inchesToMeters(0.000), false),
    

            LAUNCH_CUBE(Rotation2d.fromDegrees(-80), Units.inchesToMeters(25.000), false),
            

            STOWED(Rotation2d.fromDegrees(-108), Units.inchesToMeters(0.000), false),
            SAFE_ARM(Rotation2d.fromDegrees(-70), Units.inchesToMeters(0.000), false),
            ZERO(Rotation2d.fromDegrees(0), 0, false),
            BACK(Rotation2d.fromDegrees(-120), Units.inchesToMeters(0), false);
            
            private final Rotation2d armAngle;
            private final double elevatorHeightMeters;
            private final boolean safetyOverride;
            private ArmSetpoints(Rotation2d armAngle, double elevatorHeightMeters, boolean safetyOverride) {
                this.armAngle = armAngle;
                this.elevatorHeightMeters = elevatorHeightMeters;
                this.safetyOverride = safetyOverride;
            }

            @Override
            public double getElevatorHeight() {
                return elevatorHeightMeters;
            }

            @Override
            public Rotation2d getArmAngle() {
                return armAngle;
            }

            @Override
            public boolean getSafetyOverride() {
                return safetyOverride;
            }

            @Override
            public String toString() {
                return String.format("%s(height = %.2f, angle = %.2fdeg)", name(), elevatorHeightMeters, armAngle.getDegrees());
            }
    
            public static boolean isSetpointSafe(ArmSetpoints setpoint) {
                return setpoint.armAngle.getDegrees() > SAFE_ARM.armAngle.getDegrees() || setpoint.safetyOverride;
            }
        }
    }

    public static class IntakeConstants {
        public static final int CONE_FRONT_LIMIT = 30;
        public static final int CONE_BACK_LIMIT = 30;
        
        public static final int CUBE_FRONT_LIMIT = 25;
        public static final int CUBE_BACK_LIMIT = 8;

        public static final double CUBE_BACK_SPEED_MULT = 0.6;
    }
}
