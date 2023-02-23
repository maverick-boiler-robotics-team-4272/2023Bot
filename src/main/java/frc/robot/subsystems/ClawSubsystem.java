// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.MotorBuilder;

import static frc.robot.constants.HardwareMap.*;

public class ClawSubsystem extends SubsystemBase {
    private CANSparkMax clawLeaderMotor;
    private CANSparkMax clawFollowerMotor;

    /** Creates a new ClawSubsystem. */
    public ClawSubsystem() {
        clawLeaderMotor = MotorBuilder.createWithDefaults(CLAW_RIGHT_ID)
            .build();
        clawFollowerMotor = MotorBuilder.createWithDefaults(CLAW_LEFT_ID)
            .build();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
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
}