package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

public class OneConeCommand extends SequentialCommandGroup {
    public OneConeCommand(Drivetrain drivetrain, ArmSubsystem arm, ClawSubsystem claw) {
        super(
            new ParallelRaceGroup(
                new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
                new ConeGrabState(claw, () -> 0.1)
            ),
            new ParallelRaceGroup(
                new ConeGrabState(claw, () -> -0.5),
                new WaitCommand(0.5)
            ),
            new ParallelCommandGroup(
                new ArmSetpointState(arm, ArmSetpoints.HOME),
                new PathFollowState(drivetrain, getGlobalTrajectories().ONE_CONE_PATH)
            )
        );
    }
}
