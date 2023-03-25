package frc.robot.subsystems.arm.states;

import frc.robot.subsystems.arm.ArmSubsystem;
import frc.team4272.globals.State;

public class ArmFixState extends State<ArmSubsystem> {
    public ArmFixState(ArmSubsystem arm) {
        super(arm);
    }

    @Override
    public void initialize() {
        requiredSubsystem.startArmFixing();
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.stopArmFixing();
    }
}
