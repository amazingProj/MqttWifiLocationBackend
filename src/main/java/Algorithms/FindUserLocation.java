package Algorithms;

import Utils.Primitives.*;
import Model.ValidAccessPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class FindUserLocation {
    GsonBuilder builder;
    Gson gson;
    NumberFormat nf;
    int dimension = 3;

    public FindUserLocation() {
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public JsonObject FindEsp32UserLocation(JsonObject obj) {
        if (obj == null) return null;

        int floorLevel = 0;

        DistanceCalculator calc = new DistanceCalculator();
        JsonObject result = new JsonObject();
        PayloadInformationSentByEsp32 information = new PayloadInformationSentByEsp32();
        ValidAccessPoint valid = new ValidAccessPoint();
        Iterator<String> keys = obj.keySet().iterator();
        AccessPointLocation coordinates;
        MiddleRooms middleRooms = new MiddleRooms();

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
                    /*if (floorLevel != tempFloorLevel){
                        actualRssi += 9;
                    }
                    else if (room != closestRoom){
                        int wall = middleRooms.neighborhoodsLevel(closestRoom, room);
                        if (wall == 1){
                            actualRssi += 3;
                        }
                        else if (wall == 2){
                            actualRssi += 7;
                        }
                        else if (wall == 3){
                            actualRssi += 9;
                        }
                    }
                    calc.setMeasuredPower(mesuredPower);
                    double distance = calc.CalculateDistanceByRssi(actualRssi);
                    accessPoint.setDistance(distance);
                    information.addAccessPoint(accessPoint);*/
                    //calc.setMeasuredPower(mesuredPower);
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
        //double x = (centroid[0] + vec.getX()) / 2;
        //double y = (centroid[1] + vec.getY()) / 2;
        double z = centroid[2];
        result.addProperty("x", nf.format(centroid[0]));
        result.addProperty("y", nf.format(centroid[1]));
        result.addProperty("z", nf.format(z));
        result.addProperty("ID", information.getMacAddress());
        result.addProperty("BATTERY", information.getBattery() + "%");
        return result;
    }
}
