package Controller;

import com.google.gson.JsonObject;

public interface Observer {
    void publishMessage(JsonObject payload, String topic);
}
