package Controller.Android;

public class AccessPoint {
    int Rssi;
    String Ssid;
    String Bssid;
    int frequency;

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
