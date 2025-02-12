package exo;

public class Bodenstation {
    public static void main(String[] args) {
        System.out.println("[INFO] Bodenstation gestartet...");

        RobotGUI gui = new RobotGUI();
        gui.setVisible(true);

        RobotManager robotManager = new RobotManager();

        RemoteRobotClient robot1 = new RemoteRobotClient("127.0.0.1", 6732, "ExplorerBot1", 2, 2, gui, robotManager);
        RemoteRobotClient robot2 = new RemoteRobotClient("127.0.0.1", 6732, "ExplorerBot2", 8, 5, gui, robotManager);

        robotManager.addRobot(robot1);
        robotManager.addRobot(robot2);

        new Thread(robot1::startClient).start();
        new Thread(robot2::startClient).start();

        // Warte kurz, bis Verbindung steht
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        // **Neuer Lande-Befehl**
        robotManager.sendLandCommand("ExplorerBot1", 2, 2, "NORTH");
        robotManager.sendLandCommand("ExplorerBot2", 8, 5, "EAST");

        // Warte kurz, damit der Server `landed` verarbeiten kann
        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }

        // Danach erst bewegen und scannen
        robotManager.sendMoveCommand("ExplorerBot1");
        robotManager.sendScanCommand("ExplorerBot1");

        robotManager.sendMoveCommand("ExplorerBot2");
        robotManager.sendScanCommand("ExplorerBot2");
    }
}
