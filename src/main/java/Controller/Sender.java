package Controller;

import com.google.gson.JsonObject;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Sender implements Observer{
    private static Mqtt5BlockingClient clientSender;
    public Sender(Mqtt5BlockingClient _clientSender){
        clientSender = _clientSender;
    }
    @Override
    public void publishMessage(JsonObject payload, String topic) {
        clientSender.publishWith()
                .topic(topic)
                .payload(UTF_8.encode(payload.toString()))
                .qos(MqttQos.EXACTLY_ONCE)
                .send();
        System.out.printf("%s  %s", topic, payload);
    }
}
