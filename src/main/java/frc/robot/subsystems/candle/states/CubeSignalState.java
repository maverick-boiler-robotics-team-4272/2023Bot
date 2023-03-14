package frc.robot.subsystems.candle.states;

import frc.robot.subsystems.candle.Candle;
import frc.team4272.globals.State;

public class CubeSignalState extends State<Candle> {
    public CubeSignalState(Candle candle) {
        super(candle);
    }

    @Override
    public void initialize() {
        requiredSubsystem.setAllLEDs(128, 0, 128);
    }
    
    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.turnOffLEDs();
    }
}
