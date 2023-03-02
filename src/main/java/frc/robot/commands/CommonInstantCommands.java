package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class CommonInstantCommands {
    public static InstantCommand setRobotPos(Drivetrain drivetrain){
        return new InstantCommand(() -> drivetrain.setRobotPose(Limelights.CENTER.getRobotPose()));
    }
    
    public static InstantCommand resetGyro(Drivetrain drivetrain){
        return new InstantCommand(() -> drivetrain.getGyroscope().setRotation(new Rotation2d(0)));
    }
}
