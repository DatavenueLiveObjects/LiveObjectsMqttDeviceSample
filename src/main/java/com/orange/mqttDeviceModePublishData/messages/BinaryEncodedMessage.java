package com.orange.mqttDeviceModePublishData.messages;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.ByteBuffer;

public class BinaryEncodedMessage extends SimpleMessage {
    public MqttMessage getMessage() {
        MqttMessage mqttMessage = new MqttMessage(ByteBuffer.allocate(4).putFloat(1337.7331F).array());
        mqttMessage.setQos(QOS);
        return mqttMessage;
    }
}
