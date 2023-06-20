package frc.robot.subsystems.drivetrain.states;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.drivetrain.Drivetrain;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;

public class DriveState extends AbstractDriveState {

    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier thetaSpeed;

    public DriveState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier thetaSpeed) {
        super(drivetrain);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.thetaSpeed = thetaSpeed;
    }

    public DriveState(Drivetrain drivetrain, double xSpeed, double ySpeed, double thetaSpeed){
        this(drivetrain, () -> xSpeed, () -> ySpeed, () -> thetaSpeed); 
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
    public double getThetaSpeed() {
        return thetaSpeed.getAsDouble() * MAX_ROT_SPEED;
    }

    @Override
    public boolean isFieldRelative() {
        return true;
    }
}
