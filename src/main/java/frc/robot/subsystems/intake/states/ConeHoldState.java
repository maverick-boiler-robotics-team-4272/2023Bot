package frc.robot.subsystems.intake.states;

import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.team4272.globals.State;

public class ConeHoldState extends State<IntakeSubsystem> {
    public ConeHoldState(IntakeSubsystem intake) {
        super(intake);
    }

    @Override
    public void initialize() {
        requiredSubsystem.setConeCurrentLimits();
        requiredSubsystem.grabCone(0.1);
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.stopMotors();
    }
}
