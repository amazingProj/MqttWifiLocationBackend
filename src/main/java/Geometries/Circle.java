package Geometries;

import Primitives.Coordinates;

public class Circle {
    private double radius;
    private Coordinates middlePoint;

    public Circle(Coordinates middlePoint, double radius) {
        this.radius = radius;
        this.middlePoint = middlePoint;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Coordinates getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(Coordinates middlePoint) {
        this.middlePoint = middlePoint;
    }
}
