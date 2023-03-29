package frc.robot.utils;

import frc.team4272.controllers.Controller;

public class XboxController extends Controller {
    public static enum POVDirection {
        UP(0),
        UP_RIGHT(45),
        RIGHT(90),
        DOWN_RIGHT(135),
        DOWN(180),
        DOWN_LEFT(225),
        LEFT(270),
        UP_LEFT(315);

        private final int povValue;
        private POVDirection(int povValue) {
            this.povValue = povValue;
        }

        public boolean matches(int povValue) {
            return this.povValue == povValue;
        }
    }

    public XboxController(int port) {
        super(port);

        addButton("a", 1);
        addButton("b", 2);
        addButton("x", 3);
        addButton("y", 4);
        addButton("leftBumper", 5);
        addButton("rightBumper", 6);
        addButton("back", 7);
        addButton("start", 8);
        addButton("leftStick", 9);
        addButton("rightStick", 10);

        addAxes("left", 0, 1);
        addAxes("right", 4, 5);

        addTrigger("left", 2);
        addTrigger("right", 3);

        addPOV("d-pad", 0);
    }
}
