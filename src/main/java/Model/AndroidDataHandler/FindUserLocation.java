package Model.AndroidDataHandler;

import com.google.gson.JsonObject;

import java.util.Iterator;

public class FindUserLocation {
    public FindUserLocation(){

    }

    public JsonObject FindAndroidUserLocation(JsonObject obj){
        if (obj == null){
            return null;
        }

        Iterator<String> keys = obj.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println("Key :" + key + "  Value :" + obj.get(key));
        }
        return null;
    }
}
