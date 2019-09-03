package com.orange.mqttDeviceModePublishData.json.devData;

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