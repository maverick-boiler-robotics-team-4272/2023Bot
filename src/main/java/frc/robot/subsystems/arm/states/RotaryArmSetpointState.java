package frc.robot.subsystems.arm.states;

import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
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
