import Controller.EventsHandler;
import Controller.Sender;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import java.util.concurrent.TimeUnit;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * class represents the main program of the server
 * includes main function
 */
public class Main {
    /**
     * global variables of the main program configuration
     */

    private static final EventsHandler eventsHandler = new EventsHandler();
    private static final String host = "712d6a94edd544ddac8b5c44600f18d3.s1.eu.hivemq.cloud";
    private static final String username = "Esp32";
    private static final String password = "Esp32Asaf";
    private static final String androidTopic = "mqtt/android/wifi/messages";
    private static final String espTopic = "users/wifi/scan";

    /**
     * main program
     * firstly connect to HiveMQTT cloud,
     * listen to the server's 8884 (mqtt over websocket secure)
     * pass this message to decode and event handler
     *
     * @param args - arguments no use in our program
     */
    public static void main(String[] args) {
        final Mqtt5BlockingClient client = MqttClient.builder().useMqttVersion5().serverHost(host).serverPort(8884).sslWithDefaultConfig().webSocketConfig().serverPath("mqtt").applyWebSocketConfig().buildBlocking();

        while (!client.getState().isConnected()) {
            try {
                client.connectWith().simpleAuth().username(username).password(UTF_8.encode(password)).applySimpleAuth().send();

                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
            }
        }
        System.out.println("Connected successfully");

        eventsHandler.addObserverPublishLocationEvent(new Sender(client));

        client.subscribeWith().topicFilter(espTopic).qos(MqttQos.EXACTLY_ONCE).send();

        client.subscribeWith().topicFilter(androidTopic).qos(MqttQos.EXACTLY_ONCE).send();


        client.toAsync().publishes(ALL, publish -> {
            String messageReceived = UTF_8.decode(publish.getPayload().get()).toString();
            String topic = publish.getTopic().toString();
            eventsHandler.messageArriveEvent(messageReceived, topic);
        });


    }
}