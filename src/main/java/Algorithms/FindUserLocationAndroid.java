package Algorithms;

import Algorithms.DistanceCalculator;
import Algorithms.NonLinearLeastSquaresSolver;
import Algorithms.TrilaterationFunction;
import Model.ClosestRoom;
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

    class route {
        private String accessPoint;
        private Double distances;
        private AccessPointLocation coordinates;

        public route(String _accessPoint, Double _distances, AccessPointLocation _accessPointLocation) {
            accessPoint = _accessPoint;
            distances = _distances;
            coordinates = _accessPointLocation;
        }

        public route(String _accessPoint, Double _distances) {
            accessPoint = _accessPoint;
            distances = _distances;
        }
    }

    public JsonObject FindAndroidUserLocation(JsonObject obj) {
        /*
        JsonObject result1 = new JsonObject();
        Random rand = new Random();
        int x = rand.nextInt(22);
        int y = rand.nextInt(18);
        int z = rand.nextInt(10);
        result1.addProperty("x", x);
        result1.addProperty("y", y);
        result1.addProperty("z", z);
        result1.addProperty("ID", "94:B9:7E:FA:92:14");
        result1.addProperty("BATTERY", 50 + "%");
        return result1;*/

        boolean firstTime = true;
        int tempFloorLevel;
        int room;
        int closestRoom = 0;
        String temp;
        int floorLevel = 0;

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
        double z = centroid[2];
        ClosestRoom closestRoom1 = new ClosestRoom();
        Coordinates hashMap = closestRoom1.coordinatesHashMap.get(closestRoom);
        if (hashMap != null)
        {
            double x = (hashMap.getX() + centroid[0]) / 2;
            result.addProperty("x", nf.format(x));
            double y = (centroid[1] + hashMap.getY()) / 2;
            result.addProperty("y", nf.format(y));
            double z1 = (z + hashMap.getZ()) / 2;
            result.addProperty("z", nf.format(z1));
        }
        else
        {
            result.addProperty("x", nf.format(centroid[0]));
            result.addProperty("y", nf.format(centroid[1]));
            result.addProperty("z", nf.format(z));
        }



        result.addProperty("ID", information.getSpecialId());
        //result.addProperty("BATTERY", information.getBattery() + "%");
        return result;

    }
}
