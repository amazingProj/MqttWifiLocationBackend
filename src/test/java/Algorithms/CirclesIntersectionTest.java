package Algorithms;

import Utils.Geometries.Circle;
import Utils.Primitives.AccessPointLocation;
import org.junit.jupiter.api.Test;

import java.util.List;

class CirclesIntersectionTest {
    @Test
    public void FindTwoCirclesIntersections2DTest(){
        Circle c1 = new Circle(new AccessPointLocation(-0.5,0,0), 0.6);
        Circle c2 = new Circle(new AccessPointLocation(0.5,0,0), 0.6);
        List<AccessPointLocation> coordinates = new CirclesIntersection().FindTwoCirclesIntersections2D(c1, c2);
        System.out.printf("%s  %s\n", coordinates.get(0).getX(), coordinates.get(0).getY());
        System.out.printf("%s  %s\n", coordinates.get(1).getX(), coordinates.get(1).getY());
    }
}