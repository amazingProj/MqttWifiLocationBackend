package Model;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class ValidAccessPoint {
    public HashMap obj = new HashMap<String, Coordinates>();
    public ValidAccessPoint(){
        obj.put("b4:5d:50:bd:a0:80", new Coordinates(15.5, 9, 2.7));
        obj.put("b4:5d:50:bd:9e:60", new Coordinates(6.2, 7.9, 2.7));
        obj.put("b4:5d:50:bd:93:40", new Coordinates(4.25, 7.6, 2.7));
        obj.put("b4:5d:50:bd:a3:c0", new Coordinates(15.5, 9.3, 2.7));
    }

    public void mongodbAtlasExample(){

    }
}
