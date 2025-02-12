package exo;

import java.io.PrintStream;

public interface Robot {

    public void initRun(Planet planet, String lander, Position landPos, String userData, RobotStatus initStatus, PrintStream out);

    public void crash();

    public void statusChanged(RobotStatus newStatus);

    public String getLanderName();
}