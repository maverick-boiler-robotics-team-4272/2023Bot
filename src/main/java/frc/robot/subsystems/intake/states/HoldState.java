package frc.robot.subsystems.intake.states;

import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.team4272.globals.State;

public class HoldState extends State<IntakeSubsystem> {
    public HoldState(IntakeSubsystem intake) {
        super(intake);
    }

    @Override
    public void initialize() {
        if(requiredSubsystem.isCubeLidarTripped()) {
            requiredSubsystem.setCubeCurrentLimits();
            requiredSubsystem.grabCube(0.1);
        } else {
            requiredSubsystem.setConeCurrentLimits();
            requiredSubsystem.grabCone(0.1);
        }
    }
}
