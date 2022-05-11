package Algorithms;

public class DistanceCalculator {
    // the rssi from AP which is given when d = 1 meter
    private final int measuredPower = -35;
    // low strength is 2 and high is 4, medium is 3
    private final double environmentalFactor = 5;

    public double getEnvironmentalFactor() {
        return environmentalFactor;
    }

    public int getMeasuredPower() {
        return measuredPower;
    }

    public double CalculateDistanceByRssi(int rssi){
        if (rssi == -127){
            return Double.MAX_VALUE;
        }


        double fraction = (double)(measuredPower - rssi) / (environmentalFactor * 10.0);
        return Math.pow(10, fraction);
    }
}