package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;

public class TrajectoryContainer {
    public static TrajectoryContainer GLOBAL_SELECTED_TRAJECTORIES = null;

    public final PathPlannerTrajectory FORWARD_PATH;
    public final PathPlannerTrajectory BACKWARD_PATH;
    public final PathPlannerTrajectory CHARGE_CIRCLE;
    public final PathPlannerTrajectory CHARGE_END;

    public TrajectoryContainer(String prefix) {
        FORWARD_PATH = PathPlanner.loadPath(prefix + " April Run For", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        BACKWARD_PATH = PathPlanner.loadPath(prefix + " April Run Back", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        CHARGE_CIRCLE = PathPlanner.loadPath(prefix + " Charge Circle", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        CHARGE_END = PathPlanner.loadPath(prefix + " Charge End", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
    }
}
