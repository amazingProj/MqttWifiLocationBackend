package Controller;

import Controller.Android.PayloadInformation;
import com.google.gson.*;

import java.util.Iterator;

public class AndroidPayloadDecoder {
    public AndroidPayloadDecoder(){

    }

    public void Decode(String androidMessage){
        if (androidMessage == null){
            return;
        }

        JsonParser parser = new JsonParser();
       try {
           JsonObject obj = parser.parse(androidMessage).getAsJsonObject();
           String keyOf = "NumberOfAccessPoints";
           System.out.println("this is an obj");

           if (Integer.parseInt(obj.get(keyOf).toString()) > 0){
               System.out.println("Yessss");
           }
           Iterator<String> keys = obj.keySet().iterator();

           while (keys.hasNext()) {
               String key = keys.next();
               System.out.println("Key :" + key + "  Value :" + obj.get(key));
           }
       } catch(Exception e){
            System.out.println(e);
       }


        System.out.println("");
    }
}
