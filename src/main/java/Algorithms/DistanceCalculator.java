package Algorithms;

public class DistanceCalculator {
    // the rssi from AP which is given when d = 1 meter
    private final int measuredPower = -35;
    // low strength is 2 and high is 4, medium is 3
    private final int environmentalFactor = 4;

    public int getEnvironmentalFactor() {
        return environmentalFactor;
    }

    public int getMeasuredPower() {
        return measuredPower;
    }

    public double CalculateDistanceByRssi(int rssi){
        if (rssi == -127){
            return Double.MAX_VALUE;
        }
        if (rssi < -90){
            rssi += 15;
        }
        if (rssi < -80 && rssi > -90){
            rssi += 5;
        }
        double fraction = (double)(measuredPower - rssi) / (environmentalFactor * 10.0);
        return Math.pow(10, fraction);
    }
}