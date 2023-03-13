package frc.robot.subsystems.arm.states;

import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.utils.ArmSetpoint;
import frc.team4272.globals.State;

public class ArmSetpointState extends State<ArmSubsystem> {
    private ArmSetpoint setpoint;

    public ArmSetpointState(ArmSubsystem arm, ArmSetpoint setpoint) {
        super(arm);

        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setElevatorPos(setpoint.getElevatorHeight());
        requiredSubsystem.setArm(setpoint.getArmAngle());
    }

    @Override
    public boolean isFinished() {
        return requiredSubsystem.isElevatorAtPosition(setpoint.getElevatorHeight()) && requiredSubsystem.isArmAtAngle(setpoint.getArmAngle());
    }
}