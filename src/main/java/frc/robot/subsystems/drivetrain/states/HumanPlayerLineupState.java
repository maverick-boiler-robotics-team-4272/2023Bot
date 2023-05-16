package frc.robot.subsystems.drivetrain.states;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.MathUtils;

public class HumanPlayerLineupState extends DriveState {
    private PIDController rotationController;

    public HumanPlayerLineupState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, PIDController rotationController) {
        super(drivetrain, xSpeed, ySpeed, () -> {
            Rotation2d ang = Limelights.CENTER.getRobotPose().getRotation();
            return rotationController.calculate(ang.getRadians());
        });
        this.rotationController = rotationController;
    }

    @Override
    public void initialize() {
        Pose2d currentRobotPose = Limelights.CENTER.getRobotPose();
        double rads = MathUtils.euclideanModulo(currentRobotPose.getRotation().getRadians(), 2 * Math.PI);

        if(Math.abs(rads - Math.PI) < Math.PI / 2) {
            rotationController.setSetpoint(Math.PI);
        } else {
            rotationController.setSetpoint(0);
        }
    }
}
