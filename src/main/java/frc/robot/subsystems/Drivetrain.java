package frc.robot.subsystems;

import frc.robot.utils.Pigeon;
import frc.robot.utils.SwerveModule;
import frc.team4272.swerve.utils.SwerveDriveBase;
import frc.team4272.swerve.utils.SwerveModuleBase.PositionedSwerveModule;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;
import static frc.robot.constants.TelemetryConstants.Limelights.*;
import static frc.robot.constants.TelemetryConstants.Field.FIELD;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.FIELD_TABLE;

public class Drivetrain extends SwerveDriveBase {
    private final SwerveDriveOdometry odometry;

    public Drivetrain() {
        super(
            new Pigeon(25, -90),  // Make sure calibration of Pigeon happens before comps
            new PositionedSwerveModule(new SwerveModule(1,  51.3), -WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(2,   3.5), -WHEEL_DISTANCE, -WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(3, 209.3),  WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(4,  59.2),  WHEEL_DISTANCE, -WHEEL_DISTANCE)
        );

        odometry = new SwerveDriveOdometry(kinematics, gyroscope.getRotation(), getPositions(), THREE.getRobotPose());
        setMaxSpeeds(MAX_TRANS_SPEED, MAX_ROT_SPEED, MAX_MODULE_SPEED);
    }

    @Override
    public void drive(ChassisSpeeds speeds) {
        super.drive(speeds);
        updateOdometry();
    }

    public void updateOdometry() {
        odometry.update(gyroscope.getRotation().unaryMinus(), getPositions());
    }

    public Pose2d getRobotPose() {
        return odometry.getPoseMeters();
    }

    public void setRobotPose(Pose2d pose) {
        odometry.resetPosition(gyroscope.getRotation().unaryMinus(), getPositions(), pose);
    }

    @Override
    public void periodic() {
        FIELD.setRobotPose(getRobotPose());
        // Pose2d pose = THREE.getRobotPose();
        FIELD_TABLE.putBoolean("Valid", THREE.isValidTarget());
        // if(THREE.isValidTarget() && !pose.equals(new Pose2d(FIELD_HALF_WIDTH, FIELD_HALF_HEIGHT, new Rotation2d(0.0)))) {
        //     setRobotPose(pose);
        // }
    }
}
