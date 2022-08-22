# Indoor  Positioning Location

Decode messages of rssi scans and outputs the exact location

Using java and gradle to actually get the location of our certain building
and decode the messages given by IoT.

We have used our IoT by using Android and ESP32 devices and 
use our physics campus building as a tested building to our algorithms.

# Project structure

We have four pages and main program that we can handle the project easier.

## Algorithms package

For the algorithms such that given a rssi calculate the distance between them and the signal owner location
according to researches we have read.

```` java
    /* distnace calculating */
    
     public double CalculateDistanceByRssi(int rssi){
        if (rssi == -127){
            return Double.MAX_VALUE;
        }

        if (rssi < HIGH_RSSI_LEVEL)
        {
            double fraction = (double)(measuredPower - rssi) / (environmentalFactorSoft * 10.0);
            return Math.pow(10, fraction);
        }

        double fraction = (double)(measuredPower - rssi) / (environmentalFactor * 10.0);
        return Math.pow(10, fraction);
    }


    /*  solution  */
     public Optimum solve(boolean debugInfo) {
        int numberOfPositions = function.getPositions().length;
        int positionDimension = function.getPositions()[0].length;

        double[] initialPoint = new double[positionDimension];
        // initial point, use average of the vertices
        for (int i = 0; i < function.getPositions().length; i++) {
            double[] vertex = function.getPositions()[i];
            for (int j = 0; j < vertex.length; j++) {
                initialPoint[j] += vertex[j];
            }
        }
        for (int j = 0; j < initialPoint.length; j++) {
            initialPoint[j] /= numberOfPositions;
        }

        if (debugInfo) {
            StringBuilder output = new StringBuilder("initialPoint: ");
            for (int i = 0; i < initialPoint.length; i++) {
                output.append(initialPoint[i]).append(" ");
            }
            System.out.println(output);
        }

        double[] target = new double[numberOfPositions];
        double[] distances = function.getDistances();
        double[] weights = new double[target.length];
        for (int i = 0; i < target.length; i++) {
            target[i] = 0.0;
            weights[i] = inverseSquareLaw(distances[i]);
        }

        return solve(target, weights, initialPoint, debugInfo);
    }

````

You can see explanation later in this  document.


## Main program 


```` java
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

````

firstly connect to HiveMQTT cloud,
listen to the server's 8884 (mqtt over websocket secure)
pass this message to decode and event handler
such that 
```` java 
eventsHandler.messageArriveEvent(messageReceived, topic); 
````

received message in JSON format of a certain topic, 
deserialized  it into classes 
so 
one topic is for android and one for esp32