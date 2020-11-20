/*
 * Software Name : Live Objects Mqtt Device Sample
 * Version: 1.0
 * SPDX-FileCopyrightText: Copyright (c) 2019-2020 Orange Business Services
 * SPDX-License-Identifier: BSD-3-Clause
 * This software is distributed under the BSD-3-Clause,
 * the text of which is available at https://opensource.org/licenses/BSD-3-Clause
 * or see the "LICENCE" file for more details.
 * Software description: Sample application for Orange Datavenue Live Objects <a>https://liveobjects.orange-business.com</a>
 */

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
