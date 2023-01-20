# Subsystem Spec
## An Overview
This document serves to explain the basic functionally of each subsytem. 

This spec is a living breathing document. Much like Doctor Frankestiens monster, at times it's going to be scared, confused, and a little ugly. Treat it kindly, and it will love you like a Father. Descriptions will update as robot design decisions (both design and programmatically) are made. Please let us know if there is anything that seems out of date or inaccurate.

This does not describe how each subsystem internally algorithms work. That will be done in a different location. It simply discusses what each subsystem is meant to do, it's states are, and what mechanical motors/pneumatics/sensors it has.

Thanks!

*Marketing Note:* Make sure to highlight the fact that we use state-based programming. 

## Spec Author
Carl Lee Landskron </br>
Grady Whelan

## Dev Team
Daniel Quednow </br>
Andrew Lohr

## Resources
[WPILIB Subsystems](https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html)


## Nongoals



# Drivetrain
The drivetrain's job is to get the robot from point A to B, whether that be by driver input or path following. It uses swerve drive, motion profiling via WPILib path following libraries (TODO check/clarify this), and odometry tracking both internally and via updates from fiducials (April Tags) on the field.

*Marketing Note:* Motion profiling and odometry tracking are advanced topics; please make sure include highlight them in any media about the drivetrain.

## States
### Drive State (default)
In this state, the driver is in full control of movement. Controller input will be converted to x, y, and rotational velocity to accurately move the drivetrain as controlled.
### Path Follower
In this state, the motion is controlled by a trajectory. This trajectory is fed in as a parameter. It sets the speed of the drivetrain based on an internal timer (TODO update when it is known whether it is time-based or position-reached / got-there based). This state automatically ends when the timer reaches or exceeds the total time of the trajectory.
### Rotation Lock
In this state, the translation is controlled by the driver as described in the Drive State, but rotation is locked to ensure the robot is facing a particular point or direction at all times.
### Point State
In this state, the robot moves directly, in a straight line, to another point on the field, without regard for anything potentially blocking the robot. 

## Hardware
Swerve Drive Specialties MK4i using </br>
4 NEO Brushless motors paired with Spark Max motor controller for driving (6.75:1) </br>
4 NEO Brushless motors paired with Spark Max motor controller for module rotation (150:7)

TODO update all other subsystems below

# Intake
The intake's job is to collect balls from the ground. It also ejects balls for scoring.

## States
### Idle
The intake rollers stop spinning.
### Collecting
The intake roller spin inward.
### Ejecting
The intake rollers spin outwards to score balls.

## Hardware
1 VictorSPX (7:1 Redline)

# Arm
The Arm's job is to move the intake to the ground to collect balls, then move it up to score them.

*Technical Note:* The arm may also be involve in the high climb. Discuss with mechanical whether this is happening, so we can update this portion of the spec.

## States
### Position
This state takes in an arm position enum and moves the arm to the correct position accordingly.
### Manual
This state allows for manual movement up and down of the arm. It should not allow the arm to go past the upper and lower limits. Make sure the speed is controllable to find intermediate positions with ease.

## Hardware
1 FalonFX (105:1)




# Writing Specs
Specs written based on approach by Joel Spolsky.

[Painless Functional Specifications](https://www.joelonsoftware.com/2000/10/02/painless-functional-specifications-part-1-why-bother/)

[A Practical Guide to Writing Techincal Specs](https://stackoverflow.blog/2020/04/06/a-practical-guide-to-writing-technical-specs/)

[Markdown and Visual Code Docs](https://code.visualstudio.com/docs/languages/markdown)

[Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#links)
