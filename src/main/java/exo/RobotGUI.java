package exo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import javax.imageio.ImageIO;

public class RobotGUI {
    private static final int TILE_SIZE = 40;
    static int MAP_WIDTH = 24;
    static int MAP_HEIGHT = 16;
    private static int[][] map = new int[MAP_HEIGHT][MAP_WIDTH];
    private static HashMap<Point, String> scannedTiles = new HashMap<>();
    private static HashMap<Point, Boolean> roverPositions = new HashMap<>();
    private static Image lavaTile, stoneTile, grassTile, sandTile,
            waterTile, stationIcon, playerIcon, plantTile;
    private static TileMapPanel mapPanel;
    private static JFrame frame = new JFrame();
    private static Point roverPosition = new Point(0, 0);
    private static Random random = new Random();
    private static boolean autoPilotEnabled = false;
    private static Thread autoPilotThread;

    public static void launch(int width, int height) {
        SwingUtilities.invokeLater(() -> {
            try {
                grassTile = ImageIO.read(new File("grasss.jpg"));
                waterTile = ImageIO.read(new File("water.jpg"));
                sandTile = ImageIO.read(new File("rocky.jpg"));
                lavaTile = ImageIO.read(new File("lavap.jpg"));
                stoneTile = ImageIO.read(new File("stone.jpg"));
                plantTile = ImageIO.read(new File("plants.jpg"));
                playerIcon = ImageIO.read(new File("robot-preview.jpg"));
                stationIcon = ImageIO.read(new File("station.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            frame = new JFrame("Exo Planet");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            // Create the map panel
            mapPanel = new TileMapPanel();
            frame.add(mapPanel, BorderLayout.CENTER);

            JButton toggleAutoPilotButton = new JButton("Toggle AutoPilot");
            toggleAutoPilotButton.addActionListener(e -> toggleAutoPilot());
            frame.add(toggleAutoPilotButton, BorderLayout.SOUTH);

            frame.setSize(width * TILE_SIZE + 200, height * TILE_SIZE + 50);
            frame.setVisible(true);

            // Generate a random map with the given width and height
            generateRandomMap(width, height);
        });
    }

    public static void updateTile(int x, int y, String groundType) {
        scannedTiles.put(new Point(x, y), groundType);
        mapPanel.repaint();
    }

    public static void updateRoverPosition(int x, int y) {
        roverPositions.clear(); // Assuming only one rover, clear previous position
        roverPosition.setLocation(x, y);
        roverPositions.put(new Point(x, y), true);
        mapPanel.repaint();
    }
    public static void generateRandomMap(int width, int height) {
        map = new int[height][width];  // Initialize the map with the given dimensions

        // Generate random terrain for each tile
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // Randomly assign a terrain type (0 = grass, 1 = water, 2 = sand, 3 = plant, 4 = stone, 5 = lava)
                map[row][col] = random.nextInt(6); // Random number between 0 and 5
            }
        }

        // After generating the map, update the GUI to reflect the new map
        mapPanel.repaint();
    }

    public static void toggleAutoPilot() {
        if (autoPilotEnabled) {
            autoPilotEnabled = false;
            if (autoPilotThread != null) {
                autoPilotThread.interrupt();
            }
        } else {
            autoPilotEnabled = true;
            autoPilotThread = new Thread(() -> {
                while (autoPilotEnabled) {
                    int dx = random.nextInt(3) - 1; // -1, 0, or 1
                    int dy = random.nextInt(3) - 1;
                    int newX = Math.max(0, Math.min(MAP_WIDTH - 1, roverPosition.x + dx));
                    int newY = Math.max(0, Math.min(MAP_HEIGHT - 1, roverPosition.y + dy));
                    updateRoverPosition(newX, newY);
                    try {
                        Thread.sleep(1000); // Move every second
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            });
            autoPilotThread.start();
        }
    }

    public static void log(String message) {
        System.out.println("[GUI] " + message);
    }

    static class TileMapPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int row = 0; row < MAP_HEIGHT; row++) {
                for (int col = 0; col < MAP_WIDTH; col++) {
                    int tileType = map[row][col];
                    Image tileImage = switch (tileType) {
                        case 0 -> grassTile;
                        case 1 -> waterTile;
                        case 2 -> sandTile;
                        case 3 -> plantTile;
                        case 4 -> stoneTile;
                        case 5 -> lavaTile;
                        default -> null;
                    };

                    if (tileImage != null) {
                        g.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                    }
                }
            }

            if (playerIcon != null) {
                g.drawImage(playerIcon, roverPosition.x * TILE_SIZE, roverPosition.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
            }

            if (stationIcon != null) {
                g.drawImage(stationIcon, roverPosition.x * TILE_SIZE, roverPosition.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}