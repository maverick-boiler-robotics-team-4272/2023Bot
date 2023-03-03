package frc.robot.subsystems.drivetrain.states;

import frc.robot.limelight.Limelight;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.State;

public class ResetPoseState extends State<Drivetrain> {
    private Limelight limelight;

    public ResetPoseState(Drivetrain drivetrain, Limelight limelight) {
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
