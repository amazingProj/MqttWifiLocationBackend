package Utils;

public class Circle {
    private double radius;
    private AccessPointLocation middlePoint;

    public Circle(AccessPointLocation middlePoint, double radius) {
        this.radius = radius;
        this.middlePoint = middlePoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public AccessPointLocation getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(AccessPointLocation middlePoint) {
        this.middlePoint = middlePoint;
    }
}
