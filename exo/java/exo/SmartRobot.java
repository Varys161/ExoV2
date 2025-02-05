package exo;

import java.io.PrintStream;

public class SmartRobot implements Robot {

    private Planet planet;
    private String name;
    private Position position;
    private RobotStatutsImpl status;
    private PrintStream out;
    private boolean running = true;

    @Override
    public void initRun(Planet planet, String lander, Position landPos, String userData, RobotStatus initStatus, PrintStream out) {
        this.planet = planet;
        this.name = lander;
        this.position = landPos;
        this.out = out;
        this.status = new RobotStatutsImpl(initStatus.getWorkTemp(), initStatus.getEnergy(), initStatus.getMessage());

        out.println("[" + name + "] Initialisiere Roboter...");

        try {
            // Versuche zu landen
            Measure measure = planet.land(this, landPos);
            if (measure == null) {
                out.println("[" + name + "] Landung fehlgeschlagen!");
                return;
            }
            out.println("[" + name + "] Erfolgreich gelandet bei " + landPos + " - Boden: " + measure.getGround());

            // Starte autonomen Erkundungsmodus
            explore();
        } catch (InterruptedException e) {
            out.println("[" + name + "] Vorgang unterbrochen!");
            running = false;
        }
    }

    private void explore() throws InterruptedException {
        while (running) {
            if (status.getEnergy() < 15) {
                chargeEnergy();
            }

            Measure measure = planet.scan(this);
            out.println("[" + name + "] Scan-Ergebnis: " + measure.getGround() + ", Temp: " + measure.getTemperature());

            Position newPosition = planet.move(this);
            if (newPosition != null) {
                position = newPosition;
                out.println("[" + name + "] Bewegt zu neuer Position: " + position);
            } else {
                // Falls Bewegung nicht möglich ist, drehe den Roboter zufällig
                Rotation rotation = Math.random() > 0.5 ? Rotation.LEFT : Rotation.RIGHT;
                planet.rotate(this, rotation);
                out.println("[" + name + "] Bewegung fehlgeschlagen, drehe " + rotation);
            }

            // Warte eine Sekunde vor der nächsten Aktion
            Thread.sleep(1000);
        }
    }

    private void chargeEnergy() throws InterruptedException {
        out.println("[" + name + "] Akku schwach, versuche Aufladen...");
        RobotStatus newStatus = planet.charge(this, 5);
        status.setEnergy(newStatus.getEnergy());
        status.setMessage(newStatus.getMessage());
        out.println("[" + name + "] Neuer Akkustand: " + status.getEnergy() + "%");
    }

    @Override
    public void crash() {
        out.println("[" + name + "] Roboter ist abgestürzt!");
        running = false;
    }

    @Override
    public void statusChanged(RobotStatus newStatus) {
        status.setWorkTemp(newStatus.getWorkTemp());
        status.setEnergy(newStatus.getEnergy());
        status.setMessage(newStatus.getMessage());
        out.println("[" + name + "] Status geändert: " + newStatus.getMessage());
    }

    @Override
    public String getLanderName() {
        return name;
    }

    package exo;

import java.io.PrintStream;

    public class RobotTest {
        public static void main(String[] args) {
            Planet planet = new SimulatedPlanet(10, 10);
            Robot robot = new SmartRobot();
            Position startPos = new Position(0, 0, Direction.NORTH);
            RobotStatus initialStatus = new RobotStatusImpl(25.0f, 100, "Bereit");

            robot.initRun(planet, "Explorer-1", startPos, "", initialStatus, System.out);
        }
    }

}
