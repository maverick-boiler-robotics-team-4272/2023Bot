// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.team4272.globals.MathUtils;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ElevatorConstants.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.RotaryArmConstants.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

public class ArmSubsystem extends SubsystemBase {

    private CANSparkMax elevatorLeftFollower = new CANSparkMax(ELEVATOR_LEFT_ID, MotorType.kBrushless);
    private CANSparkMax elevatorRightLeader = new CANSparkMax(ELEVATOR_RIGHT_ID, MotorType.kBrushless);
    private CANSparkMax armMotor = new CANSparkMax(ROTARY_ARM_ID, MotorType.kBrushless);

    private double elevatorSetpoint = 0.0;
    private Rotation2d armSetpoint = new Rotation2d();

    /** Creates a new ArmSubsystem. */
    public ArmSubsystem() {
        elevatorLeftFollower.restoreFactoryDefaults();
        elevatorRightLeader.restoreFactoryDefaults();
        armMotor.restoreFactoryDefaults();

        elevatorLeftFollower.setSmartCurrentLimit(40);
        elevatorRightLeader.setSmartCurrentLimit(40);
        armMotor.setSmartCurrentLimit(20);

        elevatorLeftFollower.follow(elevatorRightLeader, true);

        elevatorRightLeader.getEncoder().setPositionConversionFactor(
                SPROCKET_REV_TO_IN_RATIO * Units.inchesToMeters(1) / MOTOR_TO_SPROCKET_RATIO * CASCADE_RATIO);

        elevatorRightLeader.enableSoftLimit(SoftLimitDirection.kForward, true);
        elevatorRightLeader.setSoftLimit(SoftLimitDirection.kForward, (float) MAX_ELEVATOR_DISTANCE);
        elevatorRightLeader.enableSoftLimit(SoftLimitDirection.kReverse, true);
        elevatorRightLeader.setSoftLimit(SoftLimitDirection.kReverse, (float) MIN_ELEVATOR_DISTANCE);

        SparkMaxPIDController elevatorRightController = elevatorRightLeader.getPIDController();

        elevatorRightController.setP(ELEVATOR_PID_P);
        elevatorRightController.setI(ELEVATOR_PID_I);
        elevatorRightController.setD(ELEVATOR_PID_D);
        elevatorRightController.setFF(ELEVATOR_PID_F);
        elevatorRightController.setIZone(ELEVATOR_PID_I_ZONE);
        elevatorRightController.setDFilter(ELEVATOR_PID_D_FILTER);
        elevatorRightController.setOutputRange(ELEVATOR_PID_OUTPUT_MIN, ELEVATOR_PID_OUTPUT_MAX);
        // elevatorRightController.setSmartMotionMaxAccel(ELEVATOR_SMART_MOTION_MAX_ACCEL,
        // 0);
        // elevatorRightController.setSmartMotionMaxVelocity(ELEVATOR_SMART_MOTION_MAX_SPEED,
        // 0);

        armMotor.getEncoder().setPositionConversionFactor(360.0 / ARM_GEAR_RATIO);
        armMotor.getEncoder().setPosition(0.0);

        armMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
        armMotor.setSoftLimit(SoftLimitDirection.kForward, (float) MAX_ARM_ANGLE);
        armMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);
        armMotor.setSoftLimit(SoftLimitDirection.kReverse, (float) MIN_ARM_ANGLE);

        SparkMaxPIDController armController = armMotor.getPIDController();

        armController.setP(ROTARY_ARM_PID_P);
        armController.setI(ROTARY_ARM_PID_I);
        armController.setD(ROTARY_ARM_PID_D);
        armController.setFF(ROTARY_ARM_PID_F);
        armController.setIZone(ROTARY_ARM_PID_I_ZONE);
        armController.setDFilter(ROTARY_ARM_PID_D_FILTER);
        armController.setOutputRange(ROTARY_ARM_PID_OUTPUT_MIN, ROTARY_ARM_PID_OUTPUT_MAX);

        elevatorLeftFollower.burnFlash();
        elevatorRightLeader.burnFlash();
        armMotor.burnFlash();
    }

    public void setElevatorPos(double meters) {
        elevatorSetpoint = meters;
    }

    public void setArm(Rotation2d angle) {
        armSetpoint = angle;
    }

    private void setElevatorMotor(double meters) {
        elevatorRightLeader.getPIDController().setReference(meters, ControlType.kPosition);
    }

    private void setArmMotor(Rotation2d angle) {
        armMotor.getPIDController().setReference(angle.getDegrees(), ControlType.kPosition);
    }

    public boolean isElevatorAtPosition(double height) {
        return Math.abs(elevatorRightLeader.getEncoder().getPosition() - height) < 0.03;
    }

    public boolean isArmAtAngle(Rotation2d angle) {
        return Math.abs(MathUtils.inputModulo(armMotor.getEncoder().getPosition() - angle.getDegrees(), -180, 180)) < 5.0;
    }

    public boolean isArmSafe() {
        return armMotor.getEncoder().getPosition() > ArmSetpoints.SAFE_ARM.armAngle.getDegrees();
    }

    public void inverseKinematics(double x, double y) {
        // Set Lengths
        // static arm
        double l = 4;
        // rotary arm
        double a = 3;

        // offset of the arm base angle
        double offset = Math.PI / 6;

        // translate the coordinates to a verticle line
        double x_prime = x * Math.cos(offset) - y * Math.sin(offset);
        double y_prime = x * Math.sin(offset) + y * Math.cos(offset);

        // joint math
        double x_1 = 0;
        double x_2 = 0.5 * l * Math.sqrt(3);
        double y_2 = 0.5 * l;
        double x_3 = x_prime - x_2 - x_1;
        double theta = Math.acos(x_2 / a);
        double y_3 = a * Math.sin(theta);
        double y_1 = y_prime - y_3 - y_2;

        double height = y_1;

        // TODO: Call the set elevator and set rotation methods

    }

    @Override
    public void periodic() {
        TESTING_TABLE.putNumber("Elevator Current", elevatorRightLeader.getEncoder().getPosition());
        TESTING_TABLE.putNumber("Elevator Current Inches", Units.metersToInches(elevatorRightLeader.getEncoder().getPosition()));
        TESTING_TABLE.putNumber("Arm Current", armMotor.getEncoder().getPosition());

        if(!isElevatorAtPosition(elevatorSetpoint)) {
            if(!isArmSafe() || armSetpoint.getDegrees() < ArmSetpoints.SAFE_ARM.armAngle.getDegrees()) {
                setArmMotor(ArmSetpoints.SAFE_ARM.armAngle);
                if(isArmSafe()) {
                    setElevatorMotor(elevatorSetpoint);
                }
            } else {
                setArmMotor(armSetpoint);
                setElevatorMotor(elevatorSetpoint);
            }
        } else {
            setArmMotor(armSetpoint);
        }
    }
}
