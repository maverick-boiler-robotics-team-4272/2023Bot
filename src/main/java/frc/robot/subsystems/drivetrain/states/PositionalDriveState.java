package frc.robot.subsystems.drivetrain.states;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.drivetrain.Drivetrain;

public class PositionalDriveState extends AbstractDriveState {
    private double desiredX;
    private double desiredY;
    private Rotation2d desiredTheta;
    private PIDController xController;
    private PIDController yController;
    private PIDController thetaController;

    public PositionalDriveState(Drivetrain drivetrain, PIDController xController, PIDController yController, PIDController thetaController) {
        super(drivetrain);

        this.xController = xController;
        this.yController = yController;
        this.thetaController = thetaController;
    }

    public void setDesiredX(double desiredX) {
        this.desiredX = desiredX;
    }

    public void setDesiredY(double desiredY) {
        this.desiredY = desiredY;
    }

    public void setDesiredTheta(Rotation2d desiredTheta) {
        this.desiredTheta = desiredTheta;
    }

    @Override
    public boolean isFieldRelative() {
        return true;
    }

    @Override
    public double getXSpeed() {
        return -xController.calculate(requiredSubsystem.getRobotPose().getX(), desiredX);
    }

    @Override
    public double getYSpeed() {
        return yController.calculate(requiredSubsystem.getRobotPose().getY(), desiredY);
    }

    @Override
    public double getThetaSpeed() {
        return -thetaController.calculate(requiredSubsystem.getRobotPose().getRotation().getRadians(), desiredTheta.getRadians());
    }
}
