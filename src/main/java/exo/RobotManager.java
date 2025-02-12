package exo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RobotManager {
    private Map<String, RemoteRobotClient> robots = new HashMap<>();
    private final Map<String, int[]> robotPositions = new HashMap<>();
    private final Map<String, String> robotDirections = new HashMap<>();
    private int mapWidth = 0;
    private int mapHeight = 0;


    public void addRobot(RemoteRobotClient robot) {
        robots.put(robot.getRobotName(), robot);
        System.out.println("[INFO] Roboter hinzugefügt: " + robot.getRobotName());
    }

    public void processInitResult(int width, int height) {
        System.out.println("[DEBUG] processInitResult() wurde aufgerufen: WIDTH=" + width + ", HEIGHT=" + height);
        this.mapWidth = width;
        this.mapHeight = height;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }


    public void processLandResult(String robotName, int x, int y, String direction, String planetName) {
        System.out.println("[DEBUG] processLandResult() wurde aufgerufen für " + robotName);
        DatabaseManager.saveOrUpdateRobot(robotName, x, y, direction, 100, planetName);
    }

    public void processMoveResult(String robotName, int x, int y, String direction, int energy, String planetName) {
        System.out.println("[DEBUG] processMoveResult() wurde aufgerufen für " + robotName);
        DatabaseManager.saveOrUpdateRobot(robotName, x, y, direction, energy, planetName);
    }

    public void processScanResult(String robotName, int x, int y, String groundType, float temperature, String planetName) {
        System.out.println("[DEBUG] processScanResult() wurde aufgerufen für " + robotName);
        DatabaseManager.saveScanData(x, y, groundType, temperature, planetName);
    }

    public void sendMoveCommand(String robotName) {
        RemoteRobotClient robot = robots.get(robotName);
        if (robot != null) {
            String command = "{\"CMD\":\"move\"}";
            DatabaseManager.saveCommand(robotName, "move", "ExoPlanet");
            robot.sendCommand(command);
        } else {
            System.out.println("[ERROR] Roboter nicht gefunden: " + robotName);
        }
    }

    public void sendScanCommand(String robotName) {
        RemoteRobotClient robot = robots.get(robotName);
        if (robot != null) {
            String command = "{\"CMD\":\"scan\"}";
            DatabaseManager.saveCommand(robotName, "scan", "ExoPlanet");
            robot.sendCommand(command);
        } else {
            System.out.println("[ERROR] Roboter nicht gefunden: " + robotName);
        }
    }

    public void sendLandCommand(String robotName, int x, int y, String direction) {
        System.out.println("[INFO] Sende LAND-Kommando für " + robotName + " an (" + x + "," + y + ") Richtung: " + direction);

        JSONObject landCommand = new JSONObject();

        JSONObject position = new JSONObject();
        position.put("X", x);
        position.put("Y", y);
        position.put("DIRECTION", direction);

        landCommand.put("CMD", "land");
        landCommand.put("POSITION", position);

        // Position & Richtung speichern
        robotPositions.put(robotName, new int[]{x, y});
        robotDirections.put(robotName, direction);

        sendCommand(robotName, landCommand.toString());
    }

    public void sendCommand(String robotName, String command) {
        RemoteRobotClient robot = robots.get(robotName);
        if (robot != null) {
            robot.sendCommand(command);
        } else {
            System.out.println("[ERROR] Kein Roboter mit Namen " + robotName + " gefunden!");
        }
    }

}
