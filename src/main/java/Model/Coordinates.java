package Model;

public class Coordinates {
    double x;
    double y;
    double z;

    public Coordinates(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public Coordinates(double _x, double _y){
        x = _x;
        y = _y;
        z = 0;
    }

    public Coordinates() {
        x = 0;
        y = 0;
        z = 0;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
