package Utils.Primitives;

import Utils.Primitives.AccessPointSentByEsp32;

import java.util.ArrayList;
import java.util.List;

public class PayloadInformationSentByEsp32 {
    //{"Battery":100,"NumberOfAccessPoints":11,"MacAddress":"94:B9:7E:FA:92:14","WifiSsid":"benny","1":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"34:49:5B:16:9D:E4","Rssi":-52,"Ssid":"benny"},"2":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"A0:8C:FD:01:2A:20","Rssi":-78,"Ssid":"DIRECT-E2-HP DeskJet 3830 series"},"3":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"84:26:15:EA:4B:D3","Rssi":-79,"Ssid":"lior"},"4":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"00:B8:C2:0E:3A:35","Rssi":-80,"Ssid":"arad_2.4"},"5":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"34:49:5B:16:BF:E4","Rssi":-80,"Ssid":"Izikwifi"},"6":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"F0:B4:D2:19:11:E3","Rssi":-80,"Ssid":"DIR-615-11E2"},"7":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"68:FF:7B:6A:AB:9E","Rssi":-83,"Ssid":"aradgood"},"8":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"6E:FF:7B:6A:AB:9E","Rssi":-85,"Ssid":""},"9":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"6E:FF:7B:6A:9D:E6","Rssi":-89,"Ssid":""},"10":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"68:FF:7B:6A:9D:E6","Rssi":-91,"Ssid":"aradgood"},"11":{"EspMacAddress":"94:B9:7E:FA:92:14","Bssid":"34:49:5B:09:91:B4","Rssi":-92,"Ssid":"Fluer"}}
    private int Battery;
    private int NumberOfAccessPoints;
    private String MacAddress;
    private String WifiSsid;
    private List<AccessPointSentByEsp32> accessPoints = new ArrayList<>();
    boolean isAlarmed;

    public List<AccessPointSentByEsp32> getAccessPoints() {
        return accessPoints;
    }

    public int getAccessPointsLength(){
        return accessPoints.size();
    }

    public void addAccessPoint(AccessPointSentByEsp32 accessPoint){
        if (accessPoint == null){
            return;
        }
        accessPoints.add(accessPoint);
    }

    public boolean isAlarmed() {
        return isAlarmed;
    }

    public void setAlarmed(boolean alarmed) {
        isAlarmed = alarmed;
    }

    public void setAccessPoints(List<AccessPointSentByEsp32> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public String getMacAddress() {
        return MacAddress;
    }

    public void setMacAddress(String macAddress) {
        MacAddress = macAddress;
    }

    public String getWifiSsid() {
        return WifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        WifiSsid = wifiSsid;
    }

    public int getNumberOfAccessPoints() {
        return NumberOfAccessPoints;
    }

    public void setNumberOfAccessPoints(int numberOfAccessPoints) {
        NumberOfAccessPoints = numberOfAccessPoints;
    }

    public int getBattery() {
        return Battery;
    }

    public void setBattery(int battery) {
        Battery = battery;
    }
}
