package exo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/exo_bodenstation";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("[DEBUG] DB-Verbindung erfolgreich!");
            return conn;
        } catch (SQLException e) {
            System.out.println("[ERROR] Verbindung zur DB fehlgeschlagen: " + e.getMessage());
            return null;
        }
    }

    public static void savePlanet(String name, int width, int height) {
        String sql = "INSERT INTO planeten (name, width, height) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE width=?, height=?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, width);
            stmt.setInt(3, height);
            stmt.setInt(4, width);
            stmt.setInt(5, height);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("[DEBUG] Planet gespeichert: " + name + " → Betroffene Zeilen: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("[ERROR] Konnte Planeten-Daten nicht speichern: " + e.getMessage());
        }
    }

    public static void saveOrUpdateRobot(String name, int x, int y, String direction, int energy, String planetName) {
        String sql = "INSERT INTO roboter (name, x, y, richtung, energie, planet_id) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT id FROM planeten WHERE name=?)) " +
                "ON DUPLICATE KEY UPDATE x=?, y=?, richtung=?, energie=?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, x);
            stmt.setInt(3, y);
            stmt.setString(4, direction);
            stmt.setInt(5, energy);
            stmt.setString(6, planetName);
            stmt.setInt(7, x);
            stmt.setInt(8, y);
            stmt.setString(9, direction);
            stmt.setInt(10, energy);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("[DEBUG] Roboter gespeichert: " + name + " → Betroffene Zeilen: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("[ERROR] Konnte Roboter-Daten nicht speichern: " + e.getMessage());
        }
    }

    public static void saveScanData(int x, int y, String groundType, float temperature, String planetName) {
        String sql = "INSERT INTO erkundung (x, y, bodentyp, temperatur, planet_id) VALUES (?, ?, ?, ?, (SELECT id FROM planeten WHERE name=?))";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, x);
            stmt.setInt(2, y);
            stmt.setString(3, groundType);
            stmt.setFloat(4, temperature);
            stmt.setString(5, planetName);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("[DEBUG] Scan gespeichert: " + x + "," + y + " → " + groundType + " | Betroffene Zeilen: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("[ERROR] Konnte Scan-Daten nicht speichern: " + e.getMessage());
        }
    }
    public static void saveCommand(String robotName, String command, String planetName) {
        String sql = "INSERT INTO befehle (roboter_id, planet_id, befehl) " +
                "VALUES ((SELECT id FROM roboter WHERE name=?), (SELECT id FROM planeten WHERE name=?), ?)";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, robotName);
            stmt.setString(2, planetName);
            stmt.setString(3, command);

            int rowsAffected = stmt.executeUpdate();
            System.out.println("[DEBUG] Befehl gespeichert: " + robotName + " → " + command + " | Betroffene Zeilen: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("[ERROR] Konnte Befehl nicht speichern: " + e.getMessage());
        }
    }

}
