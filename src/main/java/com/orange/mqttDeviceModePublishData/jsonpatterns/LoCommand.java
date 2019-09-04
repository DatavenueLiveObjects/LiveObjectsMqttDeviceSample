package com.orange.mqttDeviceModePublishData.jsonpatterns;

/*
 * Copyright (C) 2019 Orange Business Services
 *
 * This software is distributed under the terms and conditions of the '3-Clause BSD'
 * license which can be found in the file 'LICENSE' in this package distribution
 * or at 'https://opensource.org/licenses/BSD-3-Clause'.
 */

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class LoCommand {
    /**
     * Structure to answer to command requests
     */
    public static class LoCommandResponse {
        public LoCommandResponse(Map<String, Object> res, Long cid) {
            this.res = res;
            this.cid = cid;
        }

        public Map<String, Object> res;    // List of answers (keys and values are free) to the command
        public Long cid;                   // Correlation ID
    }

    public String req;               // command request
    public Map<String, Object> arg;  // command parameters
    public Long cid;                 // Correlation ID

    @Override public String toString() {
        return "LoCommand{" +
                "req='" + req + '\'' +
                ", arg=" + arg +
                ", cid=" + cid +
                '}';
    }
}