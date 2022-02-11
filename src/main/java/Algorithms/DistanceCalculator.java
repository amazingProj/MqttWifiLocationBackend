package Algorithms;

public class DistanceCalculator {
    private final double c = 27.55;
    // the rssi from AP which is given when d = 1 meter
    private final int measuredPower = -69;
    // low strength is 2 and high is 4, medium is 3
    private final int environmentalFactor = 2;

    public double CalculateNormalDistanceByFrequencyAndRssi(int rssi, int frequency){
        double numerator = (c - 2 * 10 * Math.log10(frequency)) + Math.abs(rssi);
        double dominator = 2 * 10;
        double fraction = numerator / dominator;
        return Math.pow(10, fraction);
    }

    public double Find2DDistance(double height, double distance){
        double result = 0;
        result = Math.sqrt(Math.abs(distance - height * height));
        return result;
    }

    public double CalculateDistanceByRssi(int rssi){
        if (rssi == -127){
            return Double.MAX_VALUE;
        }
        double fraction = (double)(measuredPower - rssi) / (environmentalFactor * 10.0);
        return Math.pow(10, fraction);
    }
}
