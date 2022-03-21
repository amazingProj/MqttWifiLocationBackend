package Algorithms;

import Utils.Circle;
import Utils.AccessPointLocation;

import java.util.ArrayList;
import java.util.List;

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

    public List<AccessPointLocation> FindTwoCirclesIntersections2D(Circle c1, Circle c2) {
        if (c1 == null || c2 == null) return null;
        List<AccessPointLocation> result = null;
        double x1 = c1.getMiddlePoint().getX(), x2 = c2.getMiddlePoint().getX();
        double y1 = c1.getMiddlePoint().getY(), y2 = c2.getMiddlePoint().getY();
        double r1 = c1.getRadius() + 3, r2 = c2.getRadius();
        double d = Math.hypot(x2 - x1, y2 - y1);

        if (d <= r1 + r2 && d >= Math.abs(r2 - r1)) {

            double ex = (x2 - x1) / d;
            double ey = (y2 - y1) / d;

            double x = (r1 * r1 - r2 * r2 + d * d) / (2 * d);
            double y = Math.sqrt(r1 * r1 - x * x);

            double p1X = x1 + x * ex - y * ey;
            double p1Y = y1 + x * ey + y * ex;
            double p2X = x1 + x * ex + y * ey;
            double p2Y = y1 + x * ey - y * ex;
            result = new ArrayList<AccessPointLocation>();
            result.add(new AccessPointLocation(p1X, p1Y));
            result.add(new AccessPointLocation(p2X, p2Y));
        }
        return result;
    }
}
