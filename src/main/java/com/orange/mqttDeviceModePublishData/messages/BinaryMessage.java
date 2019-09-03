package com.orange.mqttDeviceModePublishData.messages;

import org.apache.commons.io.IOUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.io.InputStream;

public class BinaryMessage extends SimpleMessage {
    public MqttMessage getMessage() {
        try {
            try (InputStream is = BinaryMessage.class.getResourceAsStream("/logo.gif")) {
                MqttMessage mqttMessage = new MqttMessage(IOUtils.toByteArray(is));
                mqttMessage.setQos(QOS);
                return mqttMessage;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
