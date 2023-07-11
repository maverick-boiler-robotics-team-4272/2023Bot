package frc.robot.subsystems.drivetrain;

import frc.robot.constants.TelemetryConstants;
import frc.robot.utils.Pigeon;
import frc.robot.utils.SwerveModule;
import frc.team4272.swerve.utils.SwerveDriveBase;
import frc.team4272.swerve.utils.SwerveModuleBase.PositionedSwerveModule;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;

import com.pathplanner.lib.server.PathPlannerServer;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;
import static frc.robot.constants.TelemetryConstants.Limelights.*;

import java.util.List;

import static frc.robot.constants.HardwareMap.*;

public class Drivetrain extends SwerveDriveBase<Pigeon, SwerveModule> {
    private final SwerveDriveOdometry odometry;

    public Drivetrain() {
        super(
            new Pigeon(PIGEON_ID, -90), // Make sure calibration of Pigeon happens before comps
            SwerveModule.class,
            List.of(
                new PositionedSwerveModule<SwerveModule>(new SwerveModule(MODULE_FL_ID,  FRONT_LEFT_OFFSET), -WHEEL_DISTANCE,  WHEEL_DISTANCE),
                new PositionedSwerveModule<SwerveModule>(new SwerveModule(MODULE_FR_ID, FRONT_RIGHT_OFFSET), -WHEEL_DISTANCE, -WHEEL_DISTANCE),
                new PositionedSwerveModule<SwerveModule>(new SwerveModule(MODULE_BL_ID,   BACK_LEFT_OFFSET),  WHEEL_DISTANCE,  WHEEL_DISTANCE),
                new PositionedSwerveModule<SwerveModule>(new SwerveModule(MODULE_BR_ID,  BACK_RIGHT_OFFSET),  WHEEL_DISTANCE, -WHEEL_DISTANCE)
            )
        );

        odometry = new SwerveDriveOdometry(kinematics, gyroscope.getRotation(), getPositions(), CENTER.getRobotPose());
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
        Pose2d robotPose = CENTER.getRobotPose();

        if(!DriverStation.isAutonomous()) {
            PathPlannerServer.sendPathFollowingData(robotPose, getRobotPose());
        }
        TelemetryConstants.ShuffleboardTables.TESTING_TABLE.putNumber("Pitch", gyroscope.getPitch());
    }

    public void resetModules() {
        for(int i = 0; i < modules.length; i++) {
            modules[i].ensureCorrect();
        }
    }

    public void stopModules() {
        for(SwerveModule module : modules) {
            module.stopModule();
        }
    }

    public void xConfig() {
        setStates(
            new SwerveModuleState(0.1, Rotation2d.fromDegrees(135)),
            new SwerveModuleState(0.1, Rotation2d.fromDegrees(-135)),
            new SwerveModuleState(0.1, Rotation2d.fromDegrees(45)),
            new SwerveModuleState(0.1, Rotation2d.fromDegrees(-45))
        );
    }
}
