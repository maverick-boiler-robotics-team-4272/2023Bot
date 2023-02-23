package frc.robot.constants;

import edu.wpi.first.networktables.BooleanEntry;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.limelight.Limelight;
import frc.robot.utils.ShuffleboardTable;

public class TelemetryConstants {
    public static class ShuffleboardTables {
        public static final ShuffleboardTable FIELD_TABLE = ShuffleboardTable.getTable("Field View");
        public static final ShuffleboardTable AUTO_TABLE = ShuffleboardTable.getTable("Auto Data");
        public static final ShuffleboardTable TESTING_TABLE = ShuffleboardTable.getTable("Testing");
    }

    public static class Limelights {
        public static final Limelight RIGHT = Limelight.getLimelight("right");
        public static final Limelight CENTER = Limelight.getLimelight("center");
        public static final Limelight LEFT = Limelight.getLimelight("left");
    }

    public static class Field {
        public static final Field2d FIELD = new Field2d();
    }

    public static class FMS {
        private static final NetworkTable FMS_TABLE = NetworkTableInstance.getDefault().getTable("FMSInfo");

        public static final BooleanEntry RED_ALLIANCE = FMS_TABLE.getBooleanTopic("IsRedAlliance").getEntry(false);
    }
}