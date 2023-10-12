package frc.robot.utils;

import edu.wpi.first.math.geometry.Rotation2d;

public interface ArmSetpoint {
    public double getElevatorHeight();
    public Rotation2d getArmAngle();
    public boolean getSafetyOverride();

    public static ArmSetpoint createArbitrary(double elevatorHeight, Rotation2d armAngle) {
        return new ArmSetpoint() {
            @Override
            public double getElevatorHeight() {
                return elevatorHeight;
            }

            @Override
            public Rotation2d getArmAngle() {
                return armAngle;
            }

            @Override
            public boolean getSafetyOverride() {
                return false;
            }

            @Override
            public String toString() {
                return String.format("ArbitrarySetpoint(height = %.2f, angle = %.2fdeg)", elevatorHeight, armAngle);
            }
        };
    }
}
