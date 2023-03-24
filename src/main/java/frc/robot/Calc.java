package frc.robot;

import java.util.Scanner;

import frc.team4272.globals.MathUtils;

import static frc.robot.constants.UniversalConstants.FIELD_WIDTH_METERS;

public class Calc {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String opt;
        while(true) {
            System.out.println("Input x, y, and rotation info");
            System.out.println("Or put done to end");

            opt = scanner.nextLine();

            if(opt.equals("done")) break;
            String[] coords = opt.split(",");

            if(coords.length != 3) {
                System.out.println("Did not input correct number of parameters. Please try again");
                continue;
            }

            double x, y, r;

            try {
                x = Double.parseDouble(coords[0]);
                y = Double.parseDouble(coords[1]);
                r = Double.parseDouble(coords[2]);
            } catch(NumberFormatException e) {
                System.out.println("One of the things inputted was not a number");
                continue;
            }


            x = FIELD_WIDTH_METERS - x;
            r = MathUtils.inputModulo(180 - r, -180, 180);

            System.out.printf("Your mirrored coordinates are: (x: %.2f, y: %.2f, rot: %.2f)\n\n", x, y, r);
        }

        scanner.close();
    }
}
