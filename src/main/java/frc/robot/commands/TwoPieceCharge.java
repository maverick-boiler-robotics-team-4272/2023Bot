package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.DriveState;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.ConeGrabState;
import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import java.util.Map;

import com.pathplanner.lib.commands.FollowPathWithEvents;

public class TwoPieceCharge extends SequentialCommandGroup {
    public TwoPieceCharge(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake){
        super(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ConeEjectState(intake, () -> 0.5).withTimeout(0.5),
            new ArmSetpointState(arm, ArmSetpoints.STOWED).withTimeout(1.0),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().TWO_PIECE_CHARGE, false).withTimeout(10), 
                getGlobalTrajectories().TWO_PIECE_CHARGE.getMarkers(),
                Map.of(
                    "dropArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CONE),
                        new ConeGrabState(intake, () -> 1.0).withTimeout(3.0)
                    ), 
                    "liftArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.STOWED),
                        new ConeGrabState(intake, () -> 0.2).withTimeout(3.0)
                    )
                )
            ),
            new ParallelDeadlineGroup(
                new WaitUntilCommand(() -> drivetrain.getGyroscope().getPitch() > -4), 
                new DriveState(drivetrain, 0.0, -0.125, 0.00)
            ),
            new DriveState(drivetrain, 0.0, 0.05, 0.0).withTimeout(1.9),
            new InstantCommand(
                drivetrain::xConfig, drivetrain
            )
        );
    }
}
