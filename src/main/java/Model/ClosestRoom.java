package Model;

import Utils.Coordinates;

import java.util.HashMap;

public class ClosestRoom {
    public HashMap<Integer, Coordinates> coordinatesHashMap = new HashMap<>();

    public ClosestRoom()
    {
        coordinatesHashMap.put(470, new Coordinates(4.5, 14.5, 4.5));
        coordinatesHashMap.put(450, new Coordinates(14.5, 12.5, 4.5));
        coordinatesHashMap.put(460, new Coordinates(4.5, 5.5, 4.5));
        coordinatesHashMap.put(440, new Coordinates(13, 5, 4.5));
        coordinatesHashMap.put(420, new Coordinates(21, 5, 4.5));
    }

}
