package Algorithms;

import Model.RssiError;
import Utils.AccessPointLocation;
import Utils.AccessPointSentByEsp32;
import Utils.PayloadInformationSentByEsp32;
import Model.ValidAccessPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
     * @param obj - a json format message contains list of rssi
     * @return user location in x,y,z
     */
    public JsonObject FindEsp32UserLocation(JsonObject obj) {
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
        return result1;
        /*
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

            if (key.matches("^[0-9]+")){

                AccessPointSentByEsp32 accessPoint = gson.fromJson(obj.get(key), AccessPointSentByEsp32.class);

                temp = accessPoint.getBssid();
                coordinates = temp == null? null : (AccessPointLocation) valid.obj.get(temp.toLowerCase());

                if (coordinates != null) {

                    accessPoint.setAccessPointLocation(new AccessPointLocation(coordinates));
                    tempFloorLevel = coordinates.getFloorLevel();
                    accessPoint.setFloor(tempFloorLevel);
                    room = coordinates.getRoom();
                    accessPoint.setRoom(room);
                    int mesuredPower = coordinates.getMeasuredPower();

                    if (firstTime){
                        closestRoom = room;
                        floorLevel = tempFloorLevel;
                        result.addProperty("FloorLevel", floorLevel);
                        firstTime = false;
                    }
                    int actualRssi = accessPoint.getRssi();
                    int error = rssiError.getError(closestRoom, temp.toLowerCase());
                    if (error != -1)
                    {
                        actualRssi += error;
                    }
                    double distance = calc.CalculateDistanceByRssi(actualRssi);
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
        if (numberOfValidAccessPoint  == 0) return null;

        AccessPointSentByEsp32 accessPoint;

        double[] distancesPrimitive = new double[numberOfValidAccessPoint];
        List<AccessPointSentByEsp32> accessPoints = information.getAccessPoints();
        ErrorAverage errorAverage = new ErrorAverage(4.5, 3, 4);
        errorAverage.update(accessPoints);

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
        return result;*/
    }
}
