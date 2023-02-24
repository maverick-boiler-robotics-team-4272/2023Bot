package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.PathFollowState;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

public class AprilRunCommand extends SequentialCommandGroup {
    public AprilRunCommand(Drivetrain drivetrain) {
        super(
            new PathFollowState(drivetrain, getGlobalTrajectories().FORWARD_PATH),
            new PathFollowState(drivetrain, getGlobalTrajectories().BACKWARD_PATH)
        );
    }
}