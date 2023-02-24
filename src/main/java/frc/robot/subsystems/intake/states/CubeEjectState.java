package frc.robot.subsystems.intake.states;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.team4272.globals.State;

public class CubeEjectState extends State<IntakeSubsystem> {
    private DoubleSupplier power;

    public CubeEjectState(IntakeSubsystem intake, DoubleSupplier power) {
        super(intake);

        this.power = power;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setCubeCurrentLimits();
    }

    @Override
    public void execute() {
        requiredSubsystem.grabCube(-power.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.stopMotors();
    }
}
