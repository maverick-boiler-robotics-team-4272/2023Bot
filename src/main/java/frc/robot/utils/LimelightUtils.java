package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.limelight.Limelight;
import frc.team4272.globals.MathUtils;

public class LimelightUtils {
    public static Pose2d averageLimelightPoses(Limelight... limelights) {
        double x = 0;
        double y = 0;
        double rot = 0;
        int numValid = 0;

        for(Limelight limelight : limelights) {
            Pose2d pose = limelight.getRobotPose();

            if(limelight.isValidTarget()) {
                x += pose.getX();
                y += pose.getY();
                rot += MathUtils.euclideanModulo(pose.getRotation().getRadians(), 2 * Math.PI);

                numValid++;
            }
        }

        if(numValid == 0) {
            return null;
        } else {
            return new Pose2d(x / numValid, y / numValid, new Rotation2d(rot / numValid));
        }
    }

    private static ExponentialAverage xAverage;
    private static ExponentialAverage yAverage;
    private static ExponentialAverage rotAverage;

    public static Pose2d exponentialAverageLimelights(Limelight... limelights) {
        Pose2d average = averageLimelightPoses(limelights);

        if(average == null) {
            return null;
        }

        double xExp = xAverage.update(average.getX());
        double yExp = yAverage.update(average.getY());
        double rExp = rotAverage.update(average.getRotation().getRadians());

        return new Pose2d(xExp, yExp, new Rotation2d(rExp));
    }
}
