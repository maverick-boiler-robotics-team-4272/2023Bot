package frc.robot.commands.yagsl;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;

public class DefaultAutoCommand extends SequentialCommandGroup {
    public DefaultAutoCommand(ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ConeEjectState(intake, () -> 0.1).withTimeout(0.5),
            new ArmSetpointState(arm, ArmSetpoints.STOWED)
        );
    }
}
