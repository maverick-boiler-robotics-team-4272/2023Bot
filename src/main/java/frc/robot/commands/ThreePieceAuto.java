package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.CubeEjectState;
import frc.robot.subsystems.intake.states.CubeGrabState;
import frc.robot.utils.ArmSetpoint;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.arm.states.ArmSetpointState;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import java.util.Map;

import com.pathplanner.lib.commands.FollowPathWithEvents;

public class ThreePieceAuto extends TwoConeCommand {
    public ThreePieceAuto(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(drivetrain, arm, intake);
        addCommands(
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().THIRD_CUBE,true,false), 
                getGlobalTrajectories().THIRD_CUBE.getMarkers(), 
                Map.of(
                    "liftArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.STOWED),
                        new CubeGrabState(intake, () -> 0.1)
                    ),
                    "dropArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CUBE),
                        new CubeGrabState(intake, () -> 0.75)
                    ),
                    "prepare",
                    new ArmSetpointState(
                        arm,
                        ArmSetpoint.createArbitrary(Units.inchesToMeters(10.0), Rotation2d.fromDegrees(-120.0))
                    ),
                    "highCube",
                    new SequentialCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.HIGH_CUBE),
                        new CubeEjectState(intake, () -> 0.75)
                    )
                )
            ),
            new FollowPathWithEvents(null, null, null)
        );
    }
}