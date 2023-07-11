package frc.robot.subsystems.drivetrain.states;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.team4272.globals.MathUtils;

import static frc.robot.constants.RobotConstants.DrivetrainConstants.MAX_TRANS_SPEED;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.TESTING_TABLE;

public class HumanPlayerLineupState extends AbstractDriveState {
    private PIDController rotationController;
    private Rotation2d desiredRotation;
    private DoubleSupplier xSpeed;
    private DoubleSupplier ySpeed;

    public static Rotation2d averageLimelights() {
        Rotation2d centerRot = Limelights.CENTER.getRobotPose().getRotation();
        Rotation2d leftRot = Limelights.LEFT.getRobotPose().getRotation();
        Rotation2d rightRot = Limelights.RIGHT.getRobotPose().getRotation();
        double rads = 0;
        int n = 0;
        if(Limelights.CENTER.isValidTarget()) {
            rads += MathUtils.inputModulo(centerRot.getRadians(), 0, Math.PI * 2);
            n++;
        }

        if(Limelights.LEFT.isValidTarget()) {
            rads += MathUtils.inputModulo(leftRot.getRadians(), 0, Math.PI * 2);
            n++;
        }

        if(Limelights.RIGHT.isValidTarget()) {
            rads += MathUtils.inputModulo(rightRot.getRadians(), 0, Math.PI * 2);
            n++;
        }


        if(n == 0) {
            return null;
        } else {
            return new Rotation2d(rads / n);
        }
    }

    public HumanPlayerLineupState(Drivetrain drivetrain, DoubleSupplier xSpeed, DoubleSupplier ySpeed, PIDController rotationController) {
        super(drivetrain);
        this.rotationController = rotationController;
        this.rotationController.enableContinuousInput(0, Math.PI * 2);

        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
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

        Rotation2d avg = averageLimelights();

        TESTING_TABLE.putNumber("Rot Lock Desired", desiredRotation.getDegrees());

        if(avg == null) {
            TESTING_TABLE.putNumber("Rot Lock Current", requiredSubsystem.getRobotPose().getRotation().getDegrees());
            TESTING_TABLE.putNumber("Rot Lock Output", -1);
            return 0;
        }

        TESTING_TABLE.putNumber("Rot Lock Current", avg.getDegrees());
        double sp = -rotationController.calculate(avg.getRadians(), desiredRotation.getRadians());

        TESTING_TABLE.putNumber("Rot Lock Output", sp);

        return sp;
    }

    @Override
    public boolean isFieldRelative() {
        return true;
    }

    @Override
    public void initialize() {
        rotationController.reset();

        Rotation2d avg = averageLimelights();
        if(avg == null) {
            desiredRotation = requiredSubsystem.getRobotPose().getRotation();
            return;
        }

        double rads = MathUtils.inputModulo(avg.getRadians(), -Math.PI, Math.PI);

        if(Math.abs(rads - Math.PI) < Math.PI / 2) {
            desiredRotation = new Rotation2d(Math.PI);
        } else {
            desiredRotation = new Rotation2d(0);
        }
    }
}
