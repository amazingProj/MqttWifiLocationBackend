package Controller.Android;

import java.util.ArrayList;
import java.util.List;

public class PayloadInformation {
    int specialId;
    int numberOfAccessPoint;
    boolean isAlarmed;
    List<AccessPoint> accessPoints = new ArrayList<>();

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

    public int getSpecialId() {
        return specialId;
    }

    public void setSpecialId(int specialId) {
        this.specialId = specialId;
    }

    @Override
    public String toString() {
        // String accessPoint;
        //for (AccessPoint accessPoint:
        //     accessPoints) {

        //}
        return "PayloadInformation{" +
                "specialId=" + specialId +
                ", numberOfAccessPoint=" + numberOfAccessPoint +
                ", isAlarmed=" + isAlarmed +
                ", accessPoints=" + accessPoints +
                '}';
    }
}
