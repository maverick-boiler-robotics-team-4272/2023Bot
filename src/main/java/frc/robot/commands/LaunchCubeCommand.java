// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.CubeEjectState;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class LaunchCubeCommand extends SequentialCommandGroup {
    /** Creates a new LaunchCube. */
    public LaunchCubeCommand(ArmSubsystem arm, IntakeSubsystem intake, boolean auto) {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());
        addCommands(
            new ParallelCommandGroup(
                new ArmSetpointState(arm, ArmSetpoints.LAUNCH_CUBE),
                new SequentialCommandGroup(
                    new WaitCommand(0.9), //was 1.0
                    new CubeEjectState(intake, () -> 1.0).withTimeout(auto ? 2.5 : 0.5)
                )
            )
        );
    }
}
