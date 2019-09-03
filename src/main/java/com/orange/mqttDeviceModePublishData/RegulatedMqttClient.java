package com.orange.mqttDeviceModePublishData;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Implementation of an Mqtt Client with a basic network throughput limit by sleeping on each publish request
 */
@SuppressWarnings("WeakerAccess")
public class RegulatedMqttClient extends MqttClient {
    private final long sleepAfterPublishMillis;

    public RegulatedMqttClient(String server, String clientId, MemoryPersistence memoryPersistence, long sleepAfterPublishMillis) throws MqttException {
        super(server, clientId, memoryPersistence);
        this.sleepAfterPublishMillis = sleepAfterPublishMillis;
    }

    @Override
    public void publish(String topic, byte[] payload, int qos, boolean retained) throws MqttException {
        super.publish(topic, payload, qos, retained);
        sleep();
    }

    @Override
    public void publish(String topic, MqttMessage message) throws MqttException {
        super.publish(topic, message);
        sleep();
    }

    /**
     * The MQTT link has a maximum threshold.
     */
    private void sleep() {
        try {
            Thread.sleep(sleepAfterPublishMillis);
        } catch (InterruptedException ignored) {}
    }
}
