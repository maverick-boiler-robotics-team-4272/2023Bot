package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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

import com.pathplanner.lib.commands.FollowPathWithEvents;

public class TwoConeCommand extends SequentialCommandGroup {
    public TwoConeCommand(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ParallelCommandGroup(
                    new ArmSetpointState(arm, ArmSetpoints.MID_CONE),
                    new ConeGrabState(intake, () -> 0.1).withTimeout(0.2)
            ),
            new ConeEjectState(intake, () -> 0.5).withTimeout(0.2),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().TWO_CONE_PATH,true, true),
                getGlobalTrajectories().TWO_CONE_PATH.getMarkers(),
                Map.of(
                    "dropArm",
                    new ParallelCommandGroup(
                        new CubeGrabState(intake, () -> 0.75),
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_AUTO_CUBE)
                    ),
                    "liftArm",
                    new ParallelCommandGroup(
                        new ArmSetpointState(arm, ArmSetpoints.STOWED),
                        new CubeGrabState(intake, () -> 0.1).withTimeout(3.0)
                    ),
                    "cubeSet",
                    new ArmSetpointState(arm, ArmSetpoint.createArbitrary(Units.inchesToMeters(11.4), Rotation2d.fromDegrees(-53.0))),
                    "hold",
                    new CubeGrabState(intake, () -> 0.1)
                )
           ),
           new CubeEjectState(intake, () -> 0.20).withTimeout(0.2)
        );
    }
}