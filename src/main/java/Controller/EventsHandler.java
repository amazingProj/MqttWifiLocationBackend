package Controller;

import Model.AndroidPayloadDecoder;
import Model.Decoder;
import Model.Esp32PayloadDecoder;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class EventsHandler {
    private List<Observer> observerListPublishLocationEvent = new ArrayList<>();
    String message = "";

    /**
     * adds a new observer
     * @param observer - an observer
     */
    public void addObserverPublishLocationEvent(Observer observer){
        if (observer == null) return;
        observerListPublishLocationEvent.add(observer);
    }

    /**
     * handles an arrived message
     * @param wifiScanMessage - a message
     * @param topic - a topic
     */
    public void messageArriveEvent(String wifiScanMessage, String topic){
        JsonObject result = null;

        if (topic.equals("users/wifi/scan")){
            // esp 32 topic
            final Decoder esp32PayloadDecoder = new Esp32PayloadDecoder();
            String[] arr = wifiScanMessage.split(",|\\s+", 4);
            int index = Integer.parseInt(arr[0]);
            int total = Integer.parseInt(arr[1]);
            String senderID = arr[2];
            message += arr[3];

            if (index == total){
                result = esp32PayloadDecoder.Decode(message);
                System.out.println(message);
                message = "";
            }
        }
        else if (topic.equals("mqtt/android/wifi/messages")){
            // android topic
            final Decoder androidPayloadDecoder = new AndroidPayloadDecoder();
            result = androidPayloadDecoder.Decode(wifiScanMessage);
        }

        if (result == null) return;
        // send the result
        notifyAllUserLocationObservers(result, "users/devices/location");
    }

    /**
     * notify all the observer that event occurred
     * @param message - a message
     * @param topic - a topic
     */
    public void notifyAllUserLocationObservers(JsonObject message, String topic){
        if (observerListPublishLocationEvent.isEmpty()) return;

        for (Observer observer
        :
        observerListPublishLocationEvent){
            observer.publishMessage(message, topic);
        }
    }
}
