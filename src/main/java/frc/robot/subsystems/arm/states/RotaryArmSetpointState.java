package frc.robot.subsystems.arm.states;

import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.utils.ArmSetpoint;
import frc.team4272.globals.State;

public class RotaryArmSetpointState extends State<ArmSubsystem> {
    private ArmSetpoint setpoint;

    public RotaryArmSetpointState(ArmSubsystem arm, ArmSetpoint setpoint) {
        super(arm);

        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setArm(setpoint.getArmAngle());
    }

    @Override
    public boolean isFinished() {
        return requiredSubsystem.isArmAtAngle(setpoint.getArmAngle()) || setpoint.getSafetyOverride();
    }
}
