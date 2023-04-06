package frc.robot;

import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import frc.team4272.globals.MathUtils;
import static frc.robot.constants.UniversalConstants.FIELD_WIDTH_METERS;


public class Mirror {
    private static final String PATH_DIRECTORY = "C:/Users/itadmin/Documents/github/RoboticsRepos/2023Bot/src/main/deploy/pathplanner/";
    public static void main(String[] args) throws Exception {

    }

    @SuppressWarnings({"unused"})
    private static void mirrorPath(String pathName, boolean fromRed) throws Exception {
        String fromPrefix;
        String toPrefix;

        if(fromRed) {
            fromPrefix = "Red ";
            toPrefix = "Blue ";
        } else {
            fromPrefix = "Blue ";
            toPrefix = "Red ";
        }

        JSONObject path = loadJson(PATH_DIRECTORY + fromPrefix + pathName + ".path");

        JSONArray waypoints = (JSONArray) path.get("waypoints");
        for(int i = 0; i < waypoints.size(); i++) {
            JSONObject waypoint = (JSONObject) waypoints.get(i);

            double r = 0;

            Object angle = waypoint.get("holonomicAngle");

            if(angle instanceof Long) {
                r = (double)((long) angle);
            } else if (angle instanceof Double) {
                r = (double) angle;
            }

            waypoint.put("holonomicAngle", MathUtils.inputModulo(180 - r, -180, 180));

            JSONObject anchor = (JSONObject) waypoint.get("anchorPoint");
            JSONObject prevControl = (JSONObject) waypoint.get("prevControl");
            JSONObject nextControl = (JSONObject) waypoint.get("nextControl");

            mirrorPoint(anchor);
            mirrorPoint(prevControl);
            mirrorPoint(nextControl);
        }

        FileWriter writer = new FileWriter(PATH_DIRECTORY + toPrefix + pathName + ".path");

        writer.append(path.toString());

        writer.close();
    }

    private static void mirrorPoint(JSONObject point) {
        if(point != null) {
            double x = (double) point.get("x");

            point.put("x", Double.parseDouble("%.2f".formatted(FIELD_WIDTH_METERS - x)));
        }
    }

    private static JSONObject loadJson(String path) throws Exception {
        JSONParser parser = new JSONParser();

        FileReader reader = new FileReader(path);
        
        return (JSONObject) parser.parse(reader);
    }
}
