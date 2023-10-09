package frc.robot.commands;

import java.util.Map;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.yagsldrive.states.PathFollowState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.CubeEjectState;
import frc.robot.subsystems.intake.states.CubeGrabState;
import frc.robot.subsystems.yagsldrive.YagslDrive;

import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints.*;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

public class ThreeThrowCommand extends SequentialCommandGroup {
    public ThreeThrowCommand(YagslDrive drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new ConePlaceHighCommand(arm, intake),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().THREE_THROW, true, true),
                getGlobalTrajectories().THREE_THROW.getMarkers(),
                Map.of(
                    "armDown",
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                        new ArmSetpointState(arm, GROUND_AUTO_CUBE),
                        new CubeGrabState(intake, () -> 0.8))// ,
                                                                            // new ArmSetpointState(arm,
                                                                            // STOWED)
                    ),
                    "event",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, STOWED),
                        new CubeGrabState(intake, () -> 0.2).withTimeout(1)),
                    "launch",
                    new SequentialCommandGroup(
                        new LaunchCubeCommand(arm, intake, true),
                        new WaitCommand(0.5)
                    )
                )
            ),
            new WaitCommand(.5),
            new ParallelCommandGroup(new ArmSetpointState(arm, STOWED))
        );
    }
}