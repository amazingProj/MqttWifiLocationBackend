package Algorithms;

public class DistanceCalculator {
    private final double c = 27.55;
    // the rssi from AP which is given when d = 1 meter
    private final int measuredPower = -35;
    // low strength is 2 and high is 4, medium is 3
    private final int environmentalFactor = 4;

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
