package exo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ExoBooster implements Serializable{

    private static final long serialVersionUID = 3L;
    private String lander;		// Name des Landers
    private Position position;	// Zielposition zum Landen
    private String userData;	// frei belegbare Benutzerdaten
    private List<BoosterPart> partList;	// Teileliste

    public ExoBooster(String lander, Position position, String userData, String className, byte[] classData) {
        super();
        this.lander = lander;
        this.position = position;
        this.userData = userData;
        this.partList = new LinkedList<BoosterPart>();
        partList.add(new BoosterPart(className, classData));
    }

    public void addPart(String className, byte[] classData){
        partList.add(0, new BoosterPart(className, classData));
    }

    public String getLander() {
        return lander;
    }

    public Position getPosition() {
        return position;
    }

    public String getUserData() {
        return userData;
    }

    public int getPartCount(){
        return partList.size();
    }

    public String getClassName(int index) {
        if(index >=0 && index < partList.size()){
            return partList.get(index).getClassName();
        }
        return null;
    }

    public byte[] getClassData(int index) {
        if(index >=0 && index < partList.size()){
            return partList.get(index).getClassData();
        }
        return null;
    }

    class BoosterPart implements Serializable{

        private static final long serialVersionUID = 2L;
        private String className;
        private byte[] classData;
        public BoosterPart(String className, byte[] classData) {
            this.className = className;
            this.classData = classData;
        }
        public String getClassName() {
            return className;
        }
        public byte[] getClassData() {
            return classData;
        }
    }

}
