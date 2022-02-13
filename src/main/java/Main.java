import Controller.EventsHandler;
import Controller.Observer;
import com.google.gson.JsonObject;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * class represents the main program of the server
 * includes main function
 */
public class Main implements Observer {
    private static Mqtt5BlockingClient clientSender;
    private static final EventsHandler eventsHandler = new EventsHandler();

    public Main(){
        eventsHandler.addObserverPublishLocationEvent(this);
    }

    public static void main(String[] args) {
        final String androidTopic = "mqtt/android/wifi/messages";
        final String espTopic = "users/wifi/scan";
        final String host = "712d6a94edd544ddac8b5c44600f18d3.s1.eu.hivemq.cloud";
        final String username = "Esp32";
        final String password = "Esp32Asaf";

        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8884)
                .sslWithDefaultConfig()
                .webSocketConfig()
                .serverPath("mqtt")
                .applyWebSocketConfig()
                .buildBlocking();

        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");
        // init a global client
        clientSender = client;

        client.subscribeWith()
                .topicFilter(espTopic)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();

        client.subscribeWith()
                .topicFilter(androidTopic)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();

         // Set a callback that is called when a message is received (using the async API style).
         // Then disconnect the client after a message was received.
        client.toAsync().publishes(ALL, publish -> {
            System.out.println("Received message: " + publish.getTopic() + " -> " + UTF_8.decode(publish.getPayload().get()));
            String messageReceived = UTF_8.decode(publish.getPayload().get()).toString();
            String topic = publish.getTopic().toString();

            eventsHandler.messageArriveEvent(messageReceived, topic);
            
            //client.disconnect();
        });
    }


    @Override
    public void publishMessage(JsonObject payload, String topic) {
        clientSender.publishWith()
                .topic(topic)
                .payload(UTF_8.encode(payload.toString()))
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
    }
}