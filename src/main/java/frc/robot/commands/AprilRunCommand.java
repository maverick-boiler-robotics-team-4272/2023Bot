package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drivetrain;

import static frc.robot.constants.AutoConstants.Paths.getGlobalTrajectories;

import com.pathplanner.lib.server.PathPlannerServer;

public class AprilRunCommand extends SequentialCommandGroup {
    public AprilRunCommand(Drivetrain drivetrain) {
        super(
            new PathFollowState(drivetrain, getGlobalTrajectories().TEST_PATH),
            new InstantCommand(() -> {
                PathPlannerServer.sendPathFollowingData(new Pose2d(), new Pose2d());
            }),
            new WaitCommand(1.0),
            new InstantCommand(() -> {
                var state = getGlobalTrajectories().TEST_PATH.getEndState();

                PathPlannerServer.sendPathFollowingData(
                    new Pose2d(
                        state.poseMeters.getTranslation(), 
                        state.holonomicRotation),
                    drivetrain.getRobotPose()
                );
            })
        );
    }
}
