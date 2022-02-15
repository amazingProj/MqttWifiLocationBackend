package Controller;

import Model.AndroidDataHandler.FindUserLocation;
import com.google.gson.*;

public class AndroidPayloadDecoder implements Decoder{
    FindUserLocation indoorUserLocation = new FindUserLocation();

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
