package frc.robot.subsystems.yagsldrive.states;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.yagsldrive.YagslDrive;

import static frc.robot.constants.AutoConstants.PathUtils.*;
import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;

public class RotationLockState extends PositionalDriveState {
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private Supplier<Rotation2d> thetaSupplier;
    private PIDController thetaController;

    public RotationLockState(YagslDrive drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Supplier<Rotation2d> thetaSupplier, PIDController thetaController) {
        super(drivetrain, X_CONTROLLER,  Y_CONTROLLER, THETA_CONTROLLER);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.thetaSupplier = thetaSupplier;
        this.thetaController = thetaController;
        this.thetaController.enableContinuousInput(-Math.PI, Math.PI);
    }

    public RotationLockState(YagslDrive drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Rotation2d desiredAngle, PIDController thetaController) {
        this(drivetrain, xSpeed, ySpeed, () -> desiredAngle, thetaController);
    }


    public RotationLockState(YagslDrive drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Supplier<Rotation2d> thetaSupplier) {
        this(drivetrain, xSpeed, ySpeed, thetaSupplier, THETA_CONTROLLER);
    }

    public RotationLockState(YagslDrive drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Rotation2d desiredAngle) {
        this(drivetrain, xSpeed, ySpeed, () -> desiredAngle);
    }

    @Override
    public double getDesiredX() {
        return 0;
    }

    @Override
    public double getDesiredY() {
        return 0;
    }

    @Override
    public Rotation2d getDesiredTheta() {
        return thetaSupplier.get();
    }

    @Override
    public double getXSpeed() {
        return ySpeed.getAsDouble() * MAX_TRANS_SPEED;
    }

    @Override
    public double getYSpeed() {
        return -xSpeed.getAsDouble() * MAX_TRANS_SPEED;
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.drive(0.0, 0.0, 0.0);
    }
}