package frc.robot.commands;

import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.ArmSubsystem;
import frc.team4272.globals.State;

public class ArmSetpointState extends State<ArmSubsystem> {
    private ArmSetpoints setpoint;

    public ArmSetpointState(ArmSubsystem arm, ArmSetpoints setpoint) {
        super(arm);

        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setElevatorPos(setpoint.elevatorHeightMeters);
        requiredSubsystem.setArm(setpoint.armAngle);
    }

    @Override
    public boolean isFinished() {
        return requiredSubsystem.isElevatorAtPosition(setpoint.elevatorHeightMeters) && requiredSubsystem.isArmAtAngle(setpoint.armAngle);
    }
}