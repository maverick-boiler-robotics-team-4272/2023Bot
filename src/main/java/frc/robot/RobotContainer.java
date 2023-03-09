// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DefaultAutoCommand;
import frc.robot.commands.LaunchCubeCommand;
import frc.robot.commands.OneConeCharge;
import frc.robot.commands.TwoPieceCommand;
import frc.robot.constants.TelemetryConstants.Limelights;
import frc.robot.subsystems.arm.ArmSubsystem;
import frc.robot.subsystems.arm.states.ArmSetpointState;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.drivetrain.states.DriveState;
import frc.robot.subsystems.drivetrain.states.ResetHeadingState;
import frc.robot.subsystems.drivetrain.states.ResetPoseState;
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
    public IntakeSubsystem intake = new IntakeSubsystem();

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
        driveRightAxes.setDeadzoneMode(DeadzoneMode.kXAxis).setPowerScale(3.0).setDeadzone(0.15);
    }

    private void configureDriverBindings() {
        JoystickAxes leftAxes = driveController.getAxes("left");
        JoystickAxes rightAxes = driveController.getAxes("right");

        new Trigger(() -> leftAxes.getDeadzonedMagnitude() != 0.0).or(() -> rightAxes.getDeadzonedX() != 0.0).whileTrue(
            new DriveState(drivetrain, leftAxes::getDeadzonedX, leftAxes::getDeadzonedY, rightAxes::getDeadzonedX)
        );

        new Trigger(driveController.getButton("a")::get).onTrue(new ResetPoseState(drivetrain, Limelights.CENTER));

        new Trigger(driveController.getButton("b")::get).onTrue(new ResetHeadingState(drivetrain));

        new Trigger(driveController.getButton("x")::get).onTrue(new InstantCommand(drivetrain::resetModules, drivetrain));
        
        new Trigger(driveController.getButton("rightBumper")::get).onTrue(new InstantCommand(drivetrain::xConfig, drivetrain));

        //TODO: Move to other controller when buttons are available
        new Trigger(driveController.getButton("leftBumper")::get).whileTrue(
            new LaunchCubeCommand(arm, intake)
        );
    }

    private void configureOperatorBindings() {
        arm.setDefaultCommand(new ArmSetpointState(arm, HOME));

        new Trigger(() -> operatorController.getTrigger("left").getValue() != 0).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new CubeGrabState(intake, operatorController.getTrigger("left")::getValue)
        );

        new Trigger(() -> operatorController.getTrigger("right").getValue() != 0).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new CubeGrabState(intake, () -> -operatorController.getTrigger("right").getValue())
        );

        new Trigger(operatorController.getButton("a")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointState(arm, GROUND_CUBE).repeatedly()
        );

        new Trigger(operatorController.getButton("b")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointState(arm, HUMAN_PLAYER_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("x")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointState(arm, MID_CUBE).repeatedly()
        );

        new Trigger(operatorController.getButton("y")::get).and(operatorController.getButton("rightBumper")::get).whileTrue(
            new ArmSetpointState(arm, HIGH_CUBE).repeatedly()
        );

        new Trigger(() -> operatorController.getTrigger("left").getValue() != 0).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ConeGrabState(intake, operatorController.getTrigger("left")::getValue)
        );

        new Trigger(() -> operatorController.getTrigger("right").getValue() != 0).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ConeGrabState(intake, () -> -operatorController.getTrigger("right").getValue())
        );

        new Trigger(operatorController.getButton("a")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointState(arm, GROUND_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("b")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointState(arm, HUMAN_PLAYER_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("x")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointState(arm, MID_CONE).repeatedly()
        );

        new Trigger(operatorController.getButton("y")::get).and(operatorController.getButton("leftBumper")::get).whileTrue(
            new ArmSetpointState(arm, HIGH_CONE).repeatedly()
        );
    }

    private void configureDemo1Bindings() {

    }

    private void configureDemo2Bindings() {

    }

    private void configureAutoSendable() {
        // AUTO_CHOOSER.addOption("One Cone", () -> new OneConeCommand(drivetrain, arm, intake));
        // AUTO_CHOOSER.addOption("Two Cone", () -> new TwoConeCommand(drivetrain, arm, intake));
        AUTO_CHOOSER.addOption("Charge Station", () -> new OneConeCharge(drivetrain, arm, intake));
        // AUTO_CHOOSER.addOption("Community Charge Station", () -> new ChargeStationExit(drivetrain, arm, intake));
        AUTO_CHOOSER.addOption("Two Piece", () -> new TwoPieceCommand(drivetrain, arm, intake));
        // AUTO_CHOOSER.addOption("One Half Charge", () -> new TwoPieceCharge(drivetrain, arm, intake));
        AUTO_CHOOSER.setDefaultOption("Default Auto", () -> new DefaultAutoCommand(drivetrain, arm, intake));

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