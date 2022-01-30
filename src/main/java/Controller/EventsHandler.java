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
        final AndroidPayloadDecoder androidPayloadDecoder = new AndroidPayloadDecoder();
        JsonObject result = androidPayloadDecoder.Decode(wifiScanMessage);
        if (result != null){
            notifyAllUserLocationObservers(result);
        }
    }

    public void notifyAllUserLocationObservers(JsonObject message){
        if (observerListPublishLocationEvent.isEmpty()) return;
        for (Observer observer
        :
        observerListPublishLocationEvent){
            observer.publishMessage(message);
        }
    }
}
