package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxAnalogSensor;

public class MAVCoder {
    private SparkMaxAnalogSensor sensor;
    private double offset;

    public MAVCoder(CANSparkMax spark, double offset) {
        this.sensor = spark.getAnalog(SparkMaxAnalogSensor.Mode.kAbsolute);
        this.sensor.setPositionConversionFactor(360 / 3.3);
        this.offset = offset;
    }

    public double getPosition() {
        return sensor.getPosition() - offset;
    }
}
