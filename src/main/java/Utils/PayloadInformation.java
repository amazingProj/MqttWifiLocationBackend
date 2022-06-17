package Utils;

import Utils.AccessPoint;

import java.util.ArrayList;
import java.util.List;

public class PayloadInformation {

    /******************************** Properties *******************************************/
    Integer battery;
    String specialId;
    int numberOfAccessPoint;
    boolean isAlarmed;
    List<AccessPoint> accessPoints = new ArrayList<>();

    /********************************* Getter and Setters **********************************/


    public List<AccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public int getAccessPointsLength(){
        return accessPoints.size();
    }

    public void addAccessPoint(AccessPoint accessPoint){
        if (accessPoint == null){
            return;
        }
        accessPoints.add(accessPoint);
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public void setAccessPoints(List<AccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public boolean isAlarmed() {
        return isAlarmed;
    }

    public void setAlarmed(boolean alarmed) {
        isAlarmed = alarmed;
    }

    public int getNumberOfAccessPoint() {
        return numberOfAccessPoint;
    }

    public void setNumberOfAccessPoint(int numberOfAccessPoint) {
        this.numberOfAccessPoint = numberOfAccessPoint;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    @Override
    public String toString() {
        return "PayloadInformation{" +
                "specialId=" + specialId +
                ", numberOfAccessPoint=" + numberOfAccessPoint +
                ", isAlarmed=" + isAlarmed +
                ", accessPoints=" + accessPoints +
                '}';
    }
}
