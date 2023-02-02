// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.constants.HardwareMap.*;
import static com.revrobotics.CANSparkMaxLowLevel.MotorType.*;
import static frc.robot.constants.RobotConstants.ElevatorConstants.*;
import static frc.robot.constants.RobotConstants.RotaryArmConstants.*;


public class ArmSubsystem extends SubsystemBase {

    private CANSparkMax elevatorLeftMotor = new CANSparkMax(ELEVATOR_LEFT_ID, kBrushless);
    private CANSparkMax elevatorRightMotor = new CANSparkMax(ELEVATOR_RIGHT_ID, kBrushless);
    private CANSparkMax armMotor = new CANSparkMax(ROTARY_ARM_ID, kBrushless);

    /** Creates a new ArmSubsystem. */
    public ArmSubsystem() {
        elevatorLeftMotor.follow(elevatorRightMotor, false); // Unsure of whether following motor will need to be inverted

        SparkMaxPIDController elevatorRightController = elevatorRightMotor.getPIDController();

        elevatorRightController.setP(ELEVATOR_PID_P);
        elevatorRightController.setI(ELEVATOR_PID_I);
        elevatorRightController.setD(ELEVATOR_PID_D);
        elevatorRightController.setFF(ELEVATOR_PID_F);

        SparkMaxPIDController armController = armMotor.getPIDController();

        armController.setP(ROTARY_ARM_PID_P);
        armController.setI(ROTARY_ARM_PID_I);
        armController.setD(ROTARY_ARM_PID_D);
        armController.setFF(ROTART_ARM_PID_F);
    }

    public void setElevatorPos(double height) {
        // TODO: finish setElevatorPosition
    }

    public void setArm(double radians) {
        // TODO: finish setArm
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
