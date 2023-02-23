package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.ClawSubsystem;
import frc.team4272.globals.State;

public class ConeGrabState extends State<ClawSubsystem> {
    private DoubleSupplier power;
    
    public ConeGrabState(ClawSubsystem claw, DoubleSupplier power) {
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
        requiredSubsystem.grabCone(0);
    }
}
