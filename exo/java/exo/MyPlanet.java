package exo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyPlanet implements Planet {

    private final Size size;
    private final Map<Robot, Position> robotPositions;
    private final Random random;

    public MyPlanet(int width, int height) {
        this.size = new Size(width, height);
        this.robotPositions = new HashMap<>();
        this.random = new Random();
    }

    @Override
    public Measure land(Robot robot, Position landPos) {
        if (!isValidPosition(landPos)) {
            return null; // Ungültige Position -> Landung fehlgeschlagen
        }
        robotPositions.put(robot, landPos);
        return generateRandomMeasure(); // Simulierte Messwerte für die Landeposition
    }

    @Override
    public Position getPosition(Robot robot) {
        return robotPositions.get(robot);
    }

    @Override
    public Position move(Robot robot) {
        Position currentPos = robotPositions.get(robot);
        if (currentPos == null) return null; // Roboter nicht auf dem Planeten

        Position newPos = calculateNewPosition(currentPos);
        if (isValidPosition(newPos)) {
            robotPositions.put(robot, newPos);
            return newPos;
        }
        return null; // Bewegung fehlgeschlagen (Hindernis oder Karte zu Ende)
    }

    @Override
    public Direction rotate(Robot robot, Rotation rotation) {
        Position currentPos = robotPositions.get(robot);
        if (currentPos == null) return null;

        Direction newDir = rotateDirection(currentPos.getDir(), rotation);
        currentPos.setDir(newDir);
        return newDir;
    }

    @Override
    public Measure scan(Robot robot) {
        Position currentPos = robotPositions.get(robot);
        if (currentPos == null) return null;

        return generateRandomMeasure(); // Simulierte Messwerte für das Feld vor dem Roboter
    }

    @Override
    public Measure moveScan(Robot robot, Position newPos) {
        Position movedPos = move(robot);
        if (movedPos == null) return null;
        return generateRandomMeasure();
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public void remove(Robot robot) {
        robotPositions.remove(robot);
    }

    @Override
    public RobotStatus charge(Robot robot, int duration) {
        return new RobotStatutsImpl(20 + random.nextInt(10), 100, "Batterie geladen");
    }

    private boolean isValidPosition(Position pos) {
        return pos.getX() >= 0 && pos.getX() < size.getWidth() &&
                pos.getY() >= 0 && pos.getY() < size.getHeight();
    }

    private Position calculateNewPosition(Position currentPos) {
        int x = currentPos.getX();
        int y = currentPos.getY();
        Direction dir = currentPos.getDir();

        switch (dir) {
            case NORTH: y--; break;
            case EAST:  x++; break;
            case SOUTH: y++; break;
            case WEST:  x--; break;
        }
        return new Position(x, y, dir);
    }

    private Direction rotateDirection(Direction current, Rotation rotation) {
        switch (current) {
            case NORTH: return (rotation == Rotation.LEFT) ? Direction.WEST : Direction.EAST;
            case EAST:  return (rotation == Rotation.LEFT) ? Direction.NORTH : Direction.SOUTH;
            case SOUTH: return (rotation == Rotation.LEFT) ? Direction.EAST : Direction.WEST;
            case WEST:  return (rotation == Rotation.LEFT) ? Direction.SOUTH : Direction.NORTH;
            default: return current;
        }
    }

    private Measure generateRandomMeasure() {
        Ground[] grounds = Ground.values();
        return new Measure(grounds[random.nextInt(grounds.length)], random.nextFloat() * 100);
    }
}
