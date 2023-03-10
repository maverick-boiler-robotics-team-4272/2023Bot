package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
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
import frc.robot.subsystems.intake.states.ConeGrabState;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

public class ChargeStationExit extends SequentialCommandGroup {
    public ChargeStationExit(Drivetrain drivetrain, ArmSubsystem arm, IntakeSubsystem intake){
        super(
            new ArmSetpointState(arm, ArmSetpoints.HIGH_CONE),
            new ParallelRaceGroup(
                new ConeGrabState(intake, () -> -0.5),
                new WaitCommand(0.5)
            ),
            new ArmSetpointState(arm, ArmSetpoints.HOME),
            new PathFollowState(drivetrain, getGlobalTrajectories().CHARGE_STATION_CROSSOVER),
            new ParallelDeadlineGroup(
                new WaitUntilCommand(() -> drivetrain.getGyroscope().getPitch() > -1),
                new DriveState(drivetrain, 0.0, 0.05, 0.00)
            ),
            new DriveState(drivetrain, 0.0, 0.05, 0.0).withTimeout(1.5),
            new InstantCommand(
                drivetrain::xConfig, drivetrain
            )
        );
    }
}
