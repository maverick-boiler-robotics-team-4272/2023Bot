package frc.robot.constants;

import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.TrajectoryContainer;
import frc.team4272.globals.MathUtils;

public class AutoConstants {
    public static final SendableChooser<Supplier<Command>> AUTO_CHOOSER = new SendableChooser<>();
    public static final SendableChooser<TrajectoryContainer> CONTAINER_CHOOSER = new SendableChooser<>();

    public static class Paths {
        public static final TrajectoryContainer RED_TRAJECTORIES = new TrajectoryContainer("Red");
        public static final TrajectoryContainer BLUE_TRAJECTORIES = new TrajectoryContainer("Blue");

        private static TrajectoryContainer globalTrajectories = null;

        public static void setGlobalTrajectories(TrajectoryContainer trajectories) {
            if(globalTrajectories != null) {
                throw new IllegalCallerException("Method has already been called");
            }

            globalTrajectories = trajectories;
        }

        public static TrajectoryContainer getGlobalTrajectories() {
            if(globalTrajectories == null) {
                throw new IllegalCallerException("Set Global Trajectories method has not been called yet");
            }

            return globalTrajectories;
        }

        public static boolean hasGlobalTrajectories() {
            return globalTrajectories != null;
        }
    }

    public static class PathUtils {
        public static final PIDController X_CONTROLLER = new PIDController(1.5, 0.015, 0.0);
        public static final PIDController Y_CONTROLLER = new PIDController(1.5, 0.015, 0.0);
        public static final PIDController THETA_CONTROLLER = new PIDController(1.5, 0.015, 0.0);

        static {
            THETA_CONTROLLER.enableContinuousInput(-Math.PI, Math.PI);
        }

        public static boolean posesEqual(Pose2d a, Pose2d b, double delta) {
            return Math.abs(a.getX() - b.getX()) < delta &&
                   Math.abs(a.getY() - b.getY()) < delta &&
                   Math.abs(MathUtils.inputModulo(a.getRotation().getRadians() - b.getRotation().getRadians(), -Math.PI, Math.PI)) < delta;
        }
    }
}