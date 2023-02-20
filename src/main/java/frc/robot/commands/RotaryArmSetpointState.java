package frc.robot.commands;

import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ArmSubsystem.ArmSetpoints;
import frc.team4272.globals.State;

public class RotaryArmSetpointState extends State<ArmSubsystem> {
    private ArmSetpoints setpoint;

    public RotaryArmSetpointState(ArmSubsystem arm, ArmSetpoints setpoint) {
        super(arm);

        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setArm(setpoint.armAngle);
    }

    @Override
    public boolean isFinished() {
        return requiredSubsystem.isArmAtAngle(setpoint.armAngle) || setpoint.safetyOverride;
    }
}
