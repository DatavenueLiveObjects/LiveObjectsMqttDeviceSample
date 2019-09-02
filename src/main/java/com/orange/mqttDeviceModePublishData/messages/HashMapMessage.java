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
