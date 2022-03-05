package Model;

import Utils.Primitives.AccessPointLocation;

import java.util.HashMap;

public class ValidAccessPoint {
    public HashMap obj = new HashMap<String, AccessPointLocation>();
    public ValidAccessPoint(){
        obj.put("b4:5d:50:bd:ac:a0", new AccessPointLocation(12, 6, 2.5, 3, 302, "floor 3 weird room"));
        obj.put("b4:5d:50:bd:a4:60", new AccessPointLocation(12, 16, 3.5, 3, 300, "level 3 outside"));
        obj.put("b4:5d:50:bd:9e:00", new AccessPointLocation(7,9, 9,5, "level 5 near 560"));
        obj.put("b4:5d:50:bd:c8:e0", new AccessPointLocation(21.7, 3, 9, 5, "level 5 offices"));
        obj.put("b4:5d:50:bd:93:40", new AccessPointLocation(7.9, 14.5, 6,4,  470));
        obj.put("b4:5d:50:bd:9e:60", new AccessPointLocation(9,5.9 , 6,4 ,460));
        obj.put("b4:5d:50:bd:a0:80", new AccessPointLocation(8.1,15 , 6,4 , 450));
        obj.put("b4:5d:50:bd:a3:c0", new AccessPointLocation(9.2, 4.2, 6, 4, 440));
        obj.put("b4:5d:50:bd:ac:40", new AccessPointLocation(21.6, 2.7, 6, 4));
    }
}
