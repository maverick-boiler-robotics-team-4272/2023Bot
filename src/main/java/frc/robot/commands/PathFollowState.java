package frc.robot.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.Limelight;
import frc.team4272.globals.State;

public class PathFollowState extends State<Drivetrain> {
    private PathPlannerTrajectory trajectory;
    private HolonomicDriveController controller;

    private Timer timer;
    private Pose2d endPose;

    public PathFollowState(Drivetrain drivetrain, PathPlannerTrajectory trajectory, HolonomicDriveController controller) {
        super(drivetrain);

        this.trajectory = trajectory;
        this.controller = controller;
        this.timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        PathPlannerState endState = trajectory.getEndState();
        endPose = new Pose2d(endState.poseMeters.getTranslation(), endState.holonomicRotation);

        Pose2d aprilTagPose = Limelight.getRobotPose();
        if(aprilTagPose.equals(new Pose2d())){
            requiredSubsystem.setRobotPose(trajectory.getInitialPose());
        } else {
            requiredSubsystem.setRobotPose(aprilTagPose);
        }
    }

    @Override
    public void execute() {
        PathPlannerState desiredState = (PathPlannerState) trajectory.sample(timer.get());
        Pose2d currentPose = requiredSubsystem.getRobotPose();

        ChassisSpeeds speeds = controller.calculate(currentPose, desiredState, desiredState.holonomicRotation);

        requiredSubsystem.driveFieldOriented(speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond);
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= trajectory.getTotalTimeSeconds() && requiredSubsystem.getRobotPose().equals(endPose);
    }
}
