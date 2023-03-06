package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.TelemetryConstants;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.DriveState;
import frc.robot.subsystems.drivetrain.states.PathFollowState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeGrabState;
import frc.robot.utils.Pigeon;
import frc.robot.utils.ShuffleboardTable;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import java.util.HashSet;
import java.util.Set;

public class OneConeCharge extends SequentialCommandGroup{
    public OneConeCharge(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake) {
        super(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ParallelRaceGroup(
                new ConeGrabState(intake, () -> -0.5),
                new WaitCommand(0.5)
            ),
            new ArmSetpointState(arm, ArmSetpoints.HOME),
            new ParallelRaceGroup(
                new PathFollowState(drivetrain, getGlobalTrajectories().CHARGE_STATION),
                new WaitCommand(4.0)
            ),
            new ParallelRaceGroup(
                new DriveState(drivetrain, 0.0, 0.1, 0.00),
                new Command() {
                    private Set<Subsystem> s = new HashSet<>();

                    @Override
                    public Set<Subsystem> getRequirements() {
                        return s;
                    }

                    @Override
                    public boolean isFinished() {
                        return drivetrain.getGyroscope().getPitch() < 2;
                    }
                }
            ),
            new InstantCommand(
                drivetrain::xConfig, drivetrain
            )
        );
    }
}
