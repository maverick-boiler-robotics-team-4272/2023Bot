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
    private CANSparkMax clawLeaderMotor = new CANSparkMax(CLAW_RIGHT_ID, MotorType.kBrushless);
    private CANSparkMax clawFollowerMotor = new CANSparkMax(CLAW_LEFT_ID, MotorType.kBrushless);

    private Lidar coneLidar = new Lidar(11);
    private Lidar cubeLidar = new Lidar(10);

    /** Creates a new ClawSubsystem. */
    public IntakeSubsystem() {
        clawLeaderMotor.restoreFactoryDefaults();
        clawFollowerMotor.restoreFactoryDefaults();

        clawFollowerMotor.setSmartCurrentLimit(20);
        clawLeaderMotor.setSmartCurrentLimit(20);
    }

    public void setConeCurrentLimits() {
        clawFollowerMotor.setSmartCurrentLimit(20);
        clawLeaderMotor.setSmartCurrentLimit(20);
    }

    public void setCubeCurrentLimits() {
        clawFollowerMotor.setSmartCurrentLimit(5);
        clawLeaderMotor.setSmartCurrentLimit(5);
    }

    public void grabCone(double speed) {
        clawLeaderMotor.set(speed);
        clawFollowerMotor.set(speed);
    }
    
    public void grabCube(double speed) {
        clawFollowerMotor.set(speed);
        clawLeaderMotor.set(-speed);
    }

    public void stopMotors() {
        clawLeaderMotor.set(0);
        clawFollowerMotor.set(0);
    }

    @Override
    public void periodic() {

    }
}