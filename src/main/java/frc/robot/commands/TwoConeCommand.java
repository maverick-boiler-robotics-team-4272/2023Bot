package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeGrabState;
import frc.robot.subsystems.intake.states.CubeGrabState;
import frc.robot.utils.ArmSetpoint;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.drivetrain.states.ResetPoseState;
import frc.robot.subsystems.arm.states.ArmSetpointState;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import java.util.Map;

import com.pathplanner.lib.commands.FollowPathWithEvents;

public class TwoConeCommand extends SequentialCommandGroup {
    public TwoConeCommand(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ParallelRaceGroup(
                    new ArmSetpointState(arm, ArmSetpoints.MID_CONE),
                    new ConeGrabState(intake, () -> 0.1)
            ),
            new ParallelRaceGroup(
                new ConeGrabState(intake, () -> -0.5),
                new WaitCommand(0.75)
            ),
            new ArmSetpointState(arm, ArmSetpoints.STOWED),
            new FollowPathWithEvents(
                new PathFollowState(drivetrain, getGlobalTrajectories().TWO_CONE_PATH,false),
                getGlobalTrajectories().TWO_CONE_PATH.getMarkers(),
                Map.of(
                    "dropArm",
                    new ParallelCommandGroup(
                        new CubeGrabState(intake, () -> 0.5),
                        new ArmSetpointState(arm, ArmSetpoints.GROUND_CUBE)
                    ),
                    "liftArm",
                    new SequentialCommandGroup(
                        new ParallelCommandGroup(
                            new ArmSetpointState(arm, ArmSetpoints.STOWED),
                            new CubeGrabState(intake, () -> 0.1)
                        ),
                        new CubeGrabState(intake, () -> 0.1).withTimeout(3)
                    )
                )
           ),
           new ArmSetpointState(arm, ArmSetpoints.MID_CUBE)
        );
    }
}