package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.TelemetryConstants;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.DriveState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;

public class OneConeCharge extends SequentialCommandGroup{
    public OneConeCharge(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ConeEjectState(intake, () -> 1.0).withTimeout(0.5),
            new ArmSetpointState(arm, ArmSetpoints.STOWED),
            new InstantCommand(() -> {
                drivetrain.getGyroscope().setRotation(Rotation2d.fromDegrees(TelemetryConstants.FMS.RED_ALLIANCE.get() ? 0 : 180));
            }, drivetrain),
            new DriveState(drivetrain, 0.0, 0.2, 0.0).withTimeout(1.1),
            new DriveState(drivetrain, 0.0, 0.05, 0.00).until(() -> drivetrain.getGyroscope().getPitch() < -4),
            new DriveState(drivetrain, 0.0, -0.05, 0.0).withTimeout(1.7),
            new InstantCommand(
                drivetrain::xConfig, drivetrain
            )
        );
    }
}
