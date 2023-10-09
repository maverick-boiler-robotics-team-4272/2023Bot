package frc.robot.subsystems.yagsldrive.states;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.yagsldrive.YagslDrive;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;

public class DriveState extends AbstractDriveState {

    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;
    private DoubleSupplier thetaSpeed;

    public DriveState(YagslDrive drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, DoubleSupplier thetaSpeed) {
        super(drivetrain);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.thetaSpeed = thetaSpeed;
    }

    public DriveState(YagslDrive drivetrain, double xSpeed, double ySpeed, double thetaSpeed){
        this(drivetrain, () -> xSpeed, () -> ySpeed, () -> thetaSpeed); 
    }

    @Override
    public double getXSpeed() {
        // TESTING_TABLE.putNumber("Mag", Math.hypot(ySpeed.getAsDouble(), xSpeed.getAsDouble()));
        TESTING_TABLE.putNumber("Mag", ySpeed.getAsDouble() * MAX_TRANS_SPEED);

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
