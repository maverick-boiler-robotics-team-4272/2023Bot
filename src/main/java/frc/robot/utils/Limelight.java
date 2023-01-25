package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArrayEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public final class Limelight {
    private Limelight() {};

    private static final double FIELD_HALF_WIDTH = Units.feetToMeters(54.0 / 2.0);
    private static final double FIELD_HALF_HEIGHT = Units.feetToMeters(27.0 / 2.0);

    private static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");

    private static DoubleArrayEntry botTopic = null;

    public static double[] getBotPose() {
        if(botTopic == null) botTopic = table.getDoubleArrayTopic("botpose").getEntry(new double[6]);
        double[] pose = botTopic.get();

        if(pose.length != 6) return new double[6];
        return pose;
    }

    public static Pose2d getRobotPose() {
        double[] pose = getBotPose();
        
        return new Pose2d(pose[0] + FIELD_HALF_WIDTH, pose[1] + FIELD_HALF_HEIGHT, Rotation2d.fromDegrees(pose[5]));
    }
}

