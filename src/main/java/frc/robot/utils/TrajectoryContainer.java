package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_SPEED;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_ACCEL;

public class TrajectoryContainer {
    public static TrajectoryContainer GLOBAL_SELECTED_TRAJECTORIES = null;

    public final PathPlannerTrajectory ONE_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PATH;
    public final PathPlannerTrajectory ONTO_CHARGESTATION_COMMUNITY;
    public final PathPlannerTrajectory ONTO_CHARGESTATION_FIELD;
    public final PathPlannerTrajectory TWO_CONE_PATH_RETURN;

    public TrajectoryContainer(String prefix) {
        ONE_CONE_PATH = PathPlanner.loadPath(prefix + " One Cone", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        TWO_CONE_PATH = PathPlanner.loadPath(prefix + " Two Cone", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        ONTO_CHARGESTATION_COMMUNITY = PathPlanner.loadPath(prefix + " Chargestation From Community", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        ONTO_CHARGESTATION_FIELD = PathPlanner.loadPath(prefix + " ChargeStation From Field", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        TWO_CONE_PATH_RETURN = PathPlanner.loadPath(prefix + " Two Cone Score", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
    }
}
