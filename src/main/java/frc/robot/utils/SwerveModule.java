package frc.robot.utils;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import frc.team4272.globals.MathUtils;
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
    private CANCoder externalRotationEncoder;
    /**
     * 
     * @param moduleID - module id. 1 is front left, 2 is front right, 3 is back left, 4 is back right
     * @param offset - readout from encoder when the module is at 0
     */
    public SwerveModule(int moduleID, double offset){
        driveMotor = new CANSparkMax(moduleID, MotorType.kBrushless);
        driveEncoder = driveMotor.getEncoder();
        drivePidController = driveMotor.getPIDController();
        rotationMotor = new CANSparkMax(moduleID + 10, MotorType.kBrushless);
        rotationEncoder = rotationMotor.getEncoder();
        rotationPidController = rotationMotor.getPIDController();
        this.offset = offset;
        externalRotationEncoder = new CANCoder(moduleID + 20);

        drivePidController.setP(DRIVE_P);
        drivePidController.setI(DRIVE_I);
        drivePidController.setD(DRIVE_D);
        drivePidController.setFF(DRIVE_F);

        rotationPidController.setP(STEER_P);
        rotationPidController.setI(STEER_I);
        rotationPidController.setD(STEER_D);
        rotationPidController.setFF(STEER_F);

        driveMotor.setIdleMode(IdleMode.kBrake);
        rotationMotor.setIdleMode(IdleMode.kBrake);

        // m_driveMotor.burnFlash();
        // m_rotationMotor.burnFlash();

        init();
    }
    
    public double getEncoderPosition(){
        return MathUtils.euclideanModulo(-externalRotationEncoder.getAbsolutePosition() + offset, 360.0);
    }

    /**
     * Initialization method for the swerve module
     */
    private void init(){
        
        //360.0 is the amount of degrees in a circle. This is useful, because
        //all our rotation math is done in degrees
        rotationEncoder.setPositionConversionFactor(-360.0 / STEER_RATIO);
        
        //PI2 is 2 * pi, 60.0 is the amount of seconds in a minute
        driveEncoder.setVelocityConversionFactor(WHEEL_RADIUS * PI2 / (60.0 * DRIVE_RATIO * Units.metersToInches(1.0)));
        driveEncoder.setPositionConversionFactor(PI2 * WHEEL_RADIUS / (DRIVE_RATIO * Units.metersToInches(1.0)));

        System.out.println(externalRotationEncoder.getAbsolutePosition());

        rotationEncoder.setPosition(getEncoderPosition());

        driveMotor.enableVoltageCompensation(NOMINAL_VOLTAGE);
        rotationMotor.enableVoltageCompensation(NOMINAL_VOLTAGE);
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
}