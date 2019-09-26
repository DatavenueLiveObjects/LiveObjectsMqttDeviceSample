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
        mqttClient = MyDevice.createAndConnectMqttClient(MyDevice.ConnectionMode.APPLICATION);
        System.out.println("Connected to Live Objects in Application Mode" + (MyDevice.SECURED ? " with TLS" : ""));

        mqttClient.subscribe("fifo/" + MyDevice.HANDLE_APPMODE_FIFO, this::messageArrived);
        System.out.println("Fifo " + MyDevice.HANDLE_APPMODE_FIFO + " subscribed.");
    }

    private void messageArrived(String s, MqttMessage mqttMessage) {
        Map<String, Object> msg = new Gson().fromJson(new String(mqttMessage.getPayload()), new TypeToken<Map<String, Object>>(){}.getType());
        System.out.println("FiFo message received: dated " + msg.get("timestamp")+ " : " + msg);
    }
}
