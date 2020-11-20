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

import com.google.gson.Gson;
import com.orange.mqttDeviceModePublishData.jsonpatterns.LoData;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings("WeakerAccess")
public class SimpleMessage {
    public static final int QOS = 1;
    double pseudoRandom1000 = (double) (new Date().getTime() % 1000);

    byte[] prepareMessage(String stream, String model) {
        // create message
        LoData loData = new LoData();

        Date msgDt       = new Date();
        loData.streamId  = stream;
        loData.model     = model;
        loData.timestamp = toISO8601UTC(msgDt);
        loData.location  = new LoData.Location();
        loData.location.lat      = 48.125 + (pseudoRandom1000 / 1000);
        loData.location.lon      = 2.185 + (pseudoRandom1000 / 1000);
        loData.location.alt      = 100 + (pseudoRandom1000 / 10);
        loData.location.accuracy = 1 + pseudoRandom1000;
        loData.location.provider = "random";
        loData.value     = preparePayload(msgDt);
        loData.tags      = Arrays.asList("MQTTdata", "SampleTag");

        String msg = new Gson().toJson(loData);
        System.out.println("Publishing message: " + msg);
        return msg.getBytes();
    }

    Object preparePayload(Date msgDt) {
        SampleData myData = new SampleData();
        myData.log = "Message from deviceMode on dev/data on " + msgDt;
        myData.temperature = (int) (Math.pow(pseudoRandom1000 / 100, 2));
        myData.hygrometry = (int) (pseudoRandom1000 / 10);
        return myData;
    }

    public MqttMessage getMessage(String stream, String model) {
        MqttMessage mqttMessage = new MqttMessage(prepareMessage(stream, model));
        mqttMessage.setQos(QOS);
        return mqttMessage;
    }

    public static class SampleData {
        public String log;
        public int temperature;
        public int hygrometry;
    }

    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }
}
