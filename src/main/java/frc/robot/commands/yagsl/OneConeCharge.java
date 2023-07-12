package frc.robot.commands.yagsl;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.constants.TelemetryConstants.FMS;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.yagsldrive.YagslDrive;
import frc.robot.subsystems.yagsldrive.states.DriveState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;

public class OneConeCharge extends SequentialCommandGroup{
    public OneConeCharge(YagslDrive yagsldrive, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ConeEjectState(intake, () -> 1.0).withTimeout(0.5),
            new ArmSetpointState(arm, ArmSetpoints.STOWED),
            new InstantCommand(() -> {
                yagsldrive.getGyroscope().setRotation(new Rotation2d(0));
            }, yagsldrive),
            new DriveState(yagsldrive, 0.0, 0.2, 0.0).withTimeout(1.1),
            new DriveState(yagsldrive, 0.0, 0.05, 0.00).until(() -> yagsldrive.getGyroscope().getPitch() < -4),
            new DriveState(yagsldrive, 0.0, -0.05, 0.0).withTimeout(1.7),
            new InstantCommand(
                yagsldrive::xConfig, yagsldrive
            ),
            new InstantCommand(() -> {
                yagsldrive.getGyroscope().setRotation(yagsldrive.getGyroscope().getRotation().rotateBy(Rotation2d.fromDegrees(FMS.RED_ALLIANCE.get() ? 0 : 180)));
            }, yagsldrive)
        );
    }
}
