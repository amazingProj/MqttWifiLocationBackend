package Model.AndroidDataHandler;

import Algorithms.DistanceCalculator;
import Controller.Android.AccessPoint;
import Controller.Android.PayloadInformation;
import Model.Coordinates;
import Model.ValidAccessPoint;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.Iterator;

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

        DistanceCalculator calc = new DistanceCalculator();

        JsonObject result = new JsonObject();

        PayloadInformation information = new PayloadInformation();
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();

        ValidAccessPoint valid = new ValidAccessPoint();

        Iterator<String> keys = obj.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();

            if (key.startsWith("AccessPoint")){
                //JsonReader reader = new JsonReader(new StringReader(obj.get(key).toString()));
                //reader.setLenient(true);
               //AccessPoint accessPoint = gson.fromJson(reader, AccessPoint.class);
                JsonElement el = obj.get(key);
                AccessPoint accessPoint = gson.fromJson(obj.get(key).getAsString(), AccessPoint.class);
                Object coordinates = valid.obj.get(accessPoint.getBssid());
                if (accessPoint.getSsid().equals("JCT-Lev-WiFi") && coordinates != null){
                    // the model contains this
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
            //System.out.println("Key :" + key + "  Value :" + obj.get(key));
        }
        if (information.getAccessPointsLength() <= 2){
            return null;
        }

        for (AccessPoint accssesPoint:
                information.getAccessPoints()) {
                double d = 0;
                d = calc.Find2DDistance(2.7,calc.CalculateNormalDistanceByFrequencyAndRssi(accssesPoint.getRssi(), accssesPoint.getFrequency()));
                System.out.printf("%s  %.4f   rssi is %d \n",accssesPoint.getBssid(), d, accssesPoint.getRssi());
        }

        return null;
    }
}
