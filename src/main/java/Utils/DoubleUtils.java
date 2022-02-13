package Utils;

public class DoubleUtils {
    double epsilon = 0.000001d;

    public boolean DoublesEquals(double d1, double d2){
        if (d1 > d2){
            return (d1 - d2) < epsilon? true : false;
        }
        return (d2 - d1) < epsilon? true : false;
    }
}
