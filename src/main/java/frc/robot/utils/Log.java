package frc.robot.utils;

import java.io.*;

public class Log {
    private static Log instance = null;

    public static Log getInstance() {
        if(instance == null)
            instance = new Log();

        return instance;
    }

    private PrintWriter logger;
    private long startTime;
    private int timesReset = 0;
    public Log() {
        try {
            File logDir = new File("/home/lvuser/logs");

            if(!logDir.exists()) {
                if(logDir.mkdir()) {
                    System.out.println("Made Directory");
                } else {
                    System.out.println("Failed to make Directory");
                }
            }

            logger = new PrintWriter("/home/lvuser/logs/output" + (timesReset++) + ".csv", "UTF-8");
            logger.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void logData(Object... data) {
        logger.print((System.currentTimeMillis() - startTime) / 1000.0);

        for(Object o : data) {
            logger.print(',');
            logger.print(o);
        }

        logger.print('\n');
        logger.flush();
    }

    public void reset() {
        if(logger != null) logger.flush();
    }

    public void resetTimeStamp() {
        startTime = System.currentTimeMillis();
    }
}
