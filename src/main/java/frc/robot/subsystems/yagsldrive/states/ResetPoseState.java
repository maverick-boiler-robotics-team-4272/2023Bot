package frc.robot.subsystems.yagsldrive.states;

import frc.robot.limelight.Limelight;
import frc.robot.subsystems.yagsldrive.YagslDrive;
import frc.team4272.globals.State;

public class ResetPoseState extends State<YagslDrive> {
    private Limelight limelight;

    public ResetPoseState(YagslDrive drivetrain, Limelight limelight) {
        super(drivetrain);

        this.limelight = limelight;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setRobotPose(limelight.getRobotPose());
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
