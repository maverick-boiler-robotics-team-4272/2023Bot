package frc.robot.commands;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.Limelight;
import frc.team4272.globals.State;

import static frc.robot.constants.AutoConstants.PathUtils.*;
import static frc.robot.constants.UniversalConstants.*;

public class PathFollowState extends State<Drivetrain> {
    private PathPlannerTrajectory trajectory;

    private Timer timer;
    private Pose2d endPose;

    public PathFollowState(Drivetrain drivetrain, PathPlannerTrajectory trajectory) {
        super(drivetrain);

        this.trajectory = trajectory;
        this.timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        PathPlannerState endState = trajectory.getEndState();
        endPose = new Pose2d(endState.poseMeters.getTranslation(), endState.holonomicRotation);

        Pose2d aprilTagPose = Limelight.getLimelight("limelight-three").getRobotPose();
        if(aprilTagPose.equals(new Pose2d(FIELD_HALF_WIDTH, FIELD_HALF_HEIGHT, new Rotation2d(0)))){
            requiredSubsystem.setRobotPose(trajectory.getInitialPose());
        } else {
            requiredSubsystem.setRobotPose(aprilTagPose);
        }
    }

    @Override
    public void execute() {
        PathPlannerState desiredState = (PathPlannerState) trajectory.sample(timer.get());
        Pose2d currentPose = requiredSubsystem.getRobotPose();
        Pose2d desiredPose = desiredState.poseMeters;

        double xSpeed = X_CONTROLLER.calculate(currentPose.getX(), desiredPose.getX());
        double ySpeed = Y_CONTROLLER.calculate(currentPose.getY(), desiredPose.getY());
        double tSpeed = THETA_CONTROLLER.calculate(requiredSubsystem.getGyroscope().getRotation().getRadians(), desiredState.holonomicRotation.getRadians());

        requiredSubsystem.driveFieldOriented(xSpeed, -ySpeed, tSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= trajectory.getTotalTimeSeconds() && posesEqual(endPose, requiredSubsystem.getRobotPose(), 0.1);
    }
}
