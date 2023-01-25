package frc.robot.utils;

import com.ctre.phoenix.sensors.Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.team4272.globals.Gyroscope;
import frc.team4272.globals.MathUtils;

public class Pigeon extends Pigeon2 implements Gyroscope {
    private double offset;

    public Pigeon(int port, double offset) {
        super(port);
        setYaw(MathUtils.euclideanModulo(getYaw(), 360.0));
        this.offset = offset;
    }

    @Override
    public Rotation2d getRotation() {
        return Rotation2d.fromDegrees(-getYaw() + offset);
    }

    @Override
    public void setRotation(Rotation2d rotation) {
        offset = getYaw() - rotation.getDegrees();
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }
}
