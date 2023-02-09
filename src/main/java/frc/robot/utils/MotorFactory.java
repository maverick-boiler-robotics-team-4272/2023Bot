package frc.robot.utils;

public class MotorFactory {
    public static class PIDParameters {
        public final double kP;
        public final double kI;
        public final double kD;
        public final double kF;

        public PIDParameters(double kP, double kI, double kD, double kF) {
            this.kP = kP;
            this.kI = kI;
            this.kD = kD;
            this.kF = kF;
        }

        public PIDParameters(double kP, double kI, double kD) {
            this(kP, kI, kD, 0.0);
        }

        public PIDParameters() {
            this(1.0, 0.0, 0.0, 0.0);
        }
    }

    private MotorFactory() {
        throw new IllegalAccessError("Motor Factory is a utility class");
    }
}
