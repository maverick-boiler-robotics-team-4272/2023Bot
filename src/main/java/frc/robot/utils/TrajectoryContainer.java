package frc.robot.utils;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_SPEED;
import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_ACCEL;

public class TrajectoryContainer {
    public final PathPlannerTrajectory ONE_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PATH;
    public final PathPlannerTrajectory TWO_CONE_PICKUP_PATH;

    public TrajectoryContainer(String prefix) {
        ONE_CONE_PATH = PathPlanner.loadPath(prefix + " One Cone", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        TWO_CONE_PATH = PathPlanner.loadPath(prefix + " Two Cone", MAX_TRANS_SPEED, MAX_TRANS_ACCEL);
        TWO_CONE_PICKUP_PATH = PathPlanner.loadPath(prefix + " Two Cone Pickup", 0.75, 1.0);
    }
}
