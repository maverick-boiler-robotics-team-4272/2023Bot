package frc.robot.subsystems.drivetrain.states;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.State;

public class FollowCoastState extends State<Drivetrain> {
    private State<Drivetrain> followState;

    public FollowCoastState(Drivetrain drivetrain, State<Drivetrain> followState) {
        super(drivetrain);

        this.followState = followState;
    }

    @Override
    public void initialize() {
        this.followState.initialize();
        requiredSubsystem.setModulesToCoast();
    }

    @Override
    public void execute() {
        this.followState.execute();
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.setModulesToBreak();
        this.followState.end(interrupted);
    }
}
