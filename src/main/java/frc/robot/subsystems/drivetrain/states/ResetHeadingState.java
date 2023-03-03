package frc.robot.subsystems.drivetrain.states;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.State;

public class ResetHeadingState extends State<Drivetrain> {
    public ResetHeadingState(Drivetrain drivetrain) {
        super(drivetrain);
    }

    @Override
    public void initialize() {
        requiredSubsystem.getGyroscope().setRotation(new Rotation2d(0));
        requiredSubsystem.setRobotPose(requiredSubsystem.getRobotPose());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
