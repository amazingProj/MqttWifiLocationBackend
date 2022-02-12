package Controller;

import com.google.gson.JsonObject;

public interface Decoder {
    JsonObject Decode(String message);
}
