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

package com.orange.mqttDeviceModePublishData.features;

import com.google.gson.Gson;
import com.orange.mqttDeviceModePublishData.jsonpatterns.LoCommand;
import org.eclipse.paho.client.mqttv3.*;

import java.util.HashMap;

import static com.orange.mqttDeviceModePublishData.features.MqttTopics.MQTT_TOPIC_SUBSCRIBE_COMMAND;

@SuppressWarnings("WeakerAccess")
public class DeviceCommands {
    public static final int QOS = 1;
    private final MqttClient mqttClient;

    public DeviceCommands(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void subscribeToCommands() throws MqttException {
        mqttClient.subscribe(MQTT_TOPIC_SUBSCRIBE_COMMAND, this::messageArrived);
        System.out.println("Device commands subscribed.");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        // parse message as command
        LoCommand command = new Gson().fromJson(new String(mqttMessage.getPayload()), LoCommand.class);
        System.out.println("Device command received: " + new Gson().toJson(command));

        LoCommand.LoCommandResponse response = new LoCommand.LoCommandResponse(new HashMap<>(), command.cid);
        response.res.put("my-ack", "this is my command acknowledge to " + command.req);

        new Thread(() -> {
            try {

                String responseJson = new Gson().toJson(response);
                System.out.println("Publishing command acknowledge message: " + responseJson);

                MqttMessage message = new MqttMessage(responseJson.getBytes());
                message.setQos(QOS);

                mqttClient.publish(MqttTopics.MQTT_TOPIC_RESPONSE_COMMAND, message);
                System.out.println("Command ack published");

            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
           }
        }).start();
    }
}
