import Controller.EventsHandler;
import Controller.Observer;
import com.google.gson.JsonObject;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import java.util.Objects;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Main implements Observer {
    private static Mqtt5BlockingClient clientSender;
    private static EventsHandler eventsHandler = new EventsHandler();

    public Main(){
        eventsHandler.addObserverPublishLocationEvent(this);
    }

    public static void main(String[] args) {
        final String topic1 = "mqtt/android/wifi/messages";
        final String topic = "users/wifi/scan";
        final String host = "712d6a94edd544ddac8b5c44600f18d3.s1.eu.hivemq.cloud";
        final String username = "Esp32";
        final String password = "Esp32Asaf";

        /**
         * Building the client with ssl.
         */
        final Mqtt5BlockingClient client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(host)
                .serverPort(8884)
                .sslWithDefaultConfig()
                .webSocketConfig()
                .serverPath("mqtt")
                .applyWebSocketConfig()
                .buildBlocking();

        /**
         * Connect securely with username, password.
         */
        client.connectWith()
                .simpleAuth()
                .username(username)
                .password(UTF_8.encode(password))
                .applySimpleAuth()
                .send();

        System.out.println("Connected successfully");
        clientSender = client;
        /**
         * Subscribe to the topic "my/test/topic" with qos = 2 and print the received message.
         */
        client.subscribeWith()
                .topicFilter(topic1)
                .qos(MqttQos.EXACTLY_ONCE)
                .send();

        /**
         * Set a callback that is called when a message is received (using the async API style).
         * Then disconnect the client after a message was received.
         */
        client.toAsync().publishes(ALL, publish -> {
            //System.out.println("Received message: " + publish.getTopic() + " -> " + UTF_8.decode(publish.getPayload().get()));

            //System.out.println(publish.getTopic());
            if (Objects.equals(publish.getTopic().toString(), topic)){
                //System.out.println("success");
            }
            else if (Objects.equals(publish.getTopic().toString(), topic1)){
                //System.out.println("success");
               eventsHandler.messageArriveEvent(UTF_8.decode(publish.getPayload().get()).toString(), topic1);
            }
            //client.disconnect();
        });
    }


    @Override
    public void publishMessage(JsonObject payload) {
        /**
         * Publish "Hello" to the topic "my/test/topic" with qos = 2.
         */
        clientSender.publishWith()
                .topic("users/android/location")
                .payload(UTF_8.encode(payload.toString()))
                .qos(MqttQos.EXACTLY_ONCE)
                .send();

        /**
         * Publish "Hello" to the topic "my/test/topic" with qos = 2.
         */
        clientSender.publishWith()
                .topic("users/esp32/location")
                .payload(UTF_8.encode("{cordinates:{x:1,y:2}}"))
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
    }
}