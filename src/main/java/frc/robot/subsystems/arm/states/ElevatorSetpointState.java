package frc.robot.subsystems.arm.states;

import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.utils.ArmSetpoint;
import frc.team4272.globals.State;

public class ElevatorSetpointState extends State<ArmSubsystem> {
    private ArmSetpoint setpoint;

    public ElevatorSetpointState(ArmSubsystem arm, ArmSetpoint setpoint) {
        super(arm);

        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setElevatorPos(setpoint.getElevatorHeight());
    }

    @Override
    public boolean isFinished() {
        return requiredSubsystem.isElevatorAtPosition(setpoint.getElevatorHeight()) || setpoint.getSafetyOverride();
    }
}
