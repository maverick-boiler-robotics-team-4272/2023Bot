package frc.robot.constants;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.RobotConstants.DrivetrainConstants;

public class AutoConstants {
    public static final SendableChooser<Command> AUTO_CHOOSER = new SendableChooser<>();

    public static class Paths {
        private static final PathPlannerTrajectory loadPath(String name) {
            return PathPlanner.loadPath((TelemetryConstants.FMS.RED_ALLIANCE.get() ? "Red " : "Blue ") + name, DrivetrainConstants.MAX_TRANS_SPEED, 2.0);
        }

        public static final PathPlannerTrajectory TEST_PATH = loadPath("Test Path");
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
