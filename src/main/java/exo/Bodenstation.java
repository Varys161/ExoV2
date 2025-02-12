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

        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }

        int width = robotManager.getMapWidth();
        int height = robotManager.getMapHeight();
        gui.launch(width, height);

        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        robotManager.sendLandCommand("ExplorerBot1", 2, 2, "NORTH");
        robotManager.sendLandCommand("ExplorerBot2", 8, 5, "EAST");

        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }

        robotManager.sendMoveCommand("ExplorerBot1");
        robotManager.sendScanCommand("ExplorerBot1");
        gui.updateTile(2, 2, "FELS");

        robotManager.sendMoveCommand("ExplorerBot2");
        robotManager.sendScanCommand("ExplorerBot2");
        gui.updateTile(8, 5, "WASSER");
    }
}
