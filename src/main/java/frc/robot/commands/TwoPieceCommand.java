package frc.robot.commands;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.ConeGrabState;

import java.util.Map;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints.*;

public class TwoPieceCommand extends SequentialCommandGroup {
    public TwoPieceCommand(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ArmSetpointState(arm, HIGH_CONE),
            new ConeEjectState(intake, () -> 0.5).withTimeout(0.2),
            new ArmSetpointState(arm, HOME),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().TWO_PIECE_GRAB),
                getGlobalTrajectories().TWO_PIECE_GRAB.getMarkers(),
                Map.of(
                    "drop intake",
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                            new ArmSetpointState(arm, GROUND_CONE),
                            new ConeGrabState(intake, () -> 0.5).withTimeout(0.75)
                        ),
                        new ParallelCommandGroup(
                            new ArmSetpointState(arm, HOME),
                            new ConeGrabState(intake, () -> 0.1)
                        )
                    )
                )
            ),
            new ArmSetpointState(arm, HOME),
            new ParallelRaceGroup(
                new PathFollowState(drivetrain, getGlobalTrajectories().TWO_PIECE_PLACE),
                new ConeGrabState(intake, () -> 0.1)
            )
        );
    }
}
