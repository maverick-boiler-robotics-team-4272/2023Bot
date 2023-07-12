package frc.robot.subsystems.yagsldrive.states;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.yagsldrive.YagslDrive;
import frc.team4272.globals.State;

public class ResetHeadingState extends State<YagslDrive> {
    public ResetHeadingState(YagslDrive drivetrain) {
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
