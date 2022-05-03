package Model;

import Algorithms.FindUserLocation;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Esp32PayloadDecoder implements Decoder {
    FindUserLocation indoorUserLocation = new FindUserLocation();

    @Override
    public JsonObject Decode(String esp32Message) {
        if (esp32Message == null){
            return null;
        }
        JsonObject result = null;
        JsonParser parser = new JsonParser();
        try {
            JsonObject obj = parser.parse(esp32Message).getAsJsonObject();
            String keyOf = "NumberOfAccessPoints";
            JsonElement i = obj.get(keyOf);
            if (i.getAsInt() >= 2) {
                System.out.printf("\n%s \t\t\t   %s  %s   %s   %s\n", "Bssid", "Distance", "Rssi", "Floor", "Room");
                result = indoorUserLocation.FindEsp32UserLocation(obj);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return result;
    }
}
