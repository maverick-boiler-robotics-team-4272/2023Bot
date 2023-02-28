package frc.robot.subsystems.intake.states;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.team4272.globals.State;

public class ConeGrabState extends State<IntakeSubsystem> {
    private DoubleSupplier power;
    
    public ConeGrabState(IntakeSubsystem claw, DoubleSupplier power) {
        super(claw);
        this.power = power;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setConeCurrentLimits();
    }

    @Override
    public void execute() {
        requiredSubsystem.grabCone(power.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.stopMotors();
    }
}
