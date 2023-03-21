// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.SparkMaxLimitSwitch.Type;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.utils.ArmSetpoint;
import frc.robot.utils.MAVCoder;
import frc.robot.utils.MotorBuilder;
import frc.team4272.globals.MathUtils;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ElevatorConstants.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.RotaryArmConstants.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

public class ArmSubsystem extends SubsystemBase {

    private static class SetpointContainer implements ArmSetpoint {
        private double elevatorHeight = 0.0;
        private Rotation2d armAngle = new Rotation2d();

        @Override
        public double getElevatorHeight() {
            return elevatorHeight;
        }

        @Override
        public Rotation2d getArmAngle() {
            return armAngle;
        }

        @Override
        public boolean getSafetyOverride() {
            return false;
        }

        public void setElevatorHeight(double elevatorHeight) {
            this.elevatorHeight = elevatorHeight;
        }

        public void setArmAngle(Rotation2d armAngle) {
            this.armAngle = armAngle;
        }
    }

    private CANSparkMax elevatorLeftFollower; // Nothing done with the value, kept here because it follows the leader
    private CANSparkMax elevatorRightLeader;
    private CANSparkMax armMotor;

    private MAVCoder armEncoder;
    private ArmFeedforward armFeedforward = new ArmFeedforward(0, ROTARY_ARM_PID_F, 0, 0);
    private ProfiledPIDController armController = new ProfiledPIDController(
        ROTARY_ARM_PID_P,
        ROTARY_ARM_PID_I,
        ROTARY_ARM_PID_D,
        new Constraints(ROTARY_ARM_SMART_MOTION_MAX_SPEED, ROTARY_ARM_SMART_MOTION_MAX_ACCEL)
    );

    private SetpointContainer setpoint =  new SetpointContainer();

    private boolean zeroed = false;
    private SparkMaxLimitSwitch limit;

    /** Creates a new ArmSubsystem. */
    public ArmSubsystem() {
        elevatorRightLeader = MotorBuilder.createWithDefaults(ELEVATOR_RIGHT_ID)
            .withInversion(true)
            .withCurrentLimit(40)
            .withPositionFactor(SPROCKET_REV_TO_IN_RATIO * Units.inchesToMeters(1) / MOTOR_TO_SPROCKET_RATIO * CASCADE_RATIO)
            .withPosition(0.0)
            .withSoftLimits(MAX_ELEVATOR_DISTANCE, MIN_ELEVATOR_DISTANCE)
            .withPID(ELEVATOR_PID_P, ELEVATOR_PID_I, ELEVATOR_PID_D)
            .withIZone(ELEVATOR_PID_I_ZONE)
            .withDFilter(ELEVATOR_PID_D_FILTER)
            .withOutputRange(ELEVATOR_PID_OUTPUT_MIN, ELEVATOR_PID_OUTPUT_MAX)
            .build();

        elevatorLeftFollower = MotorBuilder.createWithDefaults(ELEVATOR_LEFT_ID)
            .withCurrentLimit(40)
            .asFollower(elevatorRightLeader, true)
            .build();

        armMotor = MotorBuilder.createWithDefaults(ROTARY_ARM_ID)
            .withPositionFactor(360.0 / ARM_GEAR_RATIO)
            .withOutputRange(ROTARY_ARM_PID_OUTPUT_MIN, ROTARY_ARM_PID_OUTPUT_MAX)
            .build();

        armEncoder = new MAVCoder(armMotor, ROTARY_ARM_OFFSET);

        setpoint.setElevatorHeight(ArmSetpoints.STOWED.getElevatorHeight());
        setpoint.setArmAngle(ArmSetpoints.STOWED.getArmAngle());

        armController.reset(getArmPosition());
        
        limit = elevatorRightLeader.getReverseLimitSwitch(Type.kNormallyOpen);
        limit.enableLimitSwitch(true);
    }

    private double getArmPosition() {
        return armEncoder.getPosition() / 27.0 * 26.0;
    }

    public void setElevatorPos(double meters) {
        setpoint.setElevatorHeight(meters);
    }

    public void setArm(Rotation2d angle) {
        setpoint.setArmAngle(angle);
    }

    private void setElevatorMotor(double meters) {
        elevatorRightLeader.getPIDController().setReference(meters, ControlType.kPosition, 0, ELEVATOR_PID_F);
    }

    private void setArmMotor(Rotation2d angle) {
        armController.setGoal(angle.getDegrees());
    }

    private void setArmMotor(Rotation2d angle, double speed) {
        armController.setGoal(new State(angle.getDegrees(), speed));
    }

    public boolean isElevatorAtPosition(double height) {
        return Math.abs(elevatorRightLeader.getEncoder().getPosition() - height) < 0.03;
    }

    public boolean isArmAtAngle(Rotation2d angle) {
        return Math.abs(MathUtils.inputModulo(armEncoder.getPosition() - angle.getDegrees(), -180, 180)) < 5.0;
    }

    public boolean isArmSafe() {
        return armEncoder.getPosition() < ArmSetpoints.SAFE_ARM.getArmAngle().getDegrees() + 10.0;
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
        TESTING_TABLE.putNumber("Elevator Current Inches", Units.metersToInches(elevatorRightLeader.getEncoder().getPosition()));
        TESTING_TABLE.putNumber("Arm Degrees", armEncoder.getPosition());
        TESTING_TABLE.putNumber("Arm Encoder Position", armEncoder.getUnoffsetPosition());
        TESTING_TABLE.putNumber("Arm Setpoint", armController.getSetpoint().position);

        if(!isElevatorAtPosition(setpoint.getElevatorHeight())) {
            if(!isArmSafe() || setpoint.getArmAngle().getDegrees() > ArmSetpoints.SAFE_ARM.getArmAngle().getDegrees()) {
                setArmMotor(ArmSetpoints.SAFE_ARM.getArmAngle(), getSafeArmSpeed(setpoint));
                if(isArmSafe()) {
                    setElevatorMotor(setpoint.getElevatorHeight());
                }
            } else {
                setArmMotor(setpoint.getArmAngle());
                setElevatorMotor(setpoint.getElevatorHeight());
            }
        } else {
            setArmMotor(setpoint.getArmAngle());
        }
        
        if(!zeroed) {
            if(limit.isPressed()) {
                elevatorRightLeader.getEncoder().setPosition(0.0);
                zeroed = true;
            }
        }

        if(DriverStation.isDisabled()) return;
        double armOutput = 0;
        armOutput = -armController.calculate(armEncoder.getPosition());
        armOutput += armFeedforward.calculate(getArmPosition() * Math.PI / 180.0, 0.0, 0.0);
        armMotor.set(armOutput);

        TESTING_TABLE.putNumber("Arm Encoder Position", armEncoder.getUnoffsetPosition());
    }
}
