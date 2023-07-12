package frc.robot.commands.yagsl;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.constants.TelemetryConstants;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.yagsldrive.YagslDrive;
import frc.robot.subsystems.yagsldrive.states.DriveState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;

public class OneConeBackCommand extends SequentialCommandGroup {
    public OneConeBackCommand(YagslDrive yagsldrive, ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ConeEjectState(intake, () -> 0.1).withTimeout(0.5),
            new ArmSetpointState(arm, ArmSetpoints.STOWED),
            new InstantCommand(() -> {
                yagsldrive.getGyroscope().setRotation(Rotation2d.fromDegrees(TelemetryConstants.FMS.RED_ALLIANCE.get() ? 0 : 180));
            }, yagsldrive),
            new DriveState(yagsldrive, () -> 0.0, () -> 0.3, () -> 0.0).withTimeout(1.5)
        );
    }
}
