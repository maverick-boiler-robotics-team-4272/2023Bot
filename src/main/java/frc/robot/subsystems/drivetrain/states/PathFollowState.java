package frc.robot.subsystems.drivetrain.states;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import com.pathplanner.lib.server.PathPlannerServer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.drivetrain.Drivetrain;

import static frc.robot.constants.AutoConstants.PathUtils.*;
import static frc.robot.constants.TelemetryConstants.Limelights.CENTER;

public class PathFollowState extends PositionalDriveState {
    private PathPlannerTrajectory trajectory;
    private boolean resetOdometry;
    private boolean forcePath;

    private Timer timer;
    private Pose2d endPose;
    private Pose2d desiredPose;

    public PathFollowState(Drivetrain drivetrain, PathPlannerTrajectory trajectory, boolean resetOdometry, boolean forcePath) {
        super(drivetrain, X_CONTROLLER, Y_CONTROLLER, THETA_CONTROLLER);

        this.trajectory = trajectory;
        this.timer = new Timer();
        this.resetOdometry = resetOdometry;
        this.forcePath = forcePath;
    }

    public PathFollowState(Drivetrain drivetrain, PathPlannerTrajectory trajectory) {
        this(drivetrain, trajectory, true, false);
    }



    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        PathPlannerState endState = trajectory.getEndState();
        endPose = new Pose2d(endState.poseMeters.getTranslation(), endState.holonomicRotation);
        PathPlannerServer.sendActivePath(trajectory.getStates());

        if(!resetOdometry) return;
        Pose2d aprilTagPose = CENTER.getRobotPose();
        if(!CENTER.isValidTarget() || forcePath){
            requiredSubsystem.getGyroscope().setRotation(trajectory.getInitialState().holonomicRotation);
            requiredSubsystem.setRobotPose(trajectory.getInitialHolonomicPose());
        } else {
            // requiredSubsystem.getGyroscope().setRotation(aprilTagPose.getRotation());
            requiredSubsystem.setRobotPose(aprilTagPose);
        }

    }

    @Override
    public double getDesiredX() {
        return desiredPose.getX();
    }

    @Override
    public double getDesiredY() {
        return desiredPose.getY();
    }

    @Override
    public Rotation2d getDesiredTheta() {
        return desiredPose.getRotation();
    }

    @Override
    public void execute() {
        PathPlannerState state = (PathPlannerState) trajectory.sample(timer.get());
        desiredPose = new Pose2d(state.poseMeters.getTranslation(), state.holonomicRotation);

        super.execute();
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
        requiredSubsystem.drive(0, 0, 0);
    }

    @Override
    public boolean isFinished() {
        return timer.get() >= trajectory.getTotalTimeSeconds() && posesEqual(endPose, requiredSubsystem.getRobotPose(), 0.1);
    }
}