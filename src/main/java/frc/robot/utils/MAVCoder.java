package frc.robot.utils;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxAnalogSensor;

public class MAVCoder {
    private SparkMaxAnalogSensor sensor;
    private double offset;

    public MAVCoder(CANSparkMax spark, double offset) {
        this.sensor = spark.getAnalog(SparkMaxAnalogSensor.Mode.kAbsolute);
        this.offset = offset;
    }

    public double getPosition() {
        return sensor.getPosition() * 360.0 / 3.3 - offset;
    }
}