package AndroidDataHandler;

import Algorithms.DistanceCalculator;
import Algorithms.NonLinearLeastSquaresSolver;
import Algorithms.TrilaterationFunction;
import WifiScanClasses.AccessPoint;
import WifiScanClasses.PayloadInformation;
import Primitives.AccessPointLocation;
import Model.ValidAccessPoint;
import com.google.gson.*;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

public class FindUserLocation {
    GsonBuilder builder;
    Gson gson;
    NumberFormat nf;

    public FindUserLocation(){
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();
        nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
    }
    class route{
        private String accessPoint;
        private Double distances;
        private AccessPointLocation coordinates;

        public route(String _accessPoint, Double _distances, AccessPointLocation _accessPointLocation){
            accessPoint = _accessPoint;
            distances = _distances;
            coordinates = _accessPointLocation;
        }

        public route(String _accessPoint, Double _distances){
            accessPoint = _accessPoint;
            distances = _distances;
        }
    }

    public JsonObject FindAndroidUserLocation(JsonObject obj){
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

            if (key.startsWith("AccessPoint")){
                AccessPoint accessPoint = gson.fromJson(obj.get(key).getAsString(), AccessPoint.class);
                coordinates = (AccessPointLocation) valid.obj.get(accessPoint.getBssid());
                if (coordinates != null){
                   accessPoint.setCoordinates(new AccessPointLocation(coordinates.getX(), coordinates.getY(), coordinates.getZ()));
                   distance = calc.CalculateDistanceByRssi(accessPoint.getRssi());
                   System.out.printf("%s  %.4f   rssi is %distance \n",accessPoint.getBssid(), distance, accessPoint.getRssi());
                   accessPoint.setDistance(distance);
                   information.addAccessPoint(accessPoint);
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
        }

        int numberOfValidAccessPoint = information.getAccessPointsLength();

        if (numberOfValidAccessPoint < 2){
            return null;
        }

        int size = 3;

        double[] distancesPrimitive = new double[numberOfValidAccessPoint];
        List<AccessPoint> accessPoints = information.getAccessPoints();
        AccessPoint accessPoint;
        double[][] target = new double[numberOfValidAccessPoint][size];

        for (int i = 0; i < target.length; ++i) {
            accessPoint = accessPoints.get(i);
            distancesPrimitive[i] = accessPoint.getDistance();

            coordinates = accessPoint.getCoordinates();
            target[i][0] = coordinates.getX();
            target[i][1] = coordinates.getY();
            if (size == 3){
                target[i][2] = coordinates.getZ();
            }
        }

        NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(target, distancesPrimitive), new LevenbergMarquardtOptimizer());
        solver.setMAX_NUMBER_OF_ITERATIONS(2000);
        solver.setThreads(1);
        LeastSquaresOptimizer.Optimum optimum = solver.solve();

        double[] centroid = optimum.getPoint().toArray();


        for (int i = 0; i < centroid.length; ++i){
            System.out.printf("%.1f\n", centroid[i]);
        }

        result.addProperty("x", nf.format(centroid[0]));
        result.addProperty("y", nf.format(centroid[1]));

        result.addProperty("ID", information.getSpecialId());
        result.addProperty("FloorLevel", "4");
        result.addProperty("BATTERY", "100%");
        return result;
    }
}
