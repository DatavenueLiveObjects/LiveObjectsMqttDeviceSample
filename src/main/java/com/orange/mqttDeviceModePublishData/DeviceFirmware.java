package com.orange.mqttDeviceModePublishData;

import static com.orange.mqttDeviceModePublishData.MqttTopics.MQTT_TOPIC_SUBSCRIBE_RESOURCE;
import static com.orange.mqttDeviceModePublishData.json.devData.LoResource.Resource;
import static com.orange.mqttDeviceModePublishData.json.devData.LoResource.LoResourceUpdate;
import static com.orange.mqttDeviceModePublishData.json.devData.LoResource.LoResourceResponse;
import static com.orange.mqttDeviceModePublishData.json.devData.LoResource.LoResourceResponseError;
import static com.orange.mqttDeviceModePublishData.json.devData.LoResource.RESPONSE_CODE;

import com.google.gson.Gson;
import com.orange.mqttDeviceModePublishData.json.devData.LoResource;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class DeviceFirmware implements MqttCallback {
    public static final int QOS = 1;
    private final MqttClient mqttClient;

    private Resource fw1 = new Resource("18.04LTS", new HashMap<>());
    private Resource fw2 = new Resource("v1.1", null);

    private boolean SUCCESS_WHEN_APPLYING_FIRMWARE = true;  // can be set to false to generate a failure during update

    public DeviceFirmware(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
        fw1.m.put("name", "Kubuntu");
    }

    public void publish() throws MqttException {
        LoResource resources = new LoResource();

        resources.rsc.put("fw1", fw1);
        resources.rsc.put("fw2", fw2);

        String resourcesJson = new Gson().toJson(resources);
        System.out.println("Publishing resources message: " + resourcesJson);

        MqttMessage message = new MqttMessage(resourcesJson.getBytes());
        message.setQos(QOS);

        mqttClient.publish(MqttTopics.MQTT_TOPIC_PUBLISH_RESOURCE, message);
        System.out.println("Resources published");
    }

    public void subscribeToResources() throws MqttException {
        // register callback (to handle received commands)
        mqttClient.setCallback(this);

        // Subscribe to data
        mqttClient.subscribe(MQTT_TOPIC_SUBSCRIBE_RESOURCE);
        System.out.println("Device resource updates subscribed.");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) {
        // parse message as resource update
        LoResourceUpdate update = new Gson().fromJson(new String(mqttMessage.getPayload()), LoResourceUpdate.class);
        System.out.println("Device resource update received: " + new Gson().toJson(update));

        RESPONSE_CODE result = null;
        String errorString = null;

        System.out.println("Downloading " + update.m.get("uri"));
        byte[] binary = null;
        try {
            binary = IOUtils.toByteArray(new URL(update.m.get("uri")));
        } catch (IOException e) {
            result = RESPONSE_CODE.INVALID_RESOURCE;
            errorString = e.getMessage();
        }
        System.out.println("Checking binary with size " + update.m.get("size"));
        if (result == null && binary.length != Integer.parseInt(update.m.get("size"))) {
            result = RESPONSE_CODE.INVALID_RESOURCE;
            errorString = "downloaded size : " + binary.length;
        }
        System.out.println("Checking binary with MD5 " + update.m.get("md5"));
        if (result == null) {
            try {
                String md5 = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(binary));
                if (!md5.equalsIgnoreCase(update.m.get("md5"))) {
                    result = RESPONSE_CODE.INVALID_RESOURCE;
                    errorString = "downloaded MD5 : " + md5;
                }
            } catch (NoSuchAlgorithmException e) {
                result = RESPONSE_CODE.INTERNAL_ERROR;
                errorString = e.getMessage();
            }
        }
        Resource rsc = null;
        if (result == null) {
            switch (update.id) {
                case "fw1": rsc = fw1; break;
                case "fw2": rsc = fw2; break;
                default:
                    result = RESPONSE_CODE.UNKNOWN_RESOURCE;
                    errorString = "Unknown firmware " + update.id;
                    break;
            }
        }
        if (result == null) {
            if (rsc.v.equals(update.old)) {
                System.out.println("Applying " + update.new_ + " on " + update.id);
                if (SUCCESS_WHEN_APPLYING_FIRMWARE) {
                    rsc.v = update.new_;
                    result = RESPONSE_CODE.OK;
                }
                else {
                    result = RESPONSE_CODE.INTERNAL_ERROR;
                    errorString = "Failing to apply firmware because of ...";
                }
            }
            else {
                result = RESPONSE_CODE.WRONG_SOURCE_VERSION;
            }
        }

        LoResourceResponse response = new LoResourceResponse(result, update.cid);
        LoResourceResponseError responseError = (errorString != null ? new LoResourceResponseError("Generic Error", errorString) : null);

        new Thread(() -> {
            try {

                String responseJson = new Gson().toJson(response);
                System.out.println("Publishing resource update result message: " + responseJson);

                MqttMessage message = new MqttMessage(responseJson.getBytes());
                message.setQos(QOS);

                mqttClient.publish(MqttTopics.MQTT_TOPIC_RESPONSE_RESOURCE, message);
                System.out.println("Resource update result published");

                if (response.res == RESPONSE_CODE.OK) {
                    publish();
                }
                else if (responseError != null) {
                    responseJson = new Gson().toJson(responseError);
                    System.out.println("Publishing resource update result error message: " + responseJson);

                    message = new MqttMessage(responseJson.getBytes());
                    message.setQos(QOS);

                    mqttClient.publish(MqttTopics.MQTT_TOPIC_RESPONSE_RESOURCE_ERROR, message);
                    System.out.println("Resource update result error published");
                }
            } catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            }
        }).start();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Device update requests: Connection lost");
        mqttClient.notifyAll();
    }
}
