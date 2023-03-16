package frc.robot.subsystems.candle;

import com.ctre.phoenix.led.Animation;
import com.ctre.phoenix.led.CANdle;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.constants.HardwareMap.*;
import static frc.robot.constants.UniversalConstants.IS_PRACTICE_BOT;

public class Candle extends SubsystemBase {
    private CANdle candle;

    public Candle() {
        if(IS_PRACTICE_BOT) {
            candle = null;
            return;
        }
        candle = new CANdle(CANDLE_ID);
    }

    public void setLED(int index, int r, int g, int b) {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(r, g, b, 0, index, 1);
    }

    public void setLED(int index, int brightness) {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(0, 0, 0, brightness, index, 1);
    }

    public void setLEDs(int startIndex, int count, int r, int g, int b) {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(r, g, b, 0, startIndex, count);
    }

    public void setLEDs(int startIndex, int count, int brightness) {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(0, 0, 0, brightness, startIndex, count);
    }

    public void setAllLEDs(int r, int g, int b) {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(r, g, b, 0, 0, 8);
    }

    public void setAllLEDs(int brightness) {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(0, 0, 0, brightness, 0, 8);
    }

    public void animate(Animation animation) {
        if(IS_PRACTICE_BOT) return;
        candle.animate(animation);
    }

    public void turnOffLEDs() {
        if(IS_PRACTICE_BOT) return;
        candle.setLEDs(0, 0, 0, 0, 0, 8);
    }
}
