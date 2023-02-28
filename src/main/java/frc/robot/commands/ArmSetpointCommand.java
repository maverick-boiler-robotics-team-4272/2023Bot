package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ElevatorSetpointState;
import frc.robot.subsystems.arm.states.RotaryArmSetpointState;

public class ArmSetpointCommand extends SequentialCommandGroup {
    public ArmSetpointCommand(ArmSubsystem arm, ArmSetpoints setpoint) {
        if(ArmSetpoints.isSetpointSafe(setpoint)) {
            addCommands(
                setpoint.safetyOverride ? new InstantCommand() : new RotaryArmSetpointState(arm, ArmSetpoints.SAFE_ARM),
                // new ParallelCommandGroup(
                    new ElevatorSetpointState(arm, setpoint),
                    new RotaryArmSetpointState(arm, setpoint)
                // )
            );
        } else {
            addCommands(
                new RotaryArmSetpointState(arm, ArmSetpoints.SAFE_ARM),
                new ElevatorSetpointState(arm, setpoint),
                new RotaryArmSetpointState(arm, setpoint)
            );
        }
    }
}
