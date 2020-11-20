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
import com.google.gson.reflect.TypeToken;
import com.orange.mqttDeviceModePublishData.MyDevice;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Map;

@SuppressWarnings("FieldCanBeLocal")
public class AppModeHandler {
    private MqttClient mqttClient;

    public void subscribeToFifo() throws MqttException {
        mqttClient = MyDevice.createAndConnectMqttClientAsApplication();
        System.out.println("Connected to Live Objects in Application Mode" + (MyDevice.SECURED ? " with TLS" : ""));

        mqttClient.subscribe("fifo/" + MyDevice.HANDLE_APPMODE_FIFO, this::messageArrived);
        System.out.println("Fifo " + MyDevice.HANDLE_APPMODE_FIFO + " subscribed.");
    }

    private void messageArrived(String s, MqttMessage mqttMessage) {
        Map<String, Object> msg = new Gson().fromJson(new String(mqttMessage.getPayload()), new TypeToken<Map<String, Object>>(){}.getType());
        System.out.println("FiFo message received: dated " + msg.get("timestamp")+ " : " + msg);
    }
}
