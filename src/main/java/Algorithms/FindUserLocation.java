package Algorithms;

import Model.RssiError;
import Model.ValidAccessPoint;
import Utils.AccessPointLocation;
import Utils.AccessPointSentByEsp32;
import Utils.PayloadInformationSentByEsp32;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * class represents find user location class
 */
public class FindUserLocation {
    GsonBuilder builder;
    Gson gson;
    NumberFormat nf;
    int dimension = 3;
    RssiError rssiError;

    /**
     * construct a find user location class
     */
    public FindUserLocation() {
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        rssiError = new RssiError();
    }

    /**
     * finds the location of a user in the building
     *
     * @param obj - a json format message contains list of rssi
     * @return user location in x,y,z
     */
    public JsonObject FindEsp32UserLocation(JsonObject obj) {

        if (obj == null) return null;

        int floorLevel = 0;

        DistanceCalculator calc = new DistanceCalculator();
        JsonObject result = new JsonObject();
        PayloadInformationSentByEsp32 information = new PayloadInformationSentByEsp32();
        ValidAccessPoint valid = new ValidAccessPoint();
        Iterator<String> keys = obj.keySet().iterator();
        AccessPointLocation coordinates;

        boolean firstTime = true;
        int tempFloorLevel;
        int room;
        int closestRoom = 0;
        String temp;

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.matches("^[0-9]+")) {
                AccessPointSentByEsp32 accessPoint = gson.fromJson(obj.get(key), AccessPointSentByEsp32.class);
                temp = accessPoint.getBssid();
                coordinates = temp == null ? null : (AccessPointLocation) valid.obj.get(temp.toLowerCase());
                if (coordinates != null) {
                    accessPoint.setAccessPointLocation(new AccessPointLocation(coordinates));
                    tempFloorLevel = coordinates.getFloorLevel();
                    accessPoint.setFloor(tempFloorLevel);
                    room = coordinates.getRoom();
                    accessPoint.setRoom(room);
                    information.addAccessPoint(accessPoint);
                }
            } else if (key.equals("MacAddress")) {

                information.setMacAddress(obj.get(key).getAsString());

            } else if (key.equals("isAlarmedOn")) {

                information.setAlarmed(obj.get(key).getAsBoolean());

            } else if (key.equals("NumberOfAccessPoints")) {
                information.setNumberOfAccessPoints(obj.get(key).getAsInt());
            }
            else if (keys.equals("BATTERY")) {
                information.setBattery(obj.get(key).getAsInt());
            }
        }

        int numberOfValidAccessPoint = information.getAccessPointsLength();
        if (numberOfValidAccessPoint == 0) return null;

        List<AccessPointSentByEsp32> accessPoints = information.getAccessPoints();
        AccessPointSentByEsp32 accessPoint;

        accessPoints.sort(Comparator.comparingDouble(AccessPointSentByEsp32::getRssi).reversed());
        double distance;
        for (int i = 0; i < numberOfValidAccessPoint; ++i) {
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

        if (numberOfValidAccessPoint == 1) {
            accessPoint = information.getAccessPoints().get(0);
            result.addProperty("x", accessPoint.getCoordinates().getX() - accessPoint.getDistance());
            result.addProperty("y", accessPoint.getCoordinates().getY());
            result.addProperty("z", accessPoint.getCoordinates().getZ() - 0.5);
            result.addProperty("ID", information.getMacAddress());
            result.addProperty("BATTERY", information.getBattery() + "%");
            return result;
        }


        double[] distancesPrimitive = new double[numberOfValidAccessPoint];


        double[][] target = new double[numberOfValidAccessPoint][dimension];
        double distanceTemp;
        for (int i = 0; i < target.length; ++i) {
            accessPoint = accessPoints.get(i);
            distanceTemp = accessPoint.getDistance();
            System.out.printf("%s \t %.3f   %d     %d  \t %d\n", accessPoint.getBssid(), distanceTemp, accessPoint.getRssi(),
                    accessPoint.getFloor(), accessPoint.getRoom());
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
        result.addProperty("x", nf.format(centroid[0]));
        result.addProperty("y", nf.format(centroid[1]));
        result.addProperty("z", nf.format(z));
        result.addProperty("ID", information.getMacAddress());
        result.addProperty("BATTERY", information.getBattery() + "%");
        return result;
    }
}
