package frc.robot.subsystems;

import frc.robot.utils.Pigeon;
import frc.robot.utils.SwerveModule;
import frc.team4272.swerve.utils.SwerveDriveBase;
import frc.team4272.swerve.utils.SwerveModuleBase.PositionedSwerveModule;

import static frc.robot.Constants.DrivetrainConstants.*;

public class Drivetrain extends SwerveDriveBase {

    public Drivetrain() {
        super(
            new Pigeon(25, -90), 
            new PositionedSwerveModule(new SwerveModule(1,  49.0), -WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(2,   2.0), -WHEEL_DISTANCE, -WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(3, 209.0),  WHEEL_DISTANCE,  WHEEL_DISTANCE),
            new PositionedSwerveModule(new SwerveModule(4,  59.0),  WHEEL_DISTANCE, -WHEEL_DISTANCE)
        );

    }
}
