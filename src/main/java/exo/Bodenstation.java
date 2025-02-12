package exo;

public class Bodenstation {
    public static void main(String[] args) {
        System.out.println("[INFO] Bodenstation gestartet...");

        RobotGUI gui = new RobotGUI();
        gui.setVisible(true);

        RobotManager robotManager = new RobotManager();

        // Roboter erstellen
        RemoteRobotClient robot1 = new RemoteRobotClient("127.0.0.1", 6732, "ExplorerBot1", 2, 2, gui, robotManager);
        RemoteRobotClient robot2 = new RemoteRobotClient("127.0.0.1", 6732, "ExplorerBot2", 8, 5, gui, robotManager);

        robotManager.addRobot(robot1);
        robotManager.addRobot(robot2);

        // Roboter-Threads starten
        new Thread(robot1::startClient).start();
        new Thread(robot2::startClient).start();

        // Warte kurz, bis die Verbindung hergestellt ist
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Karten-Grid aus `orbit`-Befehl initialisieren
        int width = robotManager.getMapWidth();
        int height = robotManager.getMapHeight();
        RobotGUI.launch(width, height);

        // Warte kurz, bis der Server `init` verarbeitet hat
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // **Neuer Lande-Befehl**
        robotManager.sendLandCommand("ExplorerBot1", 2, 2, "NORTH");
        robotManager.sendLandCommand("ExplorerBot2", 8, 5, "EAST");

        // Warte kurz, damit der Server `landed` verarbeiten kann
        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Danach erst bewegen und scannen
        robotManager.sendMoveCommand("ExplorerBot1");
        robotManager.sendScanCommand("ExplorerBot1");
        RobotGUI.updateTile(2, 2, "FELS"); // Beispielwert - sollte aus `scaned` kommen

        robotManager.sendMoveCommand("ExplorerBot2");
        robotManager.sendScanCommand("ExplorerBot2");
        RobotGUI.updateTile(8, 5, "WASSER"); // Beispielwert - sollte aus `scaned` kommen
    }
}
