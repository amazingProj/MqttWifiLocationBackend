package Model.ESP32WiFiScanHandler;

import Algorithms.DistanceCalculator;
import Algorithms.NonLinearLeastSquaresSolver;
import Algorithms.TrilaterationFunction;
import Controller.AccessPoint;
import Controller.AccessPointSentByEsp32;
import Controller.PayloadInformation;
import Controller.PayloadInformationSentByEsp32;
import Model.Coordinates;
import Model.ValidAccessPoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FindUserLocation {
    public FindUserLocation(){

    }

    public JsonObject FindEsp32UserLocation(JsonObject obj) {
        if (obj == null){
            return null;
        }
        List<Double> distances = new ArrayList();
        DistanceCalculator calc = new DistanceCalculator();
        JsonObject result = new JsonObject();
        PayloadInformationSentByEsp32 information = new PayloadInformationSentByEsp32();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        ValidAccessPoint valid = new ValidAccessPoint();
        List<Coordinates> myList = new ArrayList<Coordinates>();
        Coordinates nested = new Coordinates();
        Iterator<String> keys = obj.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.matches("^[0-9]+")){
                AccessPointSentByEsp32 accessPoint = gson.fromJson(obj.get(key).getAsString(), AccessPointSentByEsp32.class);
                double d = calc.CalculateDistanceByRssi(accessPoint.getRssi());
                System.out.printf("%s \t %.3f\n", accessPoint.getBssid(), d);
                Coordinates coordinates = (Coordinates) valid.obj.get(accessPoint.getBssid());
                if (accessPoint.getSsid().equals("JCT-Lev-WiFi") && coordinates != null){
                    // the model contains this
                    information.addAccessPoint(accessPoint);
                    nested.setX(coordinates.getX());
                    nested.setY(coordinates.getY());
                    nested.setZ(coordinates.getZ());
                    myList.add(nested);
                }
            }
            else if (key.equals("MacAddress")) {
                information.setMacAddress(obj.get(key).getAsString());
            }
            else if (key.equals("isAlarmedOn")){
                information.setAlarmed(obj.get(key).getAsBoolean());
            }
            else if (key.equals("NumberOfAccessPoints")){
                information.setNumberOfAccessPoints(obj.get(key).getAsInt());
            }
            //System.out.println("Key :" + key + "  Value :" + obj.get(key));
        }

        if (information.getAccessPointsLength() <= 2){
            return null;
        }

        double d;
        for (AccessPointSentByEsp32 accessPoint:
                information.getAccessPoints()){
            d = calc.CalculateDistanceByRssi(accessPoint.getRssi());
            if (d > 0){
                distances.add(d);
            }
            System.out.printf("%s  %.4f   rssi is %d \n",accessPoint.getBssid(), d, accessPoint.getRssi());
        }

        int size = 2;
        Coordinates cor;
        double[][] target = new double[myList.size()][size];
        for (int i = 0; i < target.length; ++i) {
            cor = myList.get(i);
            target[i][0] = cor.getX();
            target[i][1] = cor.getY();
            if (size == 3){
                target[i][2] = cor.getZ();
            }
        }

        double[] distancesPrimitive = new double[distances.size()];
        for (int i = 0; i < distancesPrimitive.length; ++i) {
            distancesPrimitive[i] = distances.get(i);
        }

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(target, distancesPrimitive), new LevenbergMarquardtOptimizer());
        LeastSquaresOptimizer.Optimum optimum = solver.solve();


        double[] centroid = optimum.getPoint().toArray();


        for (int i = 0; i < centroid.length; ++i){
            System.out.printf("%.1f\n", centroid[i]);
        }

        result.addProperty("x", centroid[0]);
        result.addProperty("y", centroid[1]);

        result.addProperty("ID", information.getMacAddress());
        result.addProperty("FloorLevel", "4");
        result.addProperty("BATTERY", information.getBattery());
        return result;
    }
}
