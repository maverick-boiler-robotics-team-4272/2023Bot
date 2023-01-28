// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final double PI2 = Math.PI * 2;
    public static final double VOLTAGE_COMPENSATION = 11.0;

    public static final double FIELD_HALF_WIDTH = Units.feetToMeters(54.0 / 2.0);
    public static final double FIELD_HALF_HEIGHT = Units.feetToMeters(27.0 / 2.0);

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

    public static class DrivetrainConstants {
        public static final double WHEEL_DISTANCE = Units.feetToMeters(1.0);

        public static final double MAX_TRANS_SPEED = 2.5; // 2.5 meters per second
        public static final double MAX_ROT_SPEED = PI2 * 3.5; // 1.5 rotations per second
        public static final double MAX_MODULE_SPEED = Units.feetToMeters(14.5); // 14.5 feet per second
    }

    public static class Paths {
        public static PathPlannerTrajectory TEST_PATH = PathPlanner.loadPath("New Path", DrivetrainConstants.MAX_TRANS_SPEED, 2.0);
    }

    public static class PathUtils {
        public static final PIDController X_CONTROLLER = new PIDController(0.4, 0.015, 0.0);
        public static final PIDController Y_CONTROLLER = new PIDController(0.4, 0.015, 0.0);
        public static final ProfiledPIDController THETA_CONTROLLER = new ProfiledPIDController(1.75, 0.015, 0.0, new TrapezoidProfile.Constraints(DrivetrainConstants.MAX_ROT_SPEED, 1.5));

        static {
            THETA_CONTROLLER.enableContinuousInput(-Math.PI, Math.PI);
        }

        public static boolean posesEqual(Pose2d a, Pose2d b, double delta) {
            return Math.abs(a.getX() - b.getX()) < delta &&
                   Math.abs(a.getY() - b.getY()) < delta &&
                   Math.abs(a.getRotation().getRadians() - b.getRotation().getRadians()) < delta;
        }
    }
}