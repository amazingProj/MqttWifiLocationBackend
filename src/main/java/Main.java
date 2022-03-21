import Controller.EventsHandler;
import Controller.Sender;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * class represents the main program of the server
 * includes main function
 */
public class Main {
    private static final EventsHandler eventsHandler = new EventsHandler();

    /**
     * main program
     * @param args - arguments no use in our program
     */
    public static void main(String[] args) {
        /**
         *  path, username and password to the cloud
         */
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

        while (!client.getState().isConnected()){
            try {
                client.connectWith()
                        .simpleAuth()
                        .username(username)
                        .password(UTF_8.encode(password))
                        .applySimpleAuth()
                        .send();
            }
            catch (Exception e){

            }
        }
        System.out.println("Connected successfully");

        // init a global client subscribe to event handler which send a message to client's broker
        eventsHandler.addObserverPublishLocationEvent(new Sender(client));


        // subscribe client to topic from the cloud

        final String androidTopic = "mqtt/android/wifi/messages";
        final String espTopic = "users/wifi/scan";
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
            //System.out.println("Received message: " + topic + " -> " + messageReceived);
            eventsHandler.messageArriveEvent(messageReceived, topic);
            //client.disconnect();
        });
    }
}