package frc.robot.utils;

import edu.wpi.first.math.geometry.Rotation2d;

public interface ArmSetpoint {
    public double getElevatorHeight();
    public Rotation2d getArmAngle();
    public boolean getSafetyOverride();
}
