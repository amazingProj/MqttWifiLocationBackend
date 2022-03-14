package Model;

import Utils.Primitives.TwoDVectorCoordinates;

import java.util.HashMap;
import java.util.List;

public class LocationHistory {
    HashMap<String, List<TwoDVectorCoordinates>> history;

    public LocationHistory(){
        history = new HashMap<>();
    }


}
