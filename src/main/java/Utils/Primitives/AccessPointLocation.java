package Utils.Primitives;

public class AccessPointLocation {
    double x;
    double y;
    double z;
    int floorLevel = 0;
    int room = 0;

    public AccessPointLocation(AccessPointLocation coordinates){
        x = coordinates.getX();
        y = coordinates.getY();
        z = coordinates.getZ();
        floorLevel = coordinates.getFloorLevel();
        room = coordinates.getRoom();
    }


    public AccessPointLocation(double _x, double _y, double _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public AccessPointLocation(double _x, double _y, double _z, int _floorLevel) {
        x = _x;
        y = _y;
        z = _z;
        floorLevel = _floorLevel;
    }

    public AccessPointLocation(double _x, double _y, double _z, int _floorLevel, int _room) {
        x = _x;
        y = _y;
        z = _z;
        floorLevel = _floorLevel;
        room = _room;
    }

    public AccessPointLocation(double _x, double _y, double _z, int _floorLevel, String _noteComment) {
        x = _x;
        y = _y;
        z = _z;
        floorLevel = _floorLevel;
        noteComment = _noteComment;
    }

    public AccessPointLocation(double _x, double _y, double _z, int _floorLevel, int _room, String _noteComment) {
        x = _x;
        y = _y;
        z = _z;
        floorLevel = _floorLevel;
        room = _room;
        noteComment = _noteComment;
    }

    public AccessPointLocation(double _x, double _y){
        x = _x;
        y = _y;
        z = 0;
    }

    public String getNoteComment() {
        return noteComment;
    }

    public void setNoteComment(String noteComment) {
        this.noteComment = noteComment;
    }

    String noteComment;
    // lobi = 300

    public int getFloorLevel() {
        return floorLevel;
    }

    public int getRoom() {
        return room;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
