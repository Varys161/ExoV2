package exo;

public interface RobotStatus {

    public float getWorkTemp();

    public int getEnergy();

    public String getMessage();

    public static RobotStatus parse(String s) {
        String[] token = s.trim().split("\\|");
        if (token.length == 4 && token[0].equals("STATUS")) {
            try {
                float temp = Float.parseFloat(token[1]);
                int energy = Integer.parseInt(token[2]);
                String message = token[3];
                return new RobotStatusImpl(temp, energy, message);
            } catch (Exception e) {
            }
        }
        return null;
    }
}