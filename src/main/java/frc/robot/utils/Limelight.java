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
import edu.wpi.first.networktables.NetworkTableInstance.NetworkMode;

import static frc.robot.Constants.FieldSizeConstants.*;

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
        return pose.clone();
    }

    public Pose2d getRobotPose() {
        double[] pose = getBotPose();
        
        return new Pose2d(pose[0] + FIELD_HALF_WIDTH, pose[1] + FIELD_HALF_HEIGHT, Rotation2d.fromDegrees(pose[5]));
    }

    public boolean isValidTarget() {
        GenericEntry entry = getEntry("tv", NetworkTableType.kDouble);

        return entry.getDouble(0.0) != 0.0;
    }

    private GenericEntry getEntry(String name, NetworkTableType type) {
        while(NetworkTableInstance.getDefault().getNetworkMode().contains(NetworkMode.kStarting)) {
            System.out.println("Checked");
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {
                System.out.println("Network Tables not connected, interrupted while waiting");
            }
        }

        if(!entryMap.containsKey(name)) {
            Topic topic = table.getTopic(name);
            NetworkTableType t = NetworkTableType.getFromString(topic.getTypeString());
            if(!t.equals(type)){
                throw new IllegalArgumentException(String.format("Given type (%s) does not match topic type (%s)", type.getValueStr(), t.getValueStr()));
            }
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