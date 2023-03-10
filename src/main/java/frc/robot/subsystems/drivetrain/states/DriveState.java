package frc.robot.subsystems.drivetrain.states;

import java.util.function.DoubleSupplier;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.State;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.*;

public class DriveState extends State<Drivetrain> {

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
    public void execute() {
        requiredSubsystem.driveFieldOriented(ySpeed.getAsDouble() * MAX_TRANS_SPEED, -xSpeed.getAsDouble() * MAX_TRANS_SPEED, thetaSpeed.getAsDouble() * MAX_ROT_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.drive(0, 0, 0);
    }
}
