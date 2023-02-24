// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Lidar;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

public class IntakeSubsystem extends SubsystemBase {
    private CANSparkMax intakeLeaderMotor = new CANSparkMax(INTAKE_RIGHT_ID, MotorType.kBrushless);
    private CANSparkMax intakeFollowerMotor = new CANSparkMax(INTAKE_LEFT_ID, MotorType.kBrushless);

    private Lidar coneLidar = new Lidar(11);
    private Lidar cubeLidar = new Lidar(10);

    /** Creates a new IntakeSubsystem. */
    public IntakeSubsystem() {
        intakeLeaderMotor.restoreFactoryDefaults();
        intakeFollowerMotor.restoreFactoryDefaults();

        intakeFollowerMotor.setSmartCurrentLimit(20);
        intakeLeaderMotor.setSmartCurrentLimit(20);
    }

    public void setConeCurrentLimits() {
        intakeFollowerMotor.setSmartCurrentLimit(20);
        intakeLeaderMotor.setSmartCurrentLimit(20);
    }

    public void setCubeCurrentLimits() {
        intakeFollowerMotor.setSmartCurrentLimit(5);
        intakeLeaderMotor.setSmartCurrentLimit(5);
    }

    public void grabCone(double speed) {
        intakeLeaderMotor.set(speed);
        intakeFollowerMotor.set(speed);
    }
    
    public void grabCube(double speed) {
        intakeFollowerMotor.set(speed);
        intakeLeaderMotor.set(-speed);
    }

    public void stopMotors() {
        intakeLeaderMotor.set(0);
        intakeFollowerMotor.set(0);
    }

    public boolean isCubeLidarTripped() {
        return cubeLidar.getRawDutyCycle() < 0.1; // Bogus value for now. An actual value will be figured out eventually
    }

    public boolean isConeLidarTripped() {
        return coneLidar.getRawDutyCycle() < 0.1; // Bogus value for now. An actual value will be figured out eventually
    }

    @Override
    public void periodic() {

    }
}