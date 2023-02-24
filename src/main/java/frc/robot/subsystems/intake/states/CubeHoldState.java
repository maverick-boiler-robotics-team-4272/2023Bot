package frc.robot.subsystems.intake.states;

import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.team4272.globals.State;

public class CubeHoldState extends State<IntakeSubsystem> {
    public CubeHoldState(IntakeSubsystem intake) {
        super(intake);
    }

    @Override
    public void initialize() {
        requiredSubsystem.setConeCurrentLimits();
        requiredSubsystem.grabCube(0.1);
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.stopMotors();
    }
}
