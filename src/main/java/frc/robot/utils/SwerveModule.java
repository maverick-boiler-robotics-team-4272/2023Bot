package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import frc.team4272.swerve.utils.SwerveModuleBase;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.SwerveModuleConstants.*;
import static frc.robot.constants.UniversalConstants.*;

public class SwerveModule extends SwerveModuleBase {

    private CANSparkMax driveMotor;
    private RelativeEncoder driveEncoder;
    private SparkMaxPIDController drivePidController;
    private CANSparkMax rotationMotor;
    private RelativeEncoder rotationEncoder;
    private SparkMaxPIDController rotationPidController;
    private double offset;
    private MAVCoder externalRotationEncoder;
    /**
     * 
     * @param moduleID - module id. 1 is front left, 2 is front right, 3 is back left, 4 is back right
     * @param offset - readout from encoder when the module is at 0
     */
    public SwerveModule(int moduleID, double offset){
        driveMotor = MotorBuilder.createWithDefaults(moduleID)
            .withPIDF(DRIVE_P, DRIVE_I, DRIVE_D, DRIVE_F)
            .withCurrentLimit(40)
            .withVelocityFactor(WHEEL_RADIUS * PI2 / (60.0 * DRIVE_RATIO * Units.metersToInches(1.0)))
            .withPositionFactor(PI2 * WHEEL_RADIUS / (DRIVE_RATIO * Units.metersToInches(1.0)))
            .build();
        driveEncoder = driveMotor.getEncoder();
        drivePidController = driveMotor.getPIDController();

        rotationMotor = MotorBuilder.createWithDefaults(moduleID + 10)
            .withPIDF(STEER_P, STEER_I, STEER_D, STEER_F)
            .withCurrentLimit(40)
            .withPositionFactor(360.0 / STEER_RATIO)
            .getUnburntSpark();
        rotationEncoder = rotationMotor.getEncoder();
        rotationPidController = rotationMotor.getPIDController();

        this.offset = offset;
        externalRotationEncoder = new MAVCoder(rotationMotor, offset);
        
        System.out.println(externalRotationEncoder.getUnoffsetPosition());

        rotationEncoder.setPosition(externalRotationEncoder.getPosition());

        // TODO: Remove these three lines of code
        // They shouldn't be necessary because the 
        // optimize function should handle continuity.
        // Fix issue with logic, and figure out why thing no work.
        rotationPidController.setPositionPIDWrappingEnabled(true);
        rotationPidController.setPositionPIDWrappingMinInput(-180);
        rotationPidController.setPositionPIDWrappingMaxInput(180);

        rotationMotor.burnFlash();
    }


    public double getMAVCoderReading() {
        return externalRotationEncoder.getPosition();
    }

    /**
     * 
     * @return offset of the module
     */
    public double getOffset() {
        return offset;
    }

    /**
     * 
     * @return angle that the module is at
     */
    public Rotation2d getHeading(){
        return Rotation2d.fromDegrees(rotationEncoder.getPosition());
    }

    /**
     * Sets the rotation and speed of the module. Automatically handles all
     * continuous math for direction to ensure the module never rotates more
     * than 90 degrees. Also ensures that if the module isn't driving anywhere,
     * don't rotate the module
     * @param desiredState desired state for the module
     */
    
    public void goToState(SwerveModuleState desiredState){
        // Specially made optimize function to handle continuity logic
        SwerveModuleState state = optimize(desiredState, getHeading());

        drivePidController.setReference(state.speedMetersPerSecond, ControlType.kVelocity);

        if(state.speedMetersPerSecond != 0.0){
            rotationPidController.setReference(state.angle.getDegrees(), ControlType.kPosition);
        }
    }

    /**
     * 
     * @return Current state of the module
     */
    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(driveEncoder.getPosition(), Rotation2d.fromDegrees(180).minus(getHeading()));
    }

    public void ensureCorrect() {
        rotationEncoder.setPosition(getMAVCoderReading());
    }
}