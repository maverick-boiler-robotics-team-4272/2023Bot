package frc.robot.subsystems.candle.states;

import frc.robot.subsystems.candle.Candle;
import frc.team4272.globals.State;

public class ConeSignalState extends State<Candle> {
    public ConeSignalState(Candle candle) {
        super(candle);
    }

    @Override
    public void initialize() {
        requiredSubsystem.setAllLEDs(255, 255, 0);
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.setAllLEDs(0);
    }
}
