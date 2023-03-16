package frc.robot.subsystems.candle.states;

import com.ctre.phoenix.led.RainbowAnimation;

import frc.robot.subsystems.candle.Candle;
import frc.team4272.globals.State;

public class RainbowState extends State<Candle> {
    public RainbowState(Candle candle) {
        super(candle);
    }

    @Override
    public void initialize() {
        requiredSubsystem.animate(
            new RainbowAnimation(0.5, 1.0, 8)
        );
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.animate(null);
    }
}
