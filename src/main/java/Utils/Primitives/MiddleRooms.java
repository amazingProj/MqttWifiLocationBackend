package Utils.Primitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiddleRooms {
    public class Key {

        private final int x;
        private final int y;

        public Key(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Key)) return false;
            Key key = (Key) o;
            return (x == key.x && y == key.y) || (x == key.y && y == key.x);
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

    }
    public HashMap<Integer, TwoDVectorCoordinates> middleOfRooms = new HashMap<>();
    private HashMap<Key, Integer> neighborsList = new HashMap<>();

    public MiddleRooms(){
        middleOfRooms.put(470, new TwoDVectorCoordinates(4, 14, 4));
        middleOfRooms.put(450, new TwoDVectorCoordinates(12,14, 4));
        middleOfRooms.put(460, new TwoDVectorCoordinates(5, 4.5));
        middleOfRooms.put(440, new TwoDVectorCoordinates(11.5,4.5));
        middleOfRooms.put(420, new TwoDVectorCoordinates(22,5));
        middleOfRooms.put(302, new TwoDVectorCoordinates(11.5,5.8));
        middleOfRooms.put(320, new TwoDVectorCoordinates(12,12));
        middleOfRooms.put(520, new TwoDVectorCoordinates(22,6));
        middleOfRooms.put(560, new TwoDVectorCoordinates(7.5,9.5));

        // neighbors for first level
        neighborsList.put(new Key(470, 450), 1);
        neighborsList.put(new Key(460, 440), 1);
        neighborsList.put(new Key(440, 420), 2);
        neighborsList.put(new Key(450, 460), 2);
        neighborsList.put(new Key(450, 440), 2);
        neighborsList.put(new Key(470, 440), 2);
        neighborsList.put(new Key(470, 460), 2);
        neighborsList.put(new Key(450, 420), 3);
        neighborsList.put(new Key(440, 420), 3);
        neighborsList.put(new Key(460, 420), 4);
        neighborsList.put(new Key(470, 420), 4);

        neighborsList.put(new Key(302, 315), 2);
    }

    public int neighborhoodsLevel(int roomNumber, int anotherRoomNumber){
        if (roomNumber == anotherRoomNumber){
            return 0;
        }
        Key key = new Key(roomNumber, anotherRoomNumber);
        if (neighborsList.containsKey(key))
        {
            return neighborsList.get(key);
        }
        return 0;
    }

}
