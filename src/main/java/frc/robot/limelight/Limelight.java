package frc.robot.limelight;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

import static frc.robot.constants.UniversalConstants.*;

public final class Limelight {
    public enum LEDMode {
        kPipeline,
        kOff,
        kBlink,
        kOn
    }

    private final String tableName;
    
    private static final Map<String, Limelight> limelightMap = new HashMap<>();

    private Limelight(String tableName) {
        if(tableName == "" || tableName == null) {
            tableName = "limelight";
        } else {
            tableName = "limelight-" + tableName;
        }

        this.tableName = tableName;
    }

    public double[] getBotPose() {
        double[] pose = LimelightHelpers.getBotpose(tableName);
        if(pose.length != 7) return new double[7];
        return pose;
    }

    public double[] getBotPoseInTargetSpace() {
        double[] pose = LimelightHelpers.getBotPose_TargetSpace(tableName);
        if(pose.length != 7) return new double[7];
        return pose;
    }

    public Pose2d getRobotPose() {
        double[] pose = getBotPose();
        
        return new Pose2d(pose[0] + FIELD_HALF_WIDTH, pose[1] + FIELD_HALF_LENGTH, Rotation2d.fromDegrees(pose[5]));
    }

    public boolean isValidTarget() {
        return LimelightHelpers.getLimelightNTDouble(tableName, "tv") != 0.0;
    }

    public double getTX() {
        return LimelightHelpers.getTX(tableName);
    }

    public double getTY() {
        return LimelightHelpers.getTY(tableName);
    }

    public void setCropWindow(double cropXMin, double cropXMax, double cropYMin, double cropYMax) {
        LimelightHelpers.setCropWindow(tableName, cropXMin, cropXMax, cropYMin, cropYMax);
    }

    public void setLEDMode(LEDMode mode) {
        if(mode == null) return;
        LimelightHelpers.setLimelightNTDouble(tableName, "ledMode", mode.ordinal());
    }

    public static Limelight getLimelight(String name) {
        if(!limelightMap.containsKey(name)) {
            limelightMap.put(name, new Limelight(name));
        }

        return limelightMap.get(name);
    }
}