package frc.robot.subsystems.drivetrain.states;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.MathUtils;

public class HumanPlayerLineupState extends DriveState {
    private PIDController rotationController;

    public HumanPlayerLineupState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, PIDController rotationController) {
        super(drivetrain, xSpeed, ySpeed, () -> {
            double centerRot = MathUtils.euclideanModulo(Limelights.CENTER.getRobotPose().getRotation().getRadians(), 2 * Math.PI);
            double leftRot = MathUtils.euclideanModulo(Limelights.LEFT.getRobotPose().getRotation().getRadians(), 2 * Math.PI);
            double rightRot = MathUtils.euclideanModulo(Limelights.RIGHT.getRobotPose().getRotation().getRadians(), 2 * Math.PI);
    
            int n = 0;
            double r = 0;
    
            if(Limelights.CENTER.isValidTarget()) {
                r += centerRot;
                n++;
            }
    
            if(Limelights.LEFT.isValidTarget()) {
                r += leftRot;
                n++;
            }
    
            if(Limelights.RIGHT.isValidTarget()) {
                r += rightRot;
                n++;
            }
            
            if(n != 0) {
                r /= n;
                return -rotationController.calculate(r);
            } else {
                return 0;
            }

        });
        this.rotationController = rotationController;
        this.rotationController.enableContinuousInput(0, 2 * Math.PI);
    }

    @Override
    public void initialize() {
        rotationController.reset();

        Rotation2d centerRot = Limelights.CENTER.getRobotPose().getRotation();
        double rads = MathUtils.euclideanModulo(centerRot.getRadians(), 2 * Math.PI);
        if(!Limelights.CENTER.isValidTarget()) {
            rads = 0;
        }

        if(Math.abs(rads - Math.PI) < Math.PI / 2) {
            rotationController.setSetpoint(Math.PI);
        } else {
            rotationController.setSetpoint(0);
        }
    }
}
