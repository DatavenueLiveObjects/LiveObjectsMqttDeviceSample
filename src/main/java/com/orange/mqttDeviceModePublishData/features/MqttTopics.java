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

@SuppressWarnings({"WeakerAccess", "unused"})
public class MqttTopics {
    public static final String MQTT_TOPIC_PUBLISH_DATA            = "dev/data";
    public static final String MQTT_TOPIC_PUBLISH_DATA_RAW_PREFIX = "dev/v1/data/binary";

    public static final String MQTT_TOPIC_PUBLISH_RESOURCE        = "dev/rsc";
    public static final String MQTT_TOPIC_SUBSCRIBE_RESOURCE      = "dev/rsc/upd";
    public static final String MQTT_TOPIC_RESPONSE_RESOURCE       = "dev/rsc/upd/res";
    public static final String MQTT_TOPIC_RESPONSE_RESOURCE_ERROR = "dev/rsc/upd/err";

    public static final String MQTT_TOPIC_SUBSCRIBE_COMMAND       = "dev/cmd";
    public static final String MQTT_TOPIC_RESPONSE_COMMAND        = "dev/cmd/res";

    public static final String MQTT_TOPIC_PUBLISH_CONFIG          = "dev/cfg";
    public static final String MQTT_TOPIC_SUBSCRIBE_CONFIG        = "dev/cfg/upd";
}
