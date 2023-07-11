package frc.robot.subsystems.drivetrain.states;

import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.State;

public abstract class AbstractDriveState extends State<Drivetrain> {
    AbstractDriveState(Drivetrain drivetrain) {
        super(drivetrain);
    }

    public abstract double getXSpeed();
    public abstract double getYSpeed();
    public abstract double getThetaSpeed();
    public abstract boolean isFieldRelative();

    @Override
    public void execute() {
        if(isFieldRelative()) {
            requiredSubsystem.driveFieldOriented(getXSpeed(), getYSpeed(), getThetaSpeed());
        } else {
            requiredSubsystem.drive(getXSpeed(), getYSpeed(), getThetaSpeed());
        }
    }

    @Override
    public void end(boolean interrupted) {
        requiredSubsystem.stopModules();
    }
}
