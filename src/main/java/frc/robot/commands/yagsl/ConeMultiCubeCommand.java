package frc.robot.commands.yagsl;

import java.util.Map;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.yagsldrive.YagslDrive;
import frc.robot.subsystems.yagsldrive.states.PathFollowState;
import frc.robot.subsystems.yagsldrive.states.ResetPoseState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.CubeEjectState;
import frc.robot.subsystems.intake.states.CubeGrabState;

import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints.*;
import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;
import static frc.robot.constants.TelemetryConstants.Limelights.*;

public class ConeMultiCubeCommand extends SequentialCommandGroup {
    public ConeMultiCubeCommand(YagslDrive yagsldrive, ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new ArmSetpointState(arm, MID_CONE),
            new ConeEjectState(intake, () -> 0.9).withTimeout(0.3),
            new FollowPathWithEvents(
                new PathFollowState(yagsldrive, getGlobalTrajectories().TWO_PIECE_GRAB, true, true),
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
            new ParallelDeadlineGroup(
                new PathFollowState(yagsldrive, getGlobalTrajectories().CUBE_PLACE, false, false),
                new ArmSetpointState(arm, MID_CUBE),
                new CubeGrabState(intake, () -> 0.3)
            ),
            new ResetPoseState(yagsldrive, CENTER),
            new CubeEjectState(intake, () -> 0.5).withTimeout(0.4),
            new ArmSetpointState(arm, STOWED),
            new FollowPathWithEvents(
                new PathFollowState(yagsldrive, getGlobalTrajectories().CUBE_GRAB, true, false),
                getGlobalTrajectories().CUBE_GRAB.getMarkers(),
                Map.of(
                    "drop intake",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, GROUND_AUTO_CUBE),
                        new CubeGrabState(intake, () -> 0.75)
                    )
                )
            )
        );
    }
}