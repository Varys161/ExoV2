package exo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class RemoteRobotClient implements Runnable {
    private String hostname;
    private int port;
    private String robotName;
    private int startX;
    private int startY;
    private BufferedReader in;
    private PrintStream out;
    private Socket socket;
    private RobotGUI gui;
    private RobotManager robotManager;

    public RemoteRobotClient(String hostname, int port, String robotName, int startX, int startY, RobotGUI gui, RobotManager robotManager) {
        this.hostname = hostname;
        this.port = port;
        this.robotName = robotName;
        this.startX = startX;
        this.startY = startY;
        this.gui = gui;
        this.robotManager = robotManager;
    }

    @Override
    public void run() {
        startClient();
    }

    public void startClient() {
        try {
            socket = new Socket(hostname, port);
            out = new PrintStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            gui.log("[INFO] " + robotName + " verbunden mit Server: " + hostname + ":" + port);
            sendCommand("{\"CMD\":\"orbit\",\"NAME\":\"" + robotName + "\"}");

            // Automatisches Landen nach Orbit
            Thread.sleep(500);
            robotManager.sendLandCommand(robotName, startX, startY, "NORTH");

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("[DEBUG] Empfangene Server-Nachricht: " + line);
                ExoResponseParser.parseServerResponse(line, gui, robotName, robotManager);
            }
        } catch (IOException | InterruptedException e) {
            gui.log("[ERROR] Verbindung fehlgeschlagen für " + robotName + ": " + e.getMessage());
        }
    }

    public void sendCommand(String command) {
        if (out != null) {
            gui.log("[SEND " + robotName + "] " + command);
            out.println(command);
            out.flush();
        } else {
            gui.log("[ERROR] " + robotName + ": PrintStream `out` ist NULL!");
        }
    }

    public String getRobotName() {
        return robotName;
    }
}
