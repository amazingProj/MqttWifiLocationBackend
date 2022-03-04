package Model;

import Primitives.AccessPointLocation;

import java.util.HashMap;

public class ValidAccessPoint {
    public HashMap obj = new HashMap<String, AccessPointLocation>();
    public ValidAccessPoint(){
        //obj.put("B4:5D:50:BD:AC:A0", floor 3 weird room)
        obj.put("b4:5d:50:bd:ac:a0", new AccessPointLocation(12, 6, 2.5, 3, 302));
        //obj.put("B4:5D:50:BD:A4:60", level 3 outside)
        obj.put("b4:5d:50:bd:a4:60", new AccessPointLocation(12, 16, 3.5, 3, 300));
        //obj.put("B4:5D:50:BD:9E:00", level 5 near 560)
        obj.put("b4:5d:50:bd:9e:00", new AccessPointLocation(7,9, 9,5));
        //obj.put("B4:5D:50:BD:C8:E0", level 5 offices)
        obj.put("b4:5d:50:bd:c8:e0", new AccessPointLocation(21.7, 3, 9, 5));
        obj.put("b4:5d:50:bd:93:40", new AccessPointLocation(7.9, 14.5, 6,4,  470));
        obj.put("b4:5d:50:bd:9e:60", new AccessPointLocation(9,5.9 , 6,4 ,460));
        obj.put("b4:5d:50:bd:a0:80", new AccessPointLocation(8.1,15 , 6,4 , 450));
        obj.put("b4:5d:50:bd:a3:c0", new AccessPointLocation(9.2, 4.2, 6, 4, 440));
        obj.put("b4:5d:50:bd:ac:40", new AccessPointLocation(21.6, 2.7, 6, 4));
        //obj.put("34:49:5b:16:9d:e4", new Coordinates(4,5,1));
        //obj.put("f0:b4:d2:19:11:e3", new Coordinates(9, 8, 1));
    }

    public void mongodbAtlasExample(){

    }
}
