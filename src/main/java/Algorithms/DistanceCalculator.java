package Algorithms;

public class DistanceCalculator {
    private final double c = 27.55;

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
}
