package com.orange.mqttDeviceModePublishData.messages;

/*
 * Copyright (C) 2019 Orange Business Services
 *
 * This software is distributed under the terms and conditions of the '3-Clause BSD'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.ByteBuffer;

public class BinaryEncodedMessage extends SimpleMessage {
    public MqttMessage getMessage() {
        MqttMessage mqttMessage = new MqttMessage(ByteBuffer.allocate(4).putFloat(1337.7331F).array());
        mqttMessage.setQos(QOS);
        return mqttMessage;
    }
}
