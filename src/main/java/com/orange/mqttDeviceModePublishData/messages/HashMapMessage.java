package com.orange.mqttDeviceModePublishData.messages;

/*
 * Copyright (C) 2019 Orange Business Services
 *
 * This software is distributed under the terms and conditions of the '3-Clause BSD'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */

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
