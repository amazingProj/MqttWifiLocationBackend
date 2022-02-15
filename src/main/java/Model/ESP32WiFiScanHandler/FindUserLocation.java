package Model.ESP32WiFiScanHandler;

import Algorithms.CirclesIntersection;
import Algorithms.DistanceCalculator;
import Algorithms.NonLinearLeastSquaresSolver;
import Algorithms.TrilaterationFunction;
import WifiScanClasses.AccessPoint;
import WifiScanClasses.AccessPointSentByEsp32;
import WifiScanClasses.PayloadInformation;
import WifiScanClasses.PayloadInformationSentByEsp32;
import Geometries.Circle;
import Primitives.Coordinates;
import Model.ValidAccessPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FindUserLocation {
    GsonBuilder builder;
    Gson gson;
    NumberFormat nf;

    public FindUserLocation() {
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
    }

    public JsonObject FindEsp32UserLocation(JsonObject obj) {
        if (obj == null) return null;
        /*
        if(information.getAccessPointsLength() == 2){
            List<Coordinates> coordinates =
                    new CirclesIntersection().FindTwoCirclesIntersections2D(new Circle(new Coordinates(target[0][0], target[0][1]),
                            distancesPrimitive[0]),new Circle(new Coordinates(target[1][0], target[1][1]),
                            distancesPrimitive[1]));
            if (coordinates == null){
                return null;
            }
            double newX = (coordinates.get(0).getX() + coordinates.get(1).getX()) / 2;
            double newY = (coordinates.get(0).getY() + coordinates.get(1).getY()) / 2;
            result.addProperty("x", nf.format(newX));
            result.addProperty("y", nf.format(newY));

            return result;
        }
         */

        DistanceCalculator calc = new DistanceCalculator();

        JsonObject result = new JsonObject();

        PayloadInformationSentByEsp32 information = new PayloadInformationSentByEsp32();

        ValidAccessPoint valid = new ValidAccessPoint();

        Iterator<String> keys = obj.keySet().iterator();

        Coordinates coordinates;

        double distance;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.matches("^[0-9]+")){
                AccessPointSentByEsp32 accessPoint = gson.fromJson(obj.get(key), AccessPointSentByEsp32.class);
                double d = calc.CalculateDistanceByRssi(accessPoint.getRssi());
                System.out.printf("%s \t %.3f\n", accessPoint.getBssid(), d);
                System.out.println(accessPoint.getBssid().toLowerCase());
                coordinates = (Coordinates) valid.obj.get(accessPoint.getBssid().toLowerCase());
                if (coordinates != null) {
                    accessPoint.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY(), coordinates.getZ()));
                    distance = calc.CalculateDistanceByRssi(accessPoint.getRssi());
                    System.out.printf("%s  %.4f   rssi is %distance \n", accessPoint.getBssid(), distance, accessPoint.getRssi());
                    accessPoint.setDistance(distance);
                    information.addAccessPoint(accessPoint);
                }
            }
           else if (key.equals("MacAddress")) {
                information.setMacAddress(obj.get(key).getAsString());
            } else if (key.equals("isAlarmedOn")) {
                information.setAlarmed(obj.get(key).getAsBoolean());
            } else if (key.equals("NumberOfAccessPoints")) {
                information.setNumberOfAccessPoints(obj.get(key).getAsInt());
            }
        }

        int numberOfValidAccessPoint = information.getAccessPointsLength();

        if (numberOfValidAccessPoint < 2) {
            return null;
        }

        int size = 3;

        double[] distancesPrimitive = new double[numberOfValidAccessPoint];
        List<AccessPointSentByEsp32> accessPoints = information.getAccessPoints();
        AccessPointSentByEsp32 accessPoint;
        double[][] target = new double[numberOfValidAccessPoint][size];

        for (int i = 0; i < target.length; ++i) {
            accessPoint = accessPoints.get(i);
            distancesPrimitive[i] = accessPoint.getDistance();

            coordinates = accessPoint.getCoordinates();
            target[i][0] = coordinates.getX();
            target[i][1] = coordinates.getY();
            if (size == 3) {
                target[i][2] = coordinates.getZ();
            }
        }

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(target, distancesPrimitive), new LevenbergMarquardtOptimizer());
        solver.setMAX_NUMBER_OF_ITERATIONS(2000);
        solver.setThreads(1);
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        double[] centroid = optimum.getPoint().toArray();


        for (int i = 0; i < centroid.length; ++i) {
            System.out.printf("%.1f\n", centroid[i]);
        }

        result.addProperty("x", nf.format(centroid[0]));
        result.addProperty("y", nf.format(centroid[1]));

        result.addProperty("ID", information.getMacAddress());
        result.addProperty("FloorLevel", "4");
        result.addProperty("BATTERY", information.getBattery() + "%");
        return result;
    }
}
