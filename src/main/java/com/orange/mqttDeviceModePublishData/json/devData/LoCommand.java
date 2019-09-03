package com.orange.mqttDeviceModePublishData.json.devData;

import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class LoCommand {
    public static class LoCommandResponse {
        public LoCommandResponse(Map<String, Object> res, Long cid) {
            this.res = res;
            this.cid = cid;
        }

        public Map<String, Object> res;
        public Long cid;
    }

    public String req;
    public Map<String, Object> arg;
    public Long cid;

    @Override public String toString() {
        return "DeviceCommand{" +
                "req='" + req + '\'' +
                ", arg=" + arg +
                ", cid=" + cid +
                '}';
    }
}