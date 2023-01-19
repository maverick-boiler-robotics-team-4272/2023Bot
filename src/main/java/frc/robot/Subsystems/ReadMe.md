# Subsystem Spec
## An Overview
This document serves to explain the basic functionally of each subsytem. 

This spec is a living breathing document. Much like Doctor Frankestiens monster, at times it's going to be scared, confused, and a little ugly. Treat it kindly, and it will love you like a Father. Descriptions will update as robot design decisions (both design and programmatically) are made. Please let us know if there is anything that seems out of date or inaccurate.

This does not describe how each subsystem internally algorithms work. That will be done in a different location. It simply discusses what each subsystem is meant to do, it's states are, and what mechanical motors/pneumatics/sensors it has.

Thanks!

*Marketing Note:* Make sure to highlight the fact that we use state-based programming. 

## Spec Author
Carl Lee Landskron
Grady Whelan

## Dev Team
Danny Quednow
Andrew Lohr

## Resources
[WPILIB Subsystems](https://docs.wpilib.org/en/stable/docs/software/commandbased/subsystems.html)


## Nongoals



# Drivetrain
The drivetrain's job is to get the robot from point A to B, whether that be by driver input or trajectory following. It uses curvature drive and motion-profiling with quintic spline generation.

*Marketing Note:* Motion profiling + Curvature drive are very advance topics, please make sure include highlight them in any media about the drivetrain.

## States
### Open Loop (default)
In this state, the driver is in full control of movement. Joystick input will be fed directly in as power inputs to the motors (after being cubed in order to give more control to the drivers)
### Path Follower
In this state, the motion is controlled by a trajectory. This trajectory is fed in as a parameter. It sets the speed of the drivetrain based on an internal timer. This state automatically ends when the timer reaches or exceeds the total time of the trajectory.
## Hardware
6 FalconFX's for driving (10.86:1)

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

# Climber
This subsystem is in charge of raising and lowering the robot by attaching to the medium bar.
## States
### Position
This state takes in an elevator position enum and moves the arm to the correct position accordingly.
### Manual
This state allows for manual movement up and down of the elevator. It should not allow the arm to go past the upper and lower limits. Make sure the speed is controllable to find intermediate positions with ease.
## Hardware
1 FalconFX (10.61:1)



# Writing Specs
Specs written based on approach by Joel Spolsky.

[Painless Functional Specifications](https://www.joelonsoftware.com/2000/10/02/painless-functional-specifications-part-1-why-bother/)

[A Practical Guide to Writing Techincal Specs](https://stackoverflow.blog/2020/04/06/a-practical-guide-to-writing-technical-specs/)

[Markdown and Visual Code Docs](https://code.visualstudio.com/docs/languages/markdown)

[Markdown Cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#links)
