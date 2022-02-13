package Controller;

public class AccessPointSentByEsp32 {
     private String EspMacAddress;
    private String Bssid;
    private int Rssi;
    private String Ssid;

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
