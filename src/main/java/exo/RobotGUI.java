package exo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RobotGUI {

    private static final int TILE_SIZE = 40;
    private static int mapWidth;
    private static int mapHeight;
    private static int[][] map;
    private static HashMap<Point, String> scannedTiles = new HashMap<>();
    private static Image grassTile, sandTile, waterTile, stoneTile, lavaTile, plantTile;
    private static TileMapPanel mapPanel;
    private static JFrame frame = new JFrame();

    public static void launch(int width, int height) {
        mapWidth = width;
        mapHeight = height;
        map = new int[mapHeight][mapWidth];

        SwingUtilities.invokeLater(() -> {
            try {
                grassTile = ImageIO.read(new File("grasss.jpg"));
                waterTile = ImageIO.read(new File("water.jpg"));
                sandTile = ImageIO.read(new File("rocky.jpg"));
                lavaTile = ImageIO.read(new File("lavap.jpg"));
                stoneTile = ImageIO.read(new File("stone.jpg"));
                plantTile = ImageIO.read(new File("plants.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            frame = new JFrame("Exo Planet");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            mapPanel = new TileMapPanel();
            frame.add(mapPanel, BorderLayout.CENTER);

            frame.setSize(mapWidth * TILE_SIZE + 200, mapHeight * TILE_SIZE + 50);
            frame.setVisible(true);
        });
    }

    public static void updateTile(int x, int y, String groundType) {
        scannedTiles.put(new Point(x, y), groundType);
        mapPanel.repaint();
    }

    public static void log(String message) {
        System.out.println("[GUI] " + message);
    }

    static class TileMapPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int row = 0; row < mapHeight; row++) {
                for (int col = 0; col < mapWidth; col++) {
                    Image tileImage = grassTile;
                    Point point = new Point(col, row);
                    if (scannedTiles.containsKey(point)) {
                        switch (scannedTiles.get(point)) {
                            case "SAND": tileImage = sandTile; break;
                            case "WATER": tileImage = waterTile; break;
                            case "STONE": tileImage = stoneTile; break;
                            case "LAVA": tileImage = lavaTile; break;
                            case "PLANTS": tileImage = plantTile; break;
                        }
                    }
                    g.drawImage(tileImage, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
                }
            }
        }
    }
    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }

}
