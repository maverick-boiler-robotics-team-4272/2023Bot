// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.arm;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.MotorBuilder;
import frc.team4272.globals.MathUtils;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ElevatorConstants.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.RotaryArmConstants.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

public class ArmSubsystem extends SubsystemBase {

    private CANSparkMax elevatorLeftFollower; // Nothing done with the value, kept here because it follows the leader
    private CANSparkMax elevatorRightLeader;
    private CANSparkMax armMotor;

    /** Creates a new ArmSubsystem. */
    public ArmSubsystem() {
        elevatorRightLeader = MotorBuilder.createWithDefaults(ELEVATOR_RIGHT_ID)
            .withCurrentLimit(40)
            .withPositionFactor(SPROCKET_REV_TO_IN_RATIO * Units.inchesToMeters(1) / MOTOR_TO_SPROCKET_RATIO * CASCADE_RATIO)
            .withSoftLimits(MAX_ELEVATOR_DISTANCE, MIN_ELEVATOR_DISTANCE)
            .withPIDF(ELEVATOR_PID_P, ELEVATOR_PID_I, ELEVATOR_PID_D, ELEVATOR_PID_F)
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
            .withPosition(0.0)
            .withSoftLimits(MAX_ARM_ANGLE, MIN_ARM_ANGLE)
            .withPIDF(ROTARY_ARM_PID_P, ROTARY_ARM_PID_I, ROTARY_ARM_PID_D, ROTARY_ARM_PID_F)
            .withIZone(ROTARY_ARM_PID_I_ZONE)
            .withDFilter(ROTARY_ARM_PID_D_FILTER)
            .withOutputRange(ROTARY_ARM_PID_OUTPUT_MIN, ROTARY_ARM_PID_OUTPUT_MAX)
            .build();
    }

    public void setElevatorPos(double meters) {
        elevatorRightLeader.getPIDController().setReference(meters, ControlType.kPosition);
    }

    public void setArm(Rotation2d angle) {
        armMotor.getPIDController().setReference(angle.getDegrees(), ControlType.kPosition);
    }

    public boolean isElevatorAtPosition(double height) {
        return Math.abs(elevatorRightLeader.getEncoder().getPosition() - height) < 0.03;
    }

    public boolean isArmAtAngle(Rotation2d angle) {
        return Math
                .abs(MathUtils.inputModulo(armMotor.getEncoder().getPosition() - angle.getDegrees(), -180, 180)) < 5.0;
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
        // This method will be called once per scheduler run
    }
}
