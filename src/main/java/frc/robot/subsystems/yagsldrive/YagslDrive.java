package frc.robot.subsystems.yagsldrive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.TelemetryConstants;
import frc.robot.utils.Pigeon;
import frc.robot.utils.SwerveModule;
import frc.robot.utils.YAGSL.SwerveDriveOdometry2;
import frc.robot.utils.YAGSL.SwerveKinematics2;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;
import static frc.robot.constants.TelemetryConstants.Limelights.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

import com.pathplanner.lib.server.PathPlannerServer;

public class YagslDrive extends SubsystemBase {
    private final Pigeon gyroscope;
    private final SwerveKinematics2 kinematics;
    private final SwerveDriveOdometry2 odometry;
    private final SwerveModule[] modules;
    private final int numModules = 4;

    private double maxTranslational;
    private double maxRotational;
    private double maxTotal;

    public YagslDrive() {
        gyroscope = new Pigeon(PIGEON_ID, 90);
        kinematics = new SwerveKinematics2(
            new Translation2d(-WHEEL_DISTANCE, -WHEEL_DISTANCE),
            new Translation2d(-WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new Translation2d (WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new Translation2d( WHEEL_DISTANCE, -WHEEL_DISTANCE)
        );

        modules = new SwerveModule[] {
            new SwerveModule(MODULE_FR_ID,FRONT_RIGHT_OFFSET),
            new SwerveModule(MODULE_FL_ID, FRONT_LEFT_OFFSET),
            new SwerveModule(MODULE_BL_ID, BACK_LEFT_OFFSET),
            new SwerveModule(MODULE_BR_ID, BACK_RIGHT_OFFSET)
        };

        odometry = new SwerveDriveOdometry2(kinematics, gyroscope.getRotation(), getPositions());
        setMaxSpeeds(MAX_TRANS_SPEED, MAX_ROT_SPEED, MAX_MODULE_SPEED);
    }

    
    public YagslDrive setMaxSpeeds(double translationalSpeed, double rotationalSpeed, double moduleSpeed) {
        maxTranslational = translationalSpeed;
        maxRotational = rotationalSpeed;
        maxTotal = moduleSpeed;
        return this;
    }

    public YagslDrive setMaxSpeeds(double moduleSpeed) {
        maxTranslational = 0.0;
        maxRotational = 0.0;
        maxTotal = moduleSpeed;
        return this;
    }

    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);
        if(maxTotal == 0) {
            throw new IllegalCallerException("Max total must have a value. Call setMaxSpeeds at some point during initialization");
        }

        TESTING_TABLE.putNumber("Magnitude", Math.hypot(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond));

        if(maxTranslational == 0 && maxRotational == 0) {
            SwerveKinematics2.desaturateWheelSpeeds(states, maxTotal);
        } else {
            SwerveKinematics2.desaturateWheelSpeeds(states, speeds, maxTotal, maxTranslational, maxRotational);
        }

        setStates(states);

        updateOdometry();
    }

    /**
     * Drive in robot oriented
     * @param xSpeed
     * @param ySpeed
     * @param thetaSpeed
     */
    public void drive(double xSpeed, double ySpeed, double thetaSpeed) {
        ChassisSpeeds speeds = new ChassisSpeeds(xSpeed, ySpeed, thetaSpeed);
        drive(speeds);
    }

    /**
     * Drive in field oriented
     * @param xSpeed
     * @param ySpeed
     * @param thetaSpeed
     */
    public void driveFieldOriented(double xSpeed, double ySpeed, double thetaSpeed) {
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, thetaSpeed, gyroscope.getRotation());
        drive(speeds);
    }

    public Pigeon getGyroscope() {
        return gyroscope;
    }

    /**
     * Set the states of all the modules
     * @implNote This should be overwritten with a more optimized version when subclassed, but technically, its not necessary
     * @param states
     */
    public void setStates(SwerveModuleState... states) {
        if(states.length != numModules) throw new IllegalArgumentException("Number of states provided doesnt match number of modules");

        for(int i = 0; i < states.length; i++) {
            modules[i].goToState(states[i]);
        }
    }

    /**
     * Get the positions of all the modules. This is never used internally, but if desired, it can be used for odometry
     * @implNote If this is going to be used, it should be replaced with a more optimized version when subclassed
     * @return
     */
    public SwerveModulePosition[] getPositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[numModules];

        for(int i = 0; i < numModules; i++) {
            positions[i] = modules[i].getPosition();
        }

        return positions;
    }

    private Pose2d odometryPose = null;
    public void updateOdometry() {
        odometryPose = odometry.update(gyroscope.getRotation().unaryMinus(), getPositions());
    }

    public Pose2d getRobotPose() {
        return odometryPose == null ? new Pose2d() : odometryPose;
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
        
        TESTING_TABLE.putNumber("Pitch", gyroscope.getPitch());
        TESTING_TABLE.putNumber("Module 0 Angle", modules[0].getHeading().getDegrees());
        TESTING_TABLE.putNumber("Module 2 Angle", modules[2].getHeading().getDegrees());
    }

    public void resetModules() {
        for(int i = 0; i < modules.length; i++) {
            modules[i].ensureCorrect();
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
