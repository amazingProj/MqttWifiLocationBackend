package Controller;


import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class EventsHandler {
    private List<Observer> observerListPublishLocationEvent = new ArrayList<>();
    String message = "";
    //private
    public void addObserverPublishLocationEvent(Observer observer){
        if (observer == null) return;
        observerListPublishLocationEvent.add(observer);
    }

    /**
     * manager of the message which message is first when it is many messages arrive simultaneously
     */
    public void messagesHandler(){

    }

    /**
     *
     * @param wifiScanMessage - a message
     * @param topic
     */
    public void messageArriveEvent(String wifiScanMessage, String topic){
        JsonObject result = null;

        if (topic.equals("users/wifi/scan")){
            final Decoder esp32PayloadDecoder = new Esp32PayloadDecoder();
            String[] arr = wifiScanMessage.split(",|\\s+", 4);
            /*for (String str : arr){
                System.out.println(str);
            }*/
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
            final Decoder androidPayloadDecoder = new AndroidPayloadDecoder();
            result = androidPayloadDecoder.Decode(wifiScanMessage);
        }

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
