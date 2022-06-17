import Controller.EventsHandler;
import Controller.Sender;
import com.google.gson.JsonObject;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * class represents the main program of the server
 * includes main function
 */
public class Main {
    private static final EventsHandler eventsHandler = new EventsHandler();
    private static final String host = "712d6a94edd544ddac8b5c44600f18d3.s1.eu.hivemq.cloud";
    private static final String username = "Esp32";
    private static final String password = "Esp32Asaf";
    private static final String androidTopic = "mqtt/android/wifi/messages";
    private static final String espTopic = "users/wifi/scan";

    /**
     * main program
     * @param args - arguments no use in our program
     */
    public static void main(String[] args) {
        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8884)
                .sslWithDefaultConfig()
                .webSocketConfig()
                .serverPath("mqtt")
                .applyWebSocketConfig()
                .buildBlocking();

        while (!client.getState().isConnected()){
            try {
                client.connectWith()
                        .simpleAuth()
                        .username(username)
                        .password(UTF_8.encode(password))
                        .applySimpleAuth()
                        .send();

                TimeUnit.SECONDS.sleep(1);
            }
            catch (Exception e){}
        }
        System.out.println("Connected successfully");

        eventsHandler.addObserverPublishLocationEvent(new Sender(client));

        client.subscribeWith()
                .topicFilter(espTopic)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();

        client.subscribeWith()
                .topicFilter(androidTopic)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();


        client.toAsync().publishes(ALL, publish -> {
            String messageReceived = UTF_8.decode(publish.getPayload().get()).toString();
            String topic = publish.getTopic().toString();
            eventsHandler.messageArriveEvent(messageReceived, topic);
        });



/*
        //wait 0 ms before doing the action and do it evry 1000ms (1second)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                JsonObject result1 = new JsonObject();
                Random rand = new Random();
                int x = rand.nextInt(22);
                int y = rand.nextInt(15);
                int z = rand.nextInt(10);
                result1.addProperty("x", x);
                result1.addProperty("y", y);
                result1.addProperty("z", z);
                int floor = rand.nextInt(3) + 3;
                result1.addProperty("FloorLevel", 4);
                int rnd = rand.nextInt(3);
                if (rnd == 1)
                {
                    result1.addProperty("ID", "94:B9:7E:FA:92:14");
                }
                else
                {
                    result1.addProperty("ID", "AssafAndroid1010");
                }

                int bat = rand.nextInt(100);
                result1.addProperty("BATTERY", bat + "%");
                boolean f = (x % 2 == 0)? true : false;
                String msg = (f) ? "Alarmed" : "Is not alarmed";
                result1.addProperty("ISAlarmed", msg);
                System.out.println(result1.toString());
                client.publishWith()
                        .topic("users/devices/location")
                        .payload(UTF_8.encode(result1.toString()))
                        .qos(MqttQos.EXACTLY_ONCE)
                        .send();

            }
        }, 0, 10000);
       */


    }
}