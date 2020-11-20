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
import com.orange.mqttDeviceModePublishData.jsonpatterns.LoConfig;
import org.eclipse.paho.client.mqttv3.*;

import static com.orange.mqttDeviceModePublishData.features.MqttTopics.MQTT_TOPIC_SUBSCRIBE_CONFIG;

@SuppressWarnings("WeakerAccess")
public class DeviceConfig {
    public static final int QOS = 1;
    private final MqttClient mqttClient;

    private String logLevel     = "INFO";
    private double trigger      = 20.251;
    private int    connDelaySec = 10002;

    public DeviceConfig(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void publish() throws MqttException {
        publish(null);
    }

    private void publish(Long cid) throws MqttException {
        LoConfig config = new LoConfig();

        config.cfg.put("logLevel",     new LoConfig.CfgParameter("str", logLevel));
        config.cfg.put("trigger",      new LoConfig.CfgParameter("f64", trigger));
        config.cfg.put("connDelaySec", new LoConfig.CfgParameter("u32", connDelaySec));
        if (cid != null)
            config.cid = cid;

        String configJson = new Gson().toJson(config);
        System.out.println("Publishing configuration message: " + configJson);

        MqttMessage message = new MqttMessage(configJson.getBytes());
        message.setQos(QOS);

        mqttClient.publish(MqttTopics.MQTT_TOPIC_PUBLISH_CONFIG, message);
        System.out.println("Configuration published");
    }

    public void subscribeToConfigChanges() throws MqttException {
        mqttClient.subscribe(MQTT_TOPIC_SUBSCRIBE_CONFIG, this::messageArrived);
        System.out.println("Device configuration changes subscribed.");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        // parse message as configuration changes
        LoConfig config = new Gson().fromJson(new String(mqttMessage.getPayload()), LoConfig.class);
        System.out.println("Device configuration received: " + new Gson().toJson(config));

        if (config.cfg.containsKey("logLevel"))
            logLevel = LoConfig.toString(config.cfg.get("logLevel").v);
        if (config.cfg.containsKey("trigger"))
            trigger = LoConfig.toDouble(config.cfg.get("trigger").v);
        if (config.cfg.containsKey("connDelaySec"))
            connDelaySec = LoConfig.toInt(config.cfg.get("connDelaySec").v);

        new Thread(() -> {
            try {
                publish(config.cid);
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
