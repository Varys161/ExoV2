package exo;

import java.io.Serializable;

public class RobotStatutsImpl implements RobotStatus, Serializable {

    private static final long serialVersionUID = 1L;

    private float workTemp;
    private int energy;
    private String message;

    public RobotStatutsImpl(float workTemp, int energy, String message) {
        this.workTemp = workTemp;
        this.energy = energy;
        this.message = message;
    }

    @Override
    public float getWorkTemp() {
        return workTemp;
    }

    public void setWorkTemp(float workTemp) {
        this.workTemp = workTemp;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(100, energy)); // Begrenzung auf 0-100%
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "STATUS|" + workTemp + "|" + energy + "|" + message;
    }

    public static RobotStatutsImpl parse(String s) {
        String[] token = s.trim().split("\\|");
        if (token.length == 4 && token[0].equals("STATUS")) {
            try {
                float temp = Float.parseFloat(token[1]);
                int energy = Integer.parseInt(token[2]);
                String msg = token[3];
                return new RobotStatutsImpl(temp, energy, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
