package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeEjectState;
import frc.robot.subsystems.intake.states.ConeGrabState;
import frc.robot.subsystems.intake.states.CubeEjectState;
import frc.robot.subsystems.intake.states.CubeGrabState;
import frc.robot.utils.ArmSetpoint;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.arm.states.ArmSetpointState;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import java.util.Map;
import java.util.concurrent.Delayed;

import com.pathplanner.lib.commands.FollowPathWithEvents;

public class ThreePieceCableSide extends SequentialCommandGroup {
    public ThreePieceCableSide(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ParallelRaceGroup(
                new ArmSetpointState(arm, ArmSetpoints.MID_CONE),
                new ConeGrabState(intake, () -> 0.1)
            ),
            new ConeEjectState(intake, () -> 0.5).withTimeout(0.2),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().FIRST_CUBE_CABLE_SIDE, true, false), 
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
                    new ArmSetpointState(arm, ArmSetpoints.MID_CUBE),
                    "hold",
                    new CubeGrabState(intake, () -> 0.1)
                )
            ),
            new WaitCommand(0.3),
            new CubeEjectState(intake, () -> 0.8).withTimeout(.3),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().SECOND_CUBE_CABLE_SIDE, true, false), 
                getGlobalTrajectories().SECOND_CUBE_CABLE_SIDE.getMarkers(), 
                Map.of(
                    "stowArm",
                    new ArmSetpointState(arm, ArmSetpoints.STOWED),
                    "dropArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CUBE),
                        new CubeGrabState(intake, () -> 0.75)
                    )
                )
                )
        );
    }
}