package Model;

import Primitives.Coordinates;

import java.util.HashMap;

public class ValidAccessPoint {
    public HashMap obj = new HashMap<String, Coordinates>();
    public ValidAccessPoint(){
        obj.put("b4:5d:50:bd:ac:40", new Coordinates(15.5, 9, 2.7));
        obj.put("b4:5d:50:bd:a3:c0", new Coordinates(6.2, 7.9, 2.7));
        obj.put("b4:5d:50:bd:9e:60", new Coordinates(4.25, 7.6, 2.7));
        obj.put("b4:5d:50:bd:a0:80", new Coordinates(15.5, 9.3, 2.7));
        obj.put("34:49:5b:16:9d:e4", new Coordinates(4,5,1));
        obj.put("f0:b4:d2:19:11:e3", new Coordinates(9, 8, 1));
    }

    public void mongodbAtlasExample(){

    }
}
