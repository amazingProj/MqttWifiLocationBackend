package Controller;

import Model.AndroidDataHandler.FindUserLocation;
import com.google.gson.*;

public class AndroidPayloadDecoder {
    FindUserLocation indoorUserLocation = new FindUserLocation();

    public AndroidPayloadDecoder() {

    }

    public JsonObject Decode(String androidMessage) {
        if (androidMessage == null) {
            return null;
        }
        JsonObject result = null;
        JsonParser parser = new JsonParser();
        try {
            JsonObject obj = parser.parse(androidMessage).getAsJsonObject();
            String keyOf = "NumberOfAccessPoints";
            System.out.println("this is an obj");
            JsonElement i = obj.get(keyOf);
            if (i.getAsInt() > 2) {
                result = indoorUserLocation.FindAndroidUserLocation(obj);
            }

        } catch (Exception e) {
            System.out.println(e);
        }


        System.out.println("");
        return result;
    }
}
