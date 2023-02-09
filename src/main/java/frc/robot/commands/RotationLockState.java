package frc.robot.commands;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.Drivetrain;
import frc.team4272.globals.State;

import static frc.robot.constants.AutoConstants.PathUtils.THETA_CONTROLLER;

public class RotationLockState extends State<Drivetrain> {
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private PIDController thetaController;

    public RotationLockState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Rotation2d desiredAngle, PIDController thetaController) {
        super(drivetrain);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.thetaController = thetaController;
        this.thetaController.enableContinuousInput(-Math.PI, Math.PI);
        this.thetaController.setSetpoint(desiredAngle.getRadians());
    }

    public RotationLockState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Rotation2d desiredAngle) {
        this(drivetrain, xSpeed, ySpeed, desiredAngle, THETA_CONTROLLER);
    }

    @Override
    public void execute() {
        double thetaSpeed = thetaController.calculate(requiredSubsystem.getRobotPose().getRotation().getRadians());
        requiredSubsystem.drive(xSpeed.getAsDouble(), ySpeed.getAsDouble(), thetaSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.drive(0.0, 0.0, 0.0);
    }
}
