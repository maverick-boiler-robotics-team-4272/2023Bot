// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DriveState;
import frc.robot.commands.PathFollowState;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.XboxController;
import frc.team4272.controllers.utilities.JoystickAxes;
import frc.team4272.controllers.utilities.JoystickAxes.DeadzoneMode;

import static frc.robot.Constants.DrivetrainConstants.*;
import static frc.robot.Constants.FieldViewConstants.*;
import static frc.robot.Constants.Limelights.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    public Drivetrain drivetrain = new Drivetrain();

    // The robot's IO devices and commands are defined here...
    public XboxController driveController = new XboxController(0);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        // Configure the trigger bindings
        configureBindings();

        drivetrain.setMaxSpeeds(MAX_TRANS_SPEED, MAX_ROT_SPEED, MAX_MODULE_SPEED);
        
        TABLE.putData("Field", FIELD);
        TABLE.putData("Gyroscope", drivetrain.getGyroscope());
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
     * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    private void configureBindings() {
        JoystickAxes leftAxes = driveController.getAxes("left");
        JoystickAxes rightAxes = driveController.getAxes("right");

        leftAxes.setDeadzoneMode(DeadzoneMode.kMagnitude).setPowerScale(3.0).setDeadzone(0.15);
        rightAxes.setDeadzoneMode(DeadzoneMode.kXAxis).setPowerScale(2.5).setDeadzone(0.15);

        drivetrain.setDefaultCommand(new DriveState(drivetrain, leftAxes::getDeadzonedX, leftAxes::getDeadzonedY, rightAxes::getDeadzonedX));

        // new Trigger(driveController.getButton("b")::get).onTrue(new InstantCommand(() -> {
        //     drivetrain.getGyroscope().setRotation(new Rotation2d(0));
        // }, drivetrain));

        new Trigger(driveController.getButton("a")::get).onTrue(new InstantCommand(() -> {
            drivetrain.setRobotPose(THREE.getRobotPose());
            
        }, drivetrain));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return new RepeatCommand(new PathFollowState(drivetrain, Constants.Paths.APRIL_SHUFFLE));
    }
}
