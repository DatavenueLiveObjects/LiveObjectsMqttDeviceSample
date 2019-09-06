package com.orange.mqttDeviceModePublishData.features;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orange.mqttDeviceModePublishData.MyDevice;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Map;

public class AppModeHandler implements MqttCallback {
    private MqttClient mqttClient;

    public void subscribeToFifo() throws MqttException {
        mqttClient = MyDevice.createAndConnectMqttClient(MyDevice.ConnectionMode.APPLICATION);
        System.out.println("Connected to Live Objects in Application Mode" + (MyDevice.SECURED ? " with TLS" : ""));

        // register callback (to handle received messages)
        mqttClient.setCallback(this);

        // Subscribe to data
        mqttClient.subscribe("fifo/" + MyDevice.HANDLE_APPMODE_FIFO);
        System.out.println("Fifo " + MyDevice.HANDLE_APPMODE_FIFO + " subscribed.");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        Map<String, Object> msg = new Gson().fromJson(new String(mqttMessage.getPayload()), new TypeToken<Map<String, Object>>(){}.getType());
        System.out.println("FiFo message received: dated " + msg.get("timestamp")+ " : " + msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("FiFo changes: Connection lost");
        mqttClient.notifyAll();
    }
}
