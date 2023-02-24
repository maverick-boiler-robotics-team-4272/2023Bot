// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AprilRunCommand;
import frc.robot.commands.ArmSetpointCommand;
import frc.robot.commands.ChargeCircleCommand;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.DriveState;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.intake.states.ConeGrabState;
import frc.robot.subsystems.intake.states.CubeGrabState;
import frc.robot.utils.XboxController;
import frc.team4272.controllers.utilities.JoystickAxes;
import frc.team4272.controllers.utilities.JoystickAxes.DeadzoneMode;
import com.pathplanner.lib.server.PathPlannerServer;

import static frc.robot.constants.AutoConstants.AUTO_CHOOSER;
import static frc.robot.constants.TelemetryConstants.ShuffleboardTables.*;
import static frc.robot.constants.RobotConstants.ArmSubsystemConstants.ArmSetpoints.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    // The robot's subsystems and commands are defined here...
    public Drivetrain drivetrain = new Drivetrain();
    public ArmSubsystem arm = new ArmSubsystem();
    public IntakeSubsystem claw = new IntakeSubsystem();

    // The robot's IO devices and commands are defined here...
    public XboxController driveController = new XboxController(0);
    public XboxController operatorController = new XboxController(1);
    public XboxController demoController1 = new XboxController(2);
    public XboxController demoController2 = new XboxController(3);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        PathPlannerServer.startServer(4272);
        // Run all configuration methods
        configureControllers();
        configureBindings();
        configureAutoSendable();
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
        configureDriverBindings();
        configureOperatorBindings();
        configureDemo1Bindings();
        configureDemo2Bindings();
    }

    private void configureControllers() {
        JoystickAxes driveLeftAxes = driveController.getAxes("left");
        JoystickAxes driveRightAxes = driveController.getAxes("right");

        driveLeftAxes.setDeadzoneMode(DeadzoneMode.kMagnitude).setPowerScale(3.0).setDeadzone(0.15);
        driveRightAxes.setDeadzoneMode(DeadzoneMode.kXAxis).setPowerScale(2.5).setDeadzone(0.15);
    }

    private void configureDriverBindings() {
        JoystickAxes leftAxes = driveController.getAxes("left");
        JoystickAxes rightAxes = driveController.getAxes("right");

        new Trigger(() -> leftAxes.getDeadzonedMagnitude() != 0.0).or(() -> rightAxes.getDeadzonedX() != 0.0).whileTrue(
            new DriveState(drivetrain, leftAxes::getDeadzonedX, leftAxes::getDeadzonedY, rightAxes::getDeadzonedX)
        );

        new Trigger(driveController.getButton("a")::get).onTrue(new InstantCommand(() -> {
            drivetrain.setRobotPose(Limelights.THREE.getRobotPose());
        }, drivetrain));

        new Trigger(driveController.getButton("b")::get).onTrue(new InstantCommand(() -> {
            drivetrain.getGyroscope().setRotation(new Rotation2d(0));
        }, drivetrain));
    }

    private void configureOperatorBindings() {
        arm.setDefaultCommand(new ArmSetpointCommand(arm, HOME));

        new Trigger(() -> operatorController.getTrigger("left").getValue() != 0).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new CubeGrabState(claw, operatorController.getTrigger("left")::getValue)
        );

        new Trigger(() -> operatorController.getTrigger("right").getValue() != 0).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new CubeGrabState(claw, () -> -operatorController.getTrigger("right").getValue())
        );

        new Trigger(operatorController.getButton("a")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, GROUND_CUBE).repeatedly()
        );

        new Trigger(operatorController.getButton("b")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, HUMAN_PLAYER_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("x")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, LOW_CUBE).repeatedly()
        );

        new Trigger(operatorController.getButton("y")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, HIGH_CUBE).repeatedly()
        );

        new Trigger(() -> operatorController.getTrigger("left").getValue() != 0).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ConeGrabState(claw, operatorController.getTrigger("left")::getValue)
        );

        new Trigger(() -> operatorController.getTrigger("right").getValue() != 0).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ConeGrabState(claw, () -> -operatorController.getTrigger("right").getValue())
        );

        new Trigger(operatorController.getButton("a")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, GROUND_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("b")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, HUMAN_PLAYER_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("x")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, LOW_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("y")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointCommand(arm, HIGH_CONE).repeatedly()
        );
    }

    private void configureDemo1Bindings() {

    }

    private void configureDemo2Bindings() {

    }

    private void configureAutoSendable() {
        AUTO_CHOOSER.addOption("Test Path", () -> new RepeatCommand(new AprilRunCommand(drivetrain)));
        AUTO_CHOOSER.addOption("Charge Circle", () -> new RepeatCommand(new ChargeCircleCommand(drivetrain)));


        AUTO_TABLE.putData("Auto Chooser", AUTO_CHOOSER);
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return AUTO_CHOOSER.getSelected().get();
    }
}