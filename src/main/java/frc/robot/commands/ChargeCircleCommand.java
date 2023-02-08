package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

public class ChargeCircleCommand extends SequentialCommandGroup {
    public ChargeCircleCommand(Drivetrain drivetrain) {
        super(
            new PathFollowState(drivetrain, getGlobalTrajectories().CHARGE_CIRCLE),
            new PathFollowState(drivetrain, getGlobalTrajectories().CHARGE_END)
        );
        
        Pose2d initialPose = getGlobalTrajectories().CHARGE_CIRCLE.getInitialHolonomicPose();
        drivetrain.getGyroscope().setRotation(initialPose.getRotation());
        drivetrain.setRobotPose(initialPose);
    }
}