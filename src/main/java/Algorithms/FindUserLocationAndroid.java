package Algorithms;

import Model.RssiError;
import Utils.*;
import Model.ValidAccessPoint;
import com.google.gson.*;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.NumberFormat;
import java.util.*;

public class FindUserLocationAndroid {
    GsonBuilder builder;
    Gson gson;
    NumberFormat nf;
    RssiError rssiError;

    public FindUserLocationAndroid() {
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        rssiError = new RssiError();
    }

    public JsonObject FindAndroidUserLocation(JsonObject obj) {
        boolean firstTime = true;
        int tempFloorLevel;
        int room;
        int closestRoom = 0;
        String temp;
        int floorLevel;

        if (obj == null) return null;

        DistanceCalculator calc = new DistanceCalculator();

        JsonObject result = new JsonObject();

        PayloadInformation information = new PayloadInformation();

        ValidAccessPoint valid = new ValidAccessPoint();

        Iterator<String> keys = obj.keySet().iterator();

        AccessPointLocation coordinates;

        double distance;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.startsWith("AccessPoint")) {
                AccessPoint accessPoint = gson.fromJson(obj.get(key).getAsString(), AccessPoint.class);
                coordinates = (AccessPointLocation) valid.obj.get(accessPoint.getBssid());

                if (coordinates != null) {
                    accessPoint.setAccessPointLocation(new AccessPointLocation(coordinates.getX(), coordinates.getY(), coordinates.getZ()));
                    accessPoint.setFloor(coordinates.getFloorLevel());
                    accessPoint.setRoom(coordinates.getRoom());
                    information.addAccessPoint(accessPoint);
                }
            } else if (key.equals("specialIdNumber")) {
                information.setSpecialId(obj.get(key).getAsString());
            } else if (key.equals("isAlarmedOn")) {
                information.setAlarmed(obj.get(key).getAsBoolean());
            } else if (key.equals("NumberOfAccessPoints")) {
                information.setNumberOfAccessPoint(obj.get(key).getAsInt());
            }
            else if (keys.equals("BATTERY")) {
                information.setBattery(obj.get(key).getAsInt());
            }
        }

        int numberOfValidAccessPoint = information.getAccessPointsLength();
        if (numberOfValidAccessPoint == 0) return null;

        AccessPoint accessPoint;
        List<AccessPoint> accessPoints = information.getAccessPoints();
        accessPoints.sort(Comparator.comparingDouble(AccessPoint::getRssi).reversed());

        for (int i = 0; i < numberOfValidAccessPoint; ++i)
        {
            accessPoint = accessPoints.get(i);
            if (firstTime) {
                closestRoom = accessPoint.getRoom();
                floorLevel = accessPoint.getFloor();
                result.addProperty("FloorLevel", floorLevel);
                firstTime = false;
            }
            int actualRssi = accessPoint.getRssi();
            int error = rssiError.getError(closestRoom, accessPoint.getBssid().toLowerCase());
            if (error != -1) {
                actualRssi += error;
            }
            distance = calc.CalculateDistanceByRssi(actualRssi);
            accessPoint.setDistance(distance);
        }

        double[] distancesPrimitive = new double[numberOfValidAccessPoint];

        double[][] target = new double[numberOfValidAccessPoint][3];
        double distanceTemp;
        for (int i = 0; i < target.length; ++i) {
            accessPoint = accessPoints.get(i);
            distanceTemp = accessPoint.getDistance();
            System.out.printf("%s \t %.3f   %d     %d  \t %d %s\n", accessPoint.getBssid(), distanceTemp, accessPoint.getRssi(),
                    accessPoint.getFloor(), accessPoint.getRoom(), accessPoint);
            coordinates = accessPoint.getCoordinates();
            target[i][0] = coordinates.getX();
            target[i][1] = coordinates.getY();
            target[i][2] = coordinates.getZ();
            distancesPrimitive[i] = distanceTemp;
        }


        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(target, distancesPrimitive), new LevenbergMarquardtOptimizer());
        solver.setMAX_NUMBER_OF_ITERATIONS(1000);
        solver.setThreads(1);
        LeastSquaresOptimizer.Optimum optimum = solver.solve();
        double[] centroid = optimum.getPoint().toArray();
        result.addProperty("x", nf.format(centroid[0]));
        result.addProperty("y", nf.format(centroid[1]));
        result.addProperty("z", nf.format(centroid[2]));
        result.addProperty("ID", information.getSpecialId());
        result.addProperty("BATTERY", information.getBattery() + "%");
        return result;

    }
}
