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

public class DemoAutoCommand extends SequentialCommandGroup {
    public DemoAutoCommand(YagslDrive drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        addCommands(
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().DEMO, true, true),
                getGlobalTrajectories().DEMO.getMarkers(),
                Map.of(
                    // "crying",
                    // new SequentialCommandGroup(
                    //     new ArmSetpointState(arm, GROUND_CUBE)
                    // )
                    "eventgrab",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, GROUND_CUBE).repeatedly(),
                        new CubeGrabState(intake, () -> 0.8)
                    ),
                    "home",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, STOWED),
                        new CubeGrabState(intake, () -> .1)
                    )
                )
            ), 
            new ParallelCommandGroup(
                new ArmSetpointState(arm, HIGH_CUBE).withTimeout(2),
                new SequentialCommandGroup(
                    new ParallelRaceGroup(
                        new CubeGrabState(intake, () -> .1),
                    new WaitCommand(2)
                    ),
                    new CubeEjectState(intake, () -> 0.6).withTimeout(0.5)
                )
            ),
            new ArmSetpointState(arm, STOWED),
            new FollowPathWithEvents(
            new PathFollowState(drivetrain, getGlobalTrajectories().DEMO_TWO,true,false), 
            getGlobalTrajectories().DEMO_TWO.getMarkers(),
            Map.of(
                    
                )
            )
        );
    }
}
