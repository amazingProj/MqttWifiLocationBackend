package Utils.Primitives;

import Utils.Primitives.AccessPointLocation;

public class AccessPointSentByEsp32 {
    //{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"34:49:5B:16:9D:E4","Rssi":-52,"Ssid":"benny"}
    private String EspMacAddress;
    private String Bssid;
    private int Rssi;
    private String Ssid;
    private AccessPointLocation coordinates;
    private double distance;
    private int floor;
    private int room;

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public AccessPointLocation getCoordinates() {
        return coordinates;
    }

    public void setAccessPointLocation(AccessPointLocation coordinates) {
        this.coordinates = coordinates;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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
        Rssi = rssi;
    }

    public String getBssid() {
        return Bssid;
    }

    public void setBssid(String bssid) {
        Bssid = bssid;
    }

    public String getEspMacAddress() {
        return EspMacAddress;
    }

    public void setEspMacAddress(String espMacAddress) {
        EspMacAddress = espMacAddress;
    }
}
