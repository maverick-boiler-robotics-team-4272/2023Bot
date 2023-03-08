package frc.robot.commands;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.drivetrain.states.ResetPoseState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.ConeGrabState;

import java.util.Map;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints.*;
import static frc.robot.constants.TelemetryConstants.Limelights.CENTER;


public class TwoPieceCommand extends SequentialCommandGroup {
    public TwoPieceCommand(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ArmSetpointState(arm, HIGH_CONE),
            new WaitCommand(2.0),
            // new ConeEjectState(intake, () -> 1.0).withTimeout(1.0)
            new ArmSetpointState(arm, HOME)
            // new FollowPathWithEvents(
            //     new PathFollowState(drivetrain, getGlobalTrajectories().TWO_PIECE_PATH),
            //     getGlobalTrajectories().TWO_PIECE_PATH.getMarkers(),
            //     Map.of(
            //         // "reset od",
            //         // new ResetPoseState(drivetrain, CENTER),
            //         "drop intake",
            //         new ParallelCommandGroup(
            //             new ArmSetpointState(arm, GROUND_CONE),
            //             new ConeGrabState(intake, () -> 0.5).withTimeout(5.0)
            //         ),
            //         "stow",
            //         new ArmSetpointState(arm, HOME)
            //     )
            // ),
            // new ArmSetpointState(arm, HIGH_CONE),
            // new ConeEjectState(intake, () -> 0.1).withTimeout(0.2),
            // new ArmSetpointState(arm, HOME)
        );
    }
}
