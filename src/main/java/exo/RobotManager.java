package exo;

import java.util.HashMap;
import java.util.Map;

public class RobotManager {
    private Map<String, RemoteRobotClient> robots = new HashMap<>();

    public void addRobot(RemoteRobotClient robot) {
        robots.put(robot.getRobotName(), robot);
        System.out.println("[INFO] Roboter hinzugefügt: " + robot.getRobotName());
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
        RemoteRobotClient robot = robots.get(robotName);
        if (robot != null) {
            String command = String.format(
                    "{\"CMD\":\"land\",\"POSITION\":{\"X\":%d,\"Y\":%d,\"DIRECTION\":\"%s\"}}",
                    x, y, direction
            );
            DatabaseManager.saveCommand(robotName, "land", "ExoPlanet");
            robot.sendCommand(command);
            System.out.println("[INFO] Sende LAND-Kommando für " + robotName + " an (" + x + "," + y + ") Richtung: " + direction);
        } else {
            System.out.println("[ERROR] Roboter nicht gefunden: " + robotName);
        }
    }
}
