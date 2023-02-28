package frc.robot.subsystems.arm.states;

import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.team4272.globals.State;

public class ElevatorSetpointState extends State<ArmSubsystem> {
    private ArmSetpoints setpoint;

    public ElevatorSetpointState(ArmSubsystem arm, ArmSetpoints setpoint) {
        super(arm);

        this.setpoint = setpoint;
    }

    @Override
    public void initialize() {
        requiredSubsystem.setElevatorPos(setpoint.elevatorHeightMeters);
    }

    @Override
    public boolean isFinished() {
        return requiredSubsystem.isElevatorAtPosition(setpoint.elevatorHeightMeters) || setpoint.safetyOverride;
    }
}
