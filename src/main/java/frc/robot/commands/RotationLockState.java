package frc.robot.commands;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.Drivetrain;
import frc.team4272.globals.State;

import static frc.robot.constants.AutoConstants.PathUtils.THETA_CONTROLLER;
import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;

public class RotationLockState extends State<Drivetrain> {
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private Supplier<Rotation2d> thetaSupplier;
    private PIDController thetaController;

    public RotationLockState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Supplier<Rotation2d> thetaSupplier, PIDController thetaController) {
        super(drivetrain);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.thetaSupplier = thetaSupplier;
        this.thetaController = thetaController;
        this.thetaController.enableContinuousInput(-Math.PI, Math.PI);
    }

    public RotationLockState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Rotation2d desiredAngle, PIDController thetaController) {
        this(drivetrain, xSpeed, ySpeed, () -> desiredAngle, thetaController);
    }


    public RotationLockState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Supplier<Rotation2d> thetaSupplier) {
        this(drivetrain, xSpeed, ySpeed, thetaSupplier, THETA_CONTROLLER);
    }

    public RotationLockState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, Rotation2d desiredAngle) {
        this(drivetrain, xSpeed, ySpeed, () -> desiredAngle);
    }

    @Override
    public void execute() {
        double thetaSpeed = -thetaController.calculate(requiredSubsystem.getRobotPose().getRotation().getRadians(), thetaSupplier.get().getRadians());
        requiredSubsystem.drive(ySpeed.getAsDouble() * MAX_TRANS_SPEED, -xSpeed.getAsDouble() * MAX_TRANS_SPEED, thetaSpeed * MAX_ROT_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.drive(0.0, 0.0, 0.0);
    }
}
