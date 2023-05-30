package frc.robot.subsystems.drivetrain.states;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.drivetrain.Drivetrain;

public abstract class PositionalDriveState extends AbstractDriveState {
    private PIDController xController;
    private PIDController yController;
    private PIDController thetaController;

    public PositionalDriveState(Drivetrain drivetrain, PIDController xController, PIDController yController, PIDController thetaController) {
        super(drivetrain);

        this.xController = xController;
        this.yController = yController;
        this.thetaController = thetaController;
    }

    public abstract double getDesiredX();
    public abstract double getDesiredY();
    public abstract Rotation2d getDesiredTheta();

    @Override
    public boolean isFieldRelative() {
        return true;
    }

    @Override
    public double getXSpeed() {
        return -xController.calculate(requiredSubsystem.getRobotPose().getX(), getDesiredX());
    }

    @Override
    public double getYSpeed() {
        return yController.calculate(requiredSubsystem.getRobotPose().getY(), getDesiredY());
    }

    @Override
    public double getThetaSpeed() {
        return -thetaController.calculate(requiredSubsystem.getRobotPose().getRotation().getRadians(), getDesiredTheta().getRadians());
    }
}
