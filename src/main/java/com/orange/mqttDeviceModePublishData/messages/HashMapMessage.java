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

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class HashMapMessage extends SimpleMessage {
    @Override
    Object preparePayload(Date msgDt) {
        Random rand = new Random();
        HashMap<String, Object> map = new HashMap<>();
        // Hygrometrie : 0 - 100
        map.put("hygrometry", rand.nextInt(100));
        // T° from -20 to 120
        map.put("temperature", rand.nextInt(140) - 20);
        map.put("log", "HashMap value");

        return map;
    }
}
