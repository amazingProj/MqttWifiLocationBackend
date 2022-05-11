package Model;

import java.util.HashMap;

/**
 * class represents a rssi error given by many real life measurements
 */
public class RssiError {
    private HashMap<Integer, HashMap<String, Integer>> errors = new HashMap<>();

    /**
     * c'ctor of rssi error
     */
    public RssiError()
    {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(
                "b4:5d:50:bd:a0:80", 7
        );
        map.put(
                "b4:5d:50:bd:93:40", 16
        );
        map.put(
                "b4:5d:50:bd:9e:60", 5
        );
        map.put(
                "b4:5d:50:bd:a3:c0", 7
        );
        map.put(
                "b4:5d:50:bd:a4:60", 8
        );
        map.put(
                "b4:5d:50:bd:9e:00", 12
        );
        map.put(
                "b4:5d:50:bd:ac:40", -12
        );
        map.put(
                "b4:5d:50:bd:ac:a0", 8
        );
        errors.put(450, map);

        map.clear();
        map.put(
                "b4:5d:50:bd:a4:60", -2
        );
        map.put(
                "b4:5d:50:bd:93:40", 17
        );
        map.put(
                "b4:5d:50:bd:ac:a0", 14
        );
        map.put(
                "b4:5d:50:bd:a0:80", 16
        );
        errors.put(330, map);
        map.clear();
        map.put(
                "b4:5d:50:bd:ac:a0", 25
        );
        errors.put(440, map);
        map.clear();
        map.put(
                "b4:5d:50:bd:ac:40", -22
        );
        map.put(
                "b4:5d:50:bd:93:40", 25
        );
        map.put(
                "b4:5d:50:bd:9e:60", 7
        );
        map.put(
                "b4:5d:50:bd:a0:80", 10
        );
        map.put(
                "b4:5d:50:bd:a3:c0", 2
        );
        map.put(
                "b4:5d:50:bd:9e:00", 20
        );
        map.put(
                "b4:5d:50:bd:ac:a0", 16
        );
        errors.put(460, map);
        map.clear();
        map.put(
                "b4:5d:50:bd:9e:00", 4
        );
        map.put(
                "b4:5d:50:bd:c8:e0", -3
        );
        map.put(
                "b4:5d:50:bd:9e:60", 20
        );
        map.put(
                "b4:5d:50:bd:a3:c0", 20
        );
        errors.put(560, map);
    }

    /**
     * gets the error of certain ap in certain place
     * @param roomNumber - a room number
     * @param mac - a mac address
     * @return the error
     */
    public int getError(int roomNumber, String mac)
    {
        int errorWithInput = -1;
        if (mac == null) return errorWithInput;
        HashMap<String, Integer> map = errors.get(roomNumber);
        if (map == null) return -1;
        Integer integer = map.get(mac);
        if (integer == null) return errorWithInput;
        return integer.intValue();
    }
}
