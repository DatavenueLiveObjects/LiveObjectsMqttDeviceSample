package com.orange.mqttDeviceModePublishData.messages;

/*
 * Copyright (C) 2019 Orange Business Services
 *
 * This software is distributed under the terms and conditions of the '3-Clause BSD'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */

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
