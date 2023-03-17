package frc.robot.commands;

import java.util.Map;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.drivetrain.states.ResetPoseState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.CubeEjectState;
import frc.robot.subsystems.intake.states.CubeGrabState;

import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints.*;
import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;
import static frc.robot.constants.TelemetryConstants.Limelights.*;

public class ConeMultiCubeCommand extends SequentialCommandGroup {
    public ConeMultiCubeCommand(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new ArmSetpointState(arm, MID_CONE),
            new ConeEjectState(intake, () -> 0.9).withTimeout(0.3),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().TWO_PIECE_GRAB),
                getGlobalTrajectories().TWO_PIECE_GRAB.getMarkers(),
                Map.of(
                    "stow intake",
                    new ArmSetpointState(arm, STOWED),
                    "drop intake",
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                            new ArmSetpointState(arm, GROUND_CUBE),
                            new CubeGrabState(intake, () -> 1.0).withTimeout(2.0)
                        ),
                        new ParallelCommandGroup(
                            new ArmSetpointState(arm, STOWED),
                            new CubeGrabState(intake, () -> 0.3)
                        )
                    )
                )
            ),
            new ArmSetpointState(arm, STOWED),
            new InstantCommand(() -> {
                if(!intake.isCubeLidarTripped()) {
                    CommandScheduler.getInstance().cancel(this);
                }
            }),
            new ParallelRaceGroup(
                new PathFollowState(drivetrain, getGlobalTrajectories().CUBE_PLACE, false, false),
                new CubeGrabState(intake, () -> 0.3)
            ),
            new SequentialCommandGroup(
                new ParallelDeadlineGroup(
                    new ArmSetpointState(arm, MID_CUBE),
                    new CubeGrabState(intake, () -> 0.5),
                    new ResetPoseState(drivetrain, CENTER)
                ),
                new CubeEjectState(intake, () -> 0.4).withTimeout(0.2)
            ),
            new ArmSetpointState(arm, STOWED)
        );
    }
}