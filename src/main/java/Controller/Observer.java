package Controller;

import com.google.gson.JsonObject;

/**
 * interfaces observer
 */
public interface Observer {
    /**
     * publishes a message to client's broker
     * @param payload - a payload in json format
     * @param topic - a topic to publish the message
     */
    void publishMessage(JsonObject payload, String topic);
}
