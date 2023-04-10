package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_AUTO_SPEED;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_AUTO_ACCEL;

public class TrajectoryContainer {
    public static TrajectoryContainer GLOBAL_SELECTED_TRAJECTORIES = null;

    public final PathPlannerTrajectory TWO_CONE_PATH;
    public final PathPlannerTrajectory TWO_PIECE_GRAB;
    public final PathPlannerTrajectory TWO_PIECE_PLACE;
    public final PathPlannerTrajectory CUBE_PLACE;
    public final PathPlannerTrajectory CUBE_GRAB;
    public final PathPlannerTrajectory THIRD_CUBE;
    public final PathPlannerTrajectory FOURTH_CUBE;
    public final PathPlannerTrajectory FIRST_CUBE_CABLE_SIDE;
    public final PathPlannerTrajectory SECOND_CUBE_CABLE_SIDE;

    public TrajectoryContainer(String prefix) {
        TWO_CONE_PATH = PathPlanner.loadPath(prefix + " Two Cone", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_PIECE_GRAB = PathPlanner.loadPath(prefix + " Two Piece Grab", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        TWO_PIECE_PLACE = PathPlanner.loadPath(prefix + " Two Piece Place", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        CUBE_PLACE = PathPlanner.loadPath(prefix + " Cube Place", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        CUBE_GRAB = PathPlanner.loadPath(prefix + " Cube Grab", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        THIRD_CUBE = PathPlanner.loadPath(prefix + " Third Cube", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        FOURTH_CUBE = PathPlanner.loadPath(prefix + " Fourth Cube", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        FIRST_CUBE_CABLE_SIDE = PathPlanner.loadPath(prefix + " First Cube Cable side", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
        SECOND_CUBE_CABLE_SIDE = PathPlanner.loadPath(prefix + " Second Cube Cable", MAX_AUTO_SPEED, MAX_AUTO_ACCEL);
    }
}
