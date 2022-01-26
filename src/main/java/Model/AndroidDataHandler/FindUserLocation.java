package Model.AndroidDataHandler;

import Algorithms.DistanceCalculator;
import Controller.Android.AccessPoint;
import Controller.Android.PayloadInformation;
import Model.Coordinates;
import Model.ValidAccessPoint;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.lemmingapex.trilateration.NonLinearLeastSquaresSolver;
import com.lemmingapex.trilateration.TrilaterationFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FindUserLocation {
    public FindUserLocation(){

    }

    public String removeFirstandLast(String str)
    {

        // Removing first and last character
        // of a string using substring() method
        str = str.substring(1, str.length() - 1);

        // Return the modified string
        return str;
    }

    public JsonObject FindAndroidUserLocation(JsonObject obj){
        if (obj == null){
            return null;
        }
        List<Double> distances = new ArrayList();

        DistanceCalculator calc = new DistanceCalculator();

        JsonObject result = new JsonObject();

        PayloadInformation information = new PayloadInformation();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        ValidAccessPoint valid = new ValidAccessPoint();

        List<Coordinates> myList = new ArrayList<Coordinates>();

        Coordinates nested = new Coordinates();

        Iterator<String> keys = obj.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.startsWith("AccessPoint")){
                //JsonReader reader = new JsonReader(new StringReader(obj.get(key).toString()));
                //reader.setLenient(true);
               //AccessPoint accessPoint = gson.fromJson(reader, AccessPoint.class);
                JsonElement el = obj.get(key);
                AccessPoint accessPoint = gson.fromJson(obj.get(key).getAsString(), AccessPoint.class);
                Coordinates coordinates = (Coordinates) valid.obj.get(accessPoint.getBssid());
                if (accessPoint.getSsid().equals("JCT-Lev-WiFi") && coordinates != null){
                    // the model contains this
                    information.addAccessPoint(accessPoint);
                    nested.setX(coordinates.getX());
                    nested.setY(coordinates.getY());
                    nested.setZ(coordinates.getZ());
                    myList.add(nested);
                    double d = 0;
                    //d = calc.Find2DDistance(2.7,calc.CalculateNormalDistanceByFrequencyAndRssi(accssesPoint.getRssi(), accssesPoint.getFrequency()));
                    d = calc.CalculateNormalDistanceByFrequencyAndRssi(accessPoint.getRssi(), accessPoint.getFrequency());
                    if (d > 0){
                        distances.add(d);
                    }
                    System.out.printf("%s  %.4f   rssi is %d \n",accessPoint.getBssid(), d, accessPoint.getRssi());
                }
            }
            else if (key.equals("specialIdNumber")) {
                information.setSpecialId(obj.get(key).getAsInt());
            }
            else if (key.equals("isAlarmedOn")){
                information.setAlarmed(obj.get(key).getAsBoolean());
            }
            else if (key.equals("NumberOfAccessPoints")){
                information.setNumberOfAccessPoint(obj.get(key).getAsInt());
            }
            //System.out.println("Key :" + key + "  Value :" + obj.get(key));
        }
        if (information.getAccessPointsLength() <= 2){
            return null;
        }



        for (AccessPoint accssesPoint:
                information.getAccessPoints()) {
                double d = 0;
                //d = calc.Find2DDistance(2.7,calc.CalculateNormalDistanceByFrequencyAndRssi(accssesPoint.getRssi(), accssesPoint.getFrequency()));
                d = calc.CalculateNormalDistanceByFrequencyAndRssi(accssesPoint.getRssi(), accssesPoint.getFrequency());
                if (d > 0){
                    distances.add(d);
                }
                System.out.printf("%s  %.4f   rssi is %d \n",accssesPoint.getBssid(), d, accssesPoint.getRssi());

        }

        int size = 3;
        Coordinates cor;
        double[][] target = new double[myList.size()][size];
        for (int i = 0; i < target.length; ++i) {
            cor = myList.get(i);
            target[i][0] = cor.getX();
            target[i][1] = cor.getY();
            target[i][2] = cor.getZ();
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

        return null;
    }
}
