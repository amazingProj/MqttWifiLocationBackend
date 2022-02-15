package Controller;

import Model.Coordinates;

public class AccessPoint {
    int Rssi;
    String Ssid;
    String Bssid;
    int frequency;
    private Coordinates coordinates;
    private double distance;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getBssid() {
        return Bssid;
    }

    public void setBssid(String bssid) {
        Bssid = bssid;
    }

    public String getSsid() {
        return Ssid;
    }

    public void setSsid(String ssid) {
        Ssid = ssid;
    }

    public int getRssi() {
        return Rssi;
    }

    public void setRssi(int rssi) {
        this.Rssi = rssi;
    }


    @Override
    public String toString() {
        return "AccessPoint{" +
                "rssi=" + Rssi +
                ", Ssid='" + Ssid + '\'' +
                ", Bssid='" + Bssid + '\'' +
                ", frequency=" + frequency +
                '}';
    }
}
