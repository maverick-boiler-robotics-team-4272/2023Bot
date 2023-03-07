package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_SPEED;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_ACCEL;

public class TrajectoryContainer {
    public static TrajectoryContainer GLOBAL_SELECTED_TRAJECTORIES = null;

    public final PathPlannerTrajectory ONE_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PATH_RETURN;
    public final PathPlannerTrajectory CHARGE_STATION;
    public final PathPlannerTrajectory CHARGE_STATION_CROSSOVER;

    public TrajectoryContainer(String prefix) {
        ONE_CONE_PATH = PathPlanner.loadPath(prefix + " One Cone", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        TWO_CONE_PATH = PathPlanner.loadPath(prefix + " Two Cone", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        TWO_CONE_PATH_RETURN = PathPlanner.loadPath(prefix + " Two Cone Return", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        CHARGE_STATION = PathPlanner.loadPath(prefix + " Charge Station", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        CHARGE_STATION_CROSSOVER = PathPlanner.loadPath(prefix + " Charge Crossover", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
    }
}
