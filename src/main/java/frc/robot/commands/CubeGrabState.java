package frc.robot.commands;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.ClawSubsystem;
import frc.team4272.globals.State;

public class CubeGrabState extends State<ClawSubsystem> {
    private DoubleSupplier power;

    public CubeGrabState(ClawSubsystem claw, DoubleSupplier power) {
        super(claw);
        this.power = power;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setCubeCurrentLimits();
    }

    @Override
    public void execute() {
        requiredSubsystem.grabCube(power.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.grabCube(0);
    }
}
