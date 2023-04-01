package frc.robot.subsystems.candle.states;

import frc.robot.subsystems.candle.Candle;
import frc.team4272.globals.State;

public class ColorSetState extends State<Candle> {
    public static class LEDState {
        public final int startIndex;
        public final int count;
        public final int r;
        public final int g;
        public final int b;

        public LEDState(int startIndex, int count, int r, int g, int b) {
            this.startIndex = startIndex;
            this.count = count;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private LEDState[] states;

    public ColorSetState(Candle candle, LEDState... states) {
        super(candle);

        this.states = states;
    }

    @Override
    public void initialize() {
        for(LEDState state : states) {
            requiredSubsystem.setLEDs(state.startIndex, state.count, state.r, state.g, state.b);
        }
    }

    @Override
    public void end(boolean interrupted) {
        for(LEDState state : states) {
            requiredSubsystem.setLEDs(state.startIndex, state.count, 0, 0, 0);
        }
    }
}
