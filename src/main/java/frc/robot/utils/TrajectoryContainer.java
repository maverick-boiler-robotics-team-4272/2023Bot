package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_AUTO_SPEED;
import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_AUTO_ACCEL;

public class TrajectoryContainer {
    public static TrajectoryContainer GLOBAL_SELECTED_TRAJECTORIES = null;

    public final PathPlannerTrajectory ONE_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PATH_RETURN;
    public final PathPlannerTrajectory CHARGE_STATION_CROSSOVER;
    public final PathPlannerTrajectory TWO_PIECE_GRAB;
    public final PathPlannerTrajectory TWO_PIECE_PLACE;
    public final PathPlannerTrajectory TWO_PIECE_CHARGE;

    public TrajectoryContainer(String prefix) {
        ONE_CONE_PATH = PathPlanner.loadPath(prefix + " One Cone", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_CONE_PATH = PathPlanner.loadPath(prefix + " Two Cone", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_CONE_PATH_RETURN = PathPlanner.loadPath(prefix + " Two Cone Return", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        CHARGE_STATION_CROSSOVER = PathPlanner.loadPath(prefix + " Charge Crossover", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_PIECE_GRAB = PathPlanner.loadPath(prefix + " Two Piece Grab", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_PIECE_PLACE = PathPlanner.loadPath(prefix + " Two Piece Place", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_PIECE_CHARGE = PathPlanner.loadPath(prefix + " Two Charge", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
    }
}
