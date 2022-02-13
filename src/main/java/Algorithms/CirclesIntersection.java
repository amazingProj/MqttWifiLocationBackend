package Algorithms;

import Model.Circle;
import Model.Coordinates;

import java.util.Vector;
import Utils.DoubleUtils;

public class CirclesIntersection {

    public boolean IsCirclesIntersect2D(Circle c1, Circle c2){
        double x1 = c1.getMiddlePoint().getX(), x2 = c2.getMiddlePoint().getX();
        double y1 = c1.getMiddlePoint().getY(), y2 = c2.getMiddlePoint().getY();
        double r1 = c1.getRadius(), r2 = c2.getRadius();
        double distSq = (x1 - x2) * (x1 - x2) +
                (y1 - y2) * (y1 - y2);
        DoubleUtils utils = new DoubleUtils();
        double radSumSq = (r1 + r2) * (r1 + r2);
        if (utils.DoublesEquals(distSq, radSumSq)){
            return true;
        }
        return false;
    }

    public Vector<Coordinates> FindTwoCirclesIntersections2D(Circle c1, Circle c2){
        Vector<Coordinates> result = new Vector<>();

        return result;
    }
}
