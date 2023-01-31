package frc.robot.utils;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableType;
import edu.wpi.first.networktables.Topic;

import static frc.robot.constants.UniversalConstants.*;

public final class Limelight {
    private final Map<String, GenericEntry> entryMap = new HashMap<>();
    private final NetworkTable table;
    
    private static final Map<String, Limelight> limelightMap = new HashMap<>();

    private Limelight(String tableName) {
        table = NetworkTableInstance.getDefault().getTable(tableName);
    }

    public double[] getBotPose() {
        GenericEntry entry = getEntry("botpose", NetworkTableType.kDoubleArray);

        double[] pose = entry.getDoubleArray(new double[6]);
        if(pose.length != 6) return new double[6];
        return pose;
    }

    public Pose2d getRobotPose() {
        double[] pose = getBotPose();
        
        return new Pose2d(pose[0] + FIELD_HALF_WIDTH, pose[1] + FIELD_HALF_HEIGHT, Rotation2d.fromDegrees(pose[5]));
    }

    public boolean isValidTarget() {
        GenericEntry entry = getEntry("tv", NetworkTableType.kBoolean);

        return entry.getBoolean(false);
    }

    private GenericEntry getEntry(String name, NetworkTableType type) {
        if(!entryMap.containsKey(name)) {
            Topic topic = table.getTopic(name);
            if(!topic.getType().equals(type)) throw new IllegalArgumentException("Given type does not match topic type");
            GenericEntry entry = topic.getGenericEntry();
            entryMap.put(name, entry);
            return entry;
        }

        return entryMap.get(name);
    }

    public static Limelight getLimelight(String name) {
        if(!limelightMap.containsKey(name)) {
            limelightMap.put(name, new Limelight(name));
        }

        return limelightMap.get(name);
    }
}