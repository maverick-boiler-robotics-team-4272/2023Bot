package frc.robot.commands.yagsl;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.CubeEjectState;
import frc.robot.subsystems.intake.states.CubeGrabState;
import frc.robot.subsystems.yagsldrive.YagslDrive;
import frc.robot.subsystems.yagsldrive.states.PathFollowState;
import frc.robot.subsystems.arm.states.ArmSetpointState;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import java.util.Map;
import com.pathplanner.lib.commands.FollowPathWithEvents;

public class ThreePieceCableSide extends SequentialCommandGroup {
    public ThreePieceCableSide(YagslDrive yagsldrive, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ArmSetpointState(arm, ArmSetpoints.MID_CONE),
            new ConeEjectState(intake, () -> 0.8).withTimeout(0.2),
            // new ResetPoseState(yagsldrive, Limelights.LEFT),
            new FollowPathWithEvents(
                new PathFollowState(yagsldrive, getGlobalTrajectories().FIRST_CUBE_CABLE_SIDE, true, true), 
                getGlobalTrajectories().FIRST_CUBE_CABLE_SIDE.getMarkers(), 
                Map.of(
                    "stowArm",
                    new ArmSetpointState(arm, ArmSetpoints.STOWED),
                    "dropArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CUBE),
                        new CubeGrabState(intake, () -> 0.75)
                    ),
                    "liftArm",
                    new ParallelCommandGroup(
                        new CubeGrabState(intake, () -> 0.1),
                        new ArmSetpointState(arm, ArmSetpoints.MID_CUBE)
                    ),
                    "hold",
                    new CubeGrabState(intake, () -> 0.1)
                )
            ),
            new WaitCommand(0.1),
            new CubeEjectState(intake, () -> 0.85).withTimeout(.3),
            new FollowPathWithEvents(
                new PathFollowState(yagsldrive, getGlobalTrajectories().SECOND_CUBE_CABLE_SIDE, true, false), 
                getGlobalTrajectories().SECOND_CUBE_CABLE_SIDE.getMarkers(), 
                Map.of(
                    "stowArm",
                    new ArmSetpointState(arm, ArmSetpoints.STOWED),
                    "dropArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CUBE),
                        new CubeGrabState(intake, () -> 0.75)
                    ),
                    "liftArm",
                    new ParallelCommandGroup(
                        new CubeGrabState(intake, () -> 0.1),
                        new ArmSetpointState(arm, ArmSetpoints.MID_CUBE)
                    ),
                    "drop",
                    new ParallelCommandGroup(
                        new CubeGrabState(intake, () -> 0.1),
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CUBE)
                    )
                )
                ),
                new ParallelCommandGroup(
                    new CubeEjectState(intake, () -> .35).withTimeout(.3),
                    new ArmSetpointState(arm, ArmSetpoints.STOWED).beforeStarting(new WaitCommand(0.25))
                )
        );
    }
}