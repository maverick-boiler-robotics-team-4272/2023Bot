package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import static frc.robot.constants.RobotConstants.NOMINAL_VOLTAGE;
import static frc.robot.constants.RobotConstants.DEFAULT_CURRENT_LIMIT;;

public class MotorBuilder {
    private CANSparkMax spark;
    private RelativeEncoder encoder;
    private SparkMaxPIDController controller;

    private MotorBuilder(int id) {
        spark = new CANSparkMax(id, MotorType.kBrushless);

        spark.restoreFactoryDefaults();
        spark.clearFaults();
    }

    public MotorBuilder asFollower(CANSparkMax leader, boolean invert) {
        spark.follow(leader, invert);

        return this;
    }

    public MotorBuilder withPID(double p, double i, double d) {
        if(controller == null) controller = spark.getPIDController(); 

        controller.setP(p);
        controller.setI(i);
        controller.setD(d);

        return this;
    }

    public MotorBuilder withPIDF(double p, double i, double d, double f) {
        if(controller == null) controller = spark.getPIDController();

        controller.setP(p);
        controller.setI(i);
        controller.setD(d);
        controller.setFF(f);

        return this;
    }

    public MotorBuilder withIZone(double iZone) {
        if(controller == null) controller = spark.getPIDController();

        controller.setIZone(iZone);

        return this;
    }

    public MotorBuilder withDFilter(double dFilter) {
        if(controller == null) controller = spark.getPIDController();

        controller.setDFilter(dFilter);

        return this;
    }

    public MotorBuilder withOutputRange(double min, double max) {
        if(controller == null) controller = spark.getPIDController();

        controller.setOutputRange(min, max);

        return this;
    }

    public MotorBuilder withCurrentLimit(int limit) {
        spark.setSmartCurrentLimit(limit);

        return this;
    }

    public MotorBuilder withVoltageCompensation(int nominalVoltage) {
        spark.enableVoltageCompensation(nominalVoltage);

        return this;
    }

    public MotorBuilder withIdleMode(IdleMode mode) {
        spark.setIdleMode(mode);

        return this;
    }

    public MotorBuilder withInversion(boolean inverted) {
        spark.setInverted(false);

        return this;
    }

    public MotorBuilder withSoftLimits(double forwardLimit, double reverseLimit) {
        spark.enableSoftLimit(SoftLimitDirection.kForward, true);
        spark.setSoftLimit(SoftLimitDirection.kForward, (float) forwardLimit);
        
        spark.enableSoftLimit(SoftLimitDirection.kReverse, true);
        spark.setSoftLimit(SoftLimitDirection.kReverse, (float) reverseLimit);

        return this;
    }

    public MotorBuilder withPositionFactor(double factor) {
        if(encoder == null) encoder = spark.getEncoder();

        encoder.setPositionConversionFactor(factor);

        return this;
    }

    public MotorBuilder withVelocityFactor(double factor) {
        if(encoder == null) encoder = spark.getEncoder();

        encoder.setVelocityConversionFactor(factor);

        return this;
    }

    public MotorBuilder withPosition(double position) {
        if(encoder == null) encoder = spark.getEncoder();

        encoder.setPosition(position);

        return this;
    }

    public CANSparkMax build() {
        spark.burnFlash();

        return spark;
    }

    public static MotorBuilder createEmpty(int id) {
        return new MotorBuilder(id);
    }

    public static MotorBuilder createWithDefaults(int id) {
        return new MotorBuilder(id)
            .withCurrentLimit(DEFAULT_CURRENT_LIMIT)
            .withVoltageCompensation(NOMINAL_VOLTAGE)
            .withIdleMode(IdleMode.kBrake)
            .withInversion(false);
    }
}
