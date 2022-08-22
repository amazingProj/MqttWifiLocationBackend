package Model;

import Utils.AccessPointLocation;

import java.util.HashMap;

public class ValidAccessPoint {
    public HashMap obj = new HashMap<String, AccessPointLocation>();
    public ValidAccessPoint(){
        obj.put("b4:5d:50:bd:ac:a0", new AccessPointLocation(12.5, 6, 2.5, 3, 302, "floor 3 weird room", -51));
        obj.put("b4:5d:50:bd:a4:60", new AccessPointLocation(12.5, 16, 3.5, 3, 330,  "level 3 outside", -51));
        obj.put("b4:5d:50:bd:9e:00", new AccessPointLocation(7,9, 9,5, 560,"level 5 near 560", -51));
        obj.put("b4:5d:50:bd:c8:e0", new AccessPointLocation(21.7, 3, 9, 5, 520, "level 5 offices", -51));
        obj.put("b4:5d:50:bd:93:40", new AccessPointLocation(7.9, 14.5, 6,4,  470, -51));
        obj.put("b4:5d:50:bd:9e:60", new AccessPointLocation(9,5.9 , 6,4 ,460, -51));
        obj.put("b4:5d:50:bd:a0:80", new AccessPointLocation(8.1,15 , 6,4 , 450, -51));
        obj.put("b4:5d:50:bd:a3:c0", new AccessPointLocation(9.2, 4.2, 6, 4, 440, -51));
        obj.put("b4:5d:50:bd:ac:40", new AccessPointLocation(21.6, 2.7, 6, 4, 420,-51));
    }
}
