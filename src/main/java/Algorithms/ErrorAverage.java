package Algorithms;

import Model.ValidAccessPoint;
import Utils.AccessPointLocation;
import Utils.AccessPointSentByEsp32;

import java.util.HashMap;
import java.util.List;

public class ErrorAverage {
    int counter;
    double x, y, z;
    private HashMap<String, Integer> realRssi;
    public HashMap<String, Integer> avgError;

    public ErrorAverage(double _x, double _y, double _z)
    {
        counter = 0;
        x = _x;
        y = _y;
        z = _z;
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        int rssiSignal = 0;
        double distance;
        double envFactor;
        int a;
        double xi, yi, zi;
        realRssi = new HashMap<>();
        ValidAccessPoint validAccessPoint = new ValidAccessPoint();
        HashMap<String, AccessPointLocation> map = validAccessPoint.obj;
        for (String key : map.keySet())
        {
            AccessPointLocation accessPointLocation = map.get(key);
            xi = x - accessPointLocation.getX();
            yi = y - accessPointLocation.getY();
            zi = z - accessPointLocation.getZ();
            distance = Math.sqrt(xi * xi +  yi * yi + zi * zi);
            envFactor = distanceCalculator.getEnvironmentalFactor();
            a = distanceCalculator.getMeasuredPower();
            int logarithm = 10;
            rssiSignal = (int) (a - logarithm * envFactor * Math.log(distance));
            realRssi.put(key, rssiSignal);
        }
    }

    public void update(List<AccessPointSentByEsp32> accessPointSentByEsp32List)
    {
        if (accessPointSentByEsp32List == null) return;
        double temp;
        String bssid;
        for (AccessPointSentByEsp32 accessPointSentByEsp32 : accessPointSentByEsp32List)
        {
            bssid = accessPointSentByEsp32.getBssid();
            temp = accessPointSentByEsp32.getRssi() - realRssi.get(bssid);
            ++counter;
            avgError.put(bssid, (int) ((avgError.get(bssid) + temp) / counter));
        }
    }

}
