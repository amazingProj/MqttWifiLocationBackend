package ESP32WiFiScanHandler;

import Algorithms.DistanceCalculator;
import Algorithms.NonLinearLeastSquaresSolver;
import Algorithms.TrilaterationFunction;
import WifiScanClasses.AccessPointSentByEsp32;
import WifiScanClasses.PayloadInformationSentByEsp32;
import Primitives.AccessPointLocation;
import Model.ValidAccessPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.NumberFormat;
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

        DistanceCalculator calc = new DistanceCalculator();

        JsonObject result = new JsonObject();

        PayloadInformationSentByEsp32 information = new PayloadInformationSentByEsp32();

        ValidAccessPoint valid = new ValidAccessPoint();

        Iterator<String> keys = obj.keySet().iterator();

        AccessPointLocation coordinates;
        boolean firstTime = true;
        while (keys.hasNext()) {
            String key = keys.next();

            if (key.matches("^[0-9]+")){
                AccessPointSentByEsp32 accessPoint = gson.fromJson(obj.get(key), AccessPointSentByEsp32.class);
                double distance = calc.CalculateDistanceByRssi(accessPoint.getRssi());
                //System.out.printf("%s \t %.3f \t %d\n", accessPoint.getBssid(), distance, accessPoint.getRssi());
                //System.out.println(accessPoint.getBssid().toLowerCase());
                coordinates = (AccessPointLocation) valid.obj.get(accessPoint.getBssid().toLowerCase());
                if (coordinates != null) {
                    accessPoint.setAccessPointLocation(new AccessPointLocation(coordinates));
                    accessPoint.setFloor(coordinates.getFloorLevel());
                    accessPoint.setRoom(coordinates.getRoom());
                    if (firstTime){
                        result.addProperty("FloorLevel", coordinates.getFloorLevel());
                        firstTime = false;
                    }
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

        if (numberOfValidAccessPoint  == 0) {
            return null;
        }

        int size = 3;

        double[] distancesPrimitive = new double[numberOfValidAccessPoint];
        List<AccessPointSentByEsp32> accessPoints = information.getAccessPoints();
        AccessPointSentByEsp32 accessPoint;
        double[][] target = new double[numberOfValidAccessPoint][size];

        for (int i = 0; i < target.length; ++i) {
            accessPoint = accessPoints.get(i);
            System.out.printf("%s \t %.3f   %d     %d  \t %d\n", accessPoint.getBssid(), accessPoint.getDistance(), accessPoint.getRssi(),
                    accessPoint.getFloor(), accessPoint.getRoom());
            distancesPrimitive[i] = accessPoint.getDistance();

            coordinates = accessPoint.getCoordinates();
            target[i][0] = coordinates.getX();
            target[i][1] = coordinates.getY();
            if (size == 3) {
                target[i][2] = coordinates.getZ();
            }
        }

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(target, distancesPrimitive), new LevenbergMarquardtOptimizer());
        solver.setMAX_NUMBER_OF_ITERATIONS(9000);
        solver.setThreads(1);
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        double[] centroid = optimum.getPoint().toArray();

        result.addProperty("x", nf.format(centroid[0]));
        result.addProperty("y", nf.format(centroid[1]));

        result.addProperty("ID", information.getMacAddress());
        result.addProperty("BATTERY", information.getBattery() + "%");
        return result;
    }
}
