package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.constants.UniversalConstants.NOMINAL_VOLTAGE;

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

    public CANSparkMax createSparkMax(int id) {
        CANSparkMax spark = new CANSparkMax(id, MotorType.kBrushless);

        spark.enableVoltageCompensation(NOMINAL_VOLTAGE);
        spark.setIdleMode(IdleMode.kBrake);

        return spark;
    }

    public CANSparkMax createSparkMax(int id, double kP, double kI, double kD, double kF) {
        CANSparkMax spark = createSparkMax(id);

        SparkMaxPIDController controller = spark.getPIDController();

        controller.setP(kP);
        controller.setI(kI);
        controller.setD(kD);
        controller.setFF(kF);

        return spark;
    }

    public CANSparkMax createSparkMax(int id, PIDParameters parameters) {
        return createSparkMax(id, parameters.kP, parameters.kI, parameters.kD, parameters.kF);
    }
}
