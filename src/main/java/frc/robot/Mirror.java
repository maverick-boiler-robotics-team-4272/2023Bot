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
        mirrorPath("Two Cone");
        mirrorPath("Third Cube");
        mirrorPath("Fourth Cube");
    }

    private static void mirrorPath(String pathName) throws Exception {
        JSONObject path = loadJson(PATH_DIRECTORY + "Red " + pathName + ".path");

        JSONArray waypoints = (JSONArray) path.get("waypoints");
        for(int i = 0; i < waypoints.size(); i++) {
            JSONObject waypoint = (JSONObject) waypoints.get(i);

            double r = (double) waypoint.get("holonomicAngle");
            waypoint.put("holonomicAngle", MathUtils.inputModulo(180 - r, -180, 180));

            JSONObject anchor = (JSONObject) waypoint.get("anchorPoint");
            JSONObject prevControl = (JSONObject) waypoint.get("prevControl");
            JSONObject nextControl = (JSONObject) waypoint.get("nextControl");

            mirrorPoint(anchor);
            mirrorPoint(prevControl);
            mirrorPoint(nextControl);
        }

        FileWriter writer = new FileWriter(PATH_DIRECTORY + "Blue " + pathName + ".path");

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
