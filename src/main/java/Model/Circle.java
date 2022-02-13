package Model;

public class Circle {
    private Integer radius;
    private Coordinates middlePoint;

    public Circle(Integer radius, Coordinates middlePoint) {
        this.radius = radius;
        this.middlePoint = middlePoint;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public Coordinates getMiddlePoint() {
        return middlePoint;
    }

    public void setMiddlePoint(Coordinates middlePoint) {
        this.middlePoint = middlePoint;
    }
}
