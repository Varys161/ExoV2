package exo;

public class ExoResponseParser {
    public static void parseServerResponse(String response, RobotGUI gui, String robotName, RobotManager robotManager) {
        if (response == null || response.trim().isEmpty()) {
            gui.log("[ERROR] Ungültige Server-Antwort: NULL oder leer");
            return;
        }

        gui.log("[DEBUG] Server-Antwort empfangen: " + response);

        if (response.contains("\"CMD\":\"landed\"")) {
            int x = extractInt(response, "\"X\":");
            int y = extractInt(response, "\"Y\":");
            String direction = extractString(response, "\"DIRECTION\":\"");

            System.out.println("[DEBUG] Landed-Befehl erkannt für " + robotName);
            robotManager.processLandResult(robotName, x, y, direction, "ExoPlanet");
        }

        if (response.contains("\"CMD\":\"moved\"")) {
            int x = extractInt(response, "\"X\":");
            int y = extractInt(response, "\"Y\":");
            String direction = extractString(response, "\"DIRECTION\":\"");

            System.out.println("[DEBUG] Move-Befehl erkannt für " + robotName);
            robotManager.processMoveResult(robotName, x, y, direction, 100, "ExoPlanet");
        }

        if (response.contains("\"CMD\":\"scaned\"")) {
            int x = -1;
            int y = -1;
            String groundType = extractString(response, "\"GROUND\":\"");
            float temperature = extractFloat(response, "\"TEMP\":");

            System.out.println("[DEBUG] Scan-Befehl erkannt für " + robotName);
            robotManager.processScanResult(robotName, x, y, groundType, temperature, "ExoPlanet");
        }
    }

    public static int extractInt(String json, String key) {
        try {
            int start = json.indexOf(key) + key.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return Integer.parseInt(json.substring(start, end).trim());
        } catch (Exception e) {
            System.out.println("[ERROR] Konnte Integer für Schlüssel " + key + " nicht extrahieren.");
            return -1;
        }
    }

    public static float extractFloat(String json, String key) {
        try {
            int start = json.indexOf(key) + key.length();
            int end = json.indexOf(",", start);
            if (end == -1) end = json.indexOf("}", start);
            return Float.parseFloat(json.substring(start, end).trim());
        } catch (Exception e) {
            System.out.println("[ERROR] Konnte Float für Schlüssel " + key + " nicht extrahieren.");
            return -999.9f;
        }
    }

    public static String extractString(String json, String key) {
        try {
            int start = json.indexOf(key) + key.length();
            int end = json.indexOf("\"", start + 1);
            return json.substring(start, end);
        } catch (Exception e) {
            System.out.println("[ERROR] Konnte String für Schlüssel " + key + " nicht extrahieren.");
            return "UNKNOWN";
        }
    }

}
