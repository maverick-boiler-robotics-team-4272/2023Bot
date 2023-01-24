package frc.robot.subsystems;

import frc.robot.utils.Limelight;
import frc.robot.utils.Pigeon;
import frc.robot.utils.SwerveModule;
import frc.team4272.swerve.utils.SwerveDriveBase;
import frc.team4272.swerve.utils.SwerveModuleBase.PositionedSwerveModule;

import static frc.robot.Constants.DrivetrainConstants.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;

public class Drivetrain extends SwerveDriveBase {
    private final SwerveDriveOdometry odometry;

    public Drivetrain() {
        super(
            new Pigeon(25, -90), 
            new PositionedSwerveModule(new SwerveModule(1,  49.0), -WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(2,   2.0), -WHEEL_DISTANCE, -WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(3, 209.0),  WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(4,  59.0),  WHEEL_DISTANCE, -WHEEL_DISTANCE)
        );

        odometry = new SwerveDriveOdometry(kinematics, gyroscope.getRotation(), getPositions(), Limelight.getRobotPose());
    }

    public void updateOdometry() {
        odometry.update(gyroscope.getRotation().unaryMinus(), getPositions());
    }

    public Pose2d getRobotPose() {
        Pose2d odometryPose = odometry.getPoseMeters();
        return new Pose2d(odometryPose.getX(), odometryPose.getY(), new Rotation2d(odometryPose.getRotation().getRadians()));
    }

    public void setRobotPose(Pose2d pose) {
        odometry.resetPosition(gyroscope.getRotation().unaryMinus(), getPositions(), pose);
    }

    @Override
    public void periodic() {
        updateOdometry();
    }
}
