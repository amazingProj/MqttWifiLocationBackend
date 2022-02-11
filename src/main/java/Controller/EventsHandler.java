package Controller;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class EventsHandler {
    private List<Observer> observerListPublishLocationEvent = new ArrayList<>();

    public void addObserverPublishLocationEvent(Observer observer){
        if (observer == null) return;
        observerListPublishLocationEvent.add(observer);
    }

    public void messageArriveEvent(String wifiScanMessage, String topic){
        if (topic.equals("users/wifi/scan")){
            return;
        }
        final AndroidPayloadDecoder androidPayloadDecoder = new AndroidPayloadDecoder();
        JsonObject result = androidPayloadDecoder.Decode(wifiScanMessage);
        if (result != null){
            notifyAllUserLocationObservers(result, "users/devices/location");
        }
    }

    public void notifyAllUserLocationObservers(JsonObject message, String topic){
        if (observerListPublishLocationEvent.isEmpty()) return;
        for (Observer observer
        :
        observerListPublishLocationEvent){
            observer.publishMessage(message, topic);
        }
    }
}
