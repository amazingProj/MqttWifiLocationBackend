package Model;

import Algorithms.FindUserLocationAndroid;
import com.google.gson.*;

public class AndroidPayloadDecoder implements Decoder {
    FindUserLocationAndroid indoorUserLocation = new FindUserLocationAndroid();

    public AndroidPayloadDecoder() {

    }

    @Override
    public JsonObject Decode(String androidMessage) {
        if (androidMessage == null) {
            return null;
        }
        JsonObject result = null;
        JsonParser parser = new JsonParser();
        try {
            JsonObject obj = parser.parse(androidMessage).getAsJsonObject();
            String keyOfAlarmed = "isAlarmed";
            JsonElement val = obj.get(keyOfAlarmed);
            String keyOf = "NumberOfAccessPoints";
            JsonElement i = obj.get(keyOf);
            if (i.getAsInt() > 1) {
                result = indoorUserLocation.FindAndroidUserLocation(obj);
            }

        } catch (Exception e) {
            System.out.println(e);
        }


        System.out.println("");
        return result;
    }
}
