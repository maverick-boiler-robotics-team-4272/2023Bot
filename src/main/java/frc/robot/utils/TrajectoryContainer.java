package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;

public class TrajectoryContainer {
    public static TrajectoryContainer GLOBAL_SELECTED_TRAJECTORIES = null;

    public final PathPlannerTrajectory TEST_PATH;

    public TrajectoryContainer(String prefix) {
        TEST_PATH = PathPlanner.loadPath(prefix + " April Run", MAX_TRANS_SPEED, 2.0);
    }
}
