package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.ConeGrabState;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class ConePlaceHighCommand extends SequentialCommandGroup {
    public ConePlaceHighCommand(ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ConeEjectState(intake, () -> 1.0).withTimeout(0.1),
            new ArmSetpointState(arm, ArmSetpoints.STOWED)
        );
    }
}
