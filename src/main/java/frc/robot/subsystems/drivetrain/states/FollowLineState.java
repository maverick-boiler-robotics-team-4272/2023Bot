package frc.robot.subsystems.drivetrain.states;

import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.PathPlannerTrajectory.PathPlannerState;
import com.pathplanner.lib.server.PathPlannerServer;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.commands.CommonInstantCommands;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.State;

import static frc.robot.constants.AutoConstants.PathUtils.*;
import static frc.robot.constants.TelemetryConstants.Limelights.CENTER;

import java.util.ArrayList;

public class FollowLineState extends State<Drivetrain> {
    private Trajectory trajectory;
    private boolean resetOdometry;

    private Timer timer;
    private Pose2d endPose;

    public FollowLineState(Drivetrain drivetrain, Pose2d pos, boolean resetOdometry) {
        super(drivetrain);

        ArrayList<Trajectory.State> states = new ArrayList<>();
        states.add(new Trajectory.State(0, 0, 1, drivetrain.getRobotPose(), 0));
        states.add(new Trajectory.State(0, 0, 0, new Pose2d(Math.abs((pos.getX() - drivetrain.getRobotPose().getX()) / 2), 0, Rotation2d.fromDegrees((pos.getRotation().getDegrees() - drivetrain.getRobotPose().getRotation().getDegrees()) / 2)), 0);
        //TODO: Fix Midpoint formula
        this.trajectory = new Trajectory();
        this.timer = new Timer();
        this.resetOdometry = resetOdometry;
    }

    public FollowLineState(Drivetrain drivetrain, Trajectory trajectory) {
        this(drivetrain, trajectory, true);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();

        Trajectory.State endState = trajectory.getStates().get(trajectory.getStates().size() - 1);
        endPose = new Pose2d(endState.poseMeters.getTranslation(), endState.holonomicRotation);
        PathPlannerServer.sendActivePath(trajectory.getStates());

        if(!resetOdometry) return;
        Pose2d aprilTagPose = CENTER.getRobotPose();
        if(!CENTER.isValidTarget()){
            requiredSubsystem.getGyroscope().setRotation(trajectory.getInitialState().holonomicRotation);
            requiredSubsystem.setRobotPose(trajectory.getInitialHolonomicPose());
        } else {
            // requiredSubsystem.getGyroscope().setRotation(aprilTagPose.getRotation());
            requiredSubsystem.setRobotPose(aprilTagPose);
        }

    }

    @Override
    public void execute() {
        PathPlannerState desiredState = (PathPlannerState) trajectory.sample(timer.get());
        Pose2d currentPose = requiredSubsystem.getRobotPose();
        Pose2d desiredPose = new Pose2d(desiredState.poseMeters.getTranslation(), desiredState.holonomicRotation);

        double xSpeed =     -X_CONTROLLER.calculate(currentPose.getX(), desiredPose.getX());
        double ySpeed =      Y_CONTROLLER.calculate(currentPose.getY(), desiredPose.getY());
        double thetaSpeed = -THETA_CONTROLLER.calculate(currentPose.getRotation().getRadians(), desiredPose.getRotation().getRadians());

        requiredSubsystem.driveFieldOriented(xSpeed, ySpeed, thetaSpeed);

        PathPlannerServer.sendPathFollowingData(desiredPose, currentPose);
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