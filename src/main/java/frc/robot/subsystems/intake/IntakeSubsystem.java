// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.Lidar;
import frc.robot.utils.MotorBuilder;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.RobotConstants.IntakeConstants.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

public class IntakeSubsystem extends SubsystemBase {
    private CANSparkMax clawLeaderMotor;
    private CANSparkMax clawFollowerMotor;

    private Lidar coneLidar = new Lidar(11);
    private Lidar cubeLidar = new Lidar(10);

    /** Creates a new ClawSubsystem. */
    public IntakeSubsystem() {
        clawLeaderMotor = MotorBuilder.createWithDefaults(INTAKE_RIGHT_ID)
            .build();
        clawFollowerMotor = MotorBuilder.createWithDefaults(INTAKE_LEFT_ID)
            .build();
    }

    public void setConeCurrentLimits() {
        clawLeaderMotor.setSmartCurrentLimit(CONE_FRONT_LIMIT);
        clawFollowerMotor.setSmartCurrentLimit(CONE_BACK_LIMIT);
    }

    public void setCubeCurrentLimits() {
        clawLeaderMotor.setSmartCurrentLimit(CUBE_FRONT_LIMIT);
        clawFollowerMotor.setSmartCurrentLimit(CUBE_BACK_LIMIT);
    }

    public void grabCone(double speed) {
        clawLeaderMotor.set(speed);
        clawFollowerMotor.set(speed);
    }
    
    public void grabCube(double speed) {
        clawFollowerMotor.set(speed * CUBE_BACK_SPEED_MULT);
        clawLeaderMotor.set(-speed);
    }

    public void stopMotors() {
        clawLeaderMotor.set(0);
        clawFollowerMotor.set(0);
    }

    public boolean isCubeLidarTripped() {
        return cubeLidar.getRawDutyCycle() < 0.05; // Bogus value for now. An actual value will be figured out eventually
    }

    public boolean isConeLidarTripped() {
        return coneLidar.getRawDutyCycle() < 0.05; // Bogus value for now. An actual value will be figured out eventually
    }

    @Override
    public void periodic() {
        TESTING_TABLE.putNumber("Cone Lidar", coneLidar.getRawDutyCycle());
        TESTING_TABLE.putNumber("Cube Lidar", cubeLidar.getRawDutyCycle());
        TESTING_TABLE.putBoolean("Has Cube", isCubeLidarTripped());
        TESTING_TABLE.putBoolean("Has Cone", isConeLidarTripped());
    }
}