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

package com.orange.mqttDeviceModePublishData;

import com.orange.mqttDeviceModePublishData.features.*;
import com.orange.mqttDeviceModePublishData.messages.BinaryEncodedMessage;
import com.orange.mqttDeviceModePublishData.messages.BinaryMessage;
import com.orange.mqttDeviceModePublishData.messages.HashMapMessage;
import com.orange.mqttDeviceModePublishData.messages.SimpleMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.Random;

import static com.orange.mqttDeviceModePublishData.features.MqttTopics.MQTT_TOPIC_PUBLISH_DATA;
import static com.orange.mqttDeviceModePublishData.features.MqttTopics.MQTT_TOPIC_PUBLISH_DATA_RAW_PREFIX;

/**
 * This sample will publish data for a device "device1", with several payloads,
 * manage incoming commands and parameters, and allow firmware updates.
 * At this end you can take a look in the data zone of live objects to see the data sent,
 * to send remote commands and parameters, or change the firmware.
 * First, change the constants belows, then run Main function.
 * Optionally, you can run several devices at once, or change the data type.
 **/
@SuppressWarnings("BusyWait")
public class MyDevice {
	// Connection parameters
	private static final String  API_KEY              = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // <-- REPLACE by YOUR API_KEY with "MQTT device" rights
	private static final String  DEVICE_URN           = "urn:lo:nsid:samples:device";       // in device mode : should be the syntax urn:lo:nsid:{namespace}:{id}
	private static final String  STREAM               = "device1stream";                    // timeseries this message belongs to
	private static final String  MODEL                = "devtype1";                         // data indexing model
	public  static final boolean SECURED              = true;                               // TLS-secured connection ?
	private static final boolean HANDLE_CONFIGURATION = false;                              // publish configuration and subscribe to updates ?
	private static final boolean HANDLE_COMMANDS      = false;                              // subscribe to commands ?
	private static final boolean HANDLE_FIRMWARE      = false;                              // publish firmware version and subscribe to updates ?
	/*
	 * Application mode: the device may also open a dedicated MQTT connection to subscribe to FiFo topics, like cloud applications do.
	 * Warning: the API-Key must have the additional BUS_R right. And therefore, SECURED must be set, or Live Objects will reject both connections.
	 */
	private static final boolean HANDLE_APPMODE       = false;                              // also act as an application consuming a FiFo (separate MQTT connection) ?
	public  static final String  HANDLE_APPMODE_FIFO  = "DeviceToDevice";                   // application-mode: FiFo name to consume (must exist in Live Objects)
	/*
	 * MSG_SRC=0: no data message sent
	 * MSG_SRC=1: simple data message built with objects
     * MSG_SRC=2: simple data message built with hash map
     * MSG_SRC=3: raw data message to be decoded by Live Objects as a float number
     * MSG_SRC=4: raw data message (image) NOT to be decoded by Live Objects
	 */
    private static final int     MSG_SRC              = 1;
    private static       boolean LOOP                 = true;                              // Run in a loop, or just send 1 message ?
	private static final int     NB_DEVICES_TO_CREATE = 1;                                 // why not simulating several devices at once ?

	private final int deviceId;

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < NB_DEVICES_TO_CREATE; i++) {
			final int devId = i + 1;
			new Thread(() -> new MyDevice(devId)).start();
			Thread.sleep(1000);
		}
	}

    @SuppressWarnings("ConstantConditions")
	public MyDevice(int deviceId) {
		this.deviceId = deviceId;
		try {
			MqttClient mqttClient = createAndConnectMqttClientAsDevice();
			System.out.println("Connected to Live Objects in Device Mode" + (SECURED ? " with TLS" : ""));

			if (HANDLE_CONFIGURATION) {
				DeviceConfig configHandler = new DeviceConfig(mqttClient);
				configHandler.publish();
				configHandler.subscribeToConfigChanges();
			}
			if (HANDLE_COMMANDS) {
				DeviceCommands commandsHandler = new DeviceCommands(mqttClient);
				commandsHandler.subscribeToCommands();
			}
			if (HANDLE_FIRMWARE) {
				DeviceFirmware firmwareHandler = new DeviceFirmware(mqttClient);
				firmwareHandler.publish();
				firmwareHandler.subscribeToResources();
			}
			if (HANDLE_APPMODE) {
				AppModeHandler appModeHandler = new AppModeHandler();
				appModeHandler.subscribeToFifo();
			}

			do {
				MqttMessage message = null;
				String topic = null;
                switch (MSG_SRC) {
					default:
					case 0:
						break;
                    case 1:
						message = new SimpleMessage().getMessage(STREAM, MODEL);
						topic = MQTT_TOPIC_PUBLISH_DATA;
						break;
					case 2:
						message = new HashMapMessage().getMessage(STREAM, MODEL);
						topic = MQTT_TOPIC_PUBLISH_DATA;
						break;
					case 3:
						message = new BinaryEncodedMessage().getMessage();
						topic = MQTT_TOPIC_PUBLISH_DATA_RAW_PREFIX + "/test";
						break;
					case 4:
						message = new BinaryMessage().getMessage();
						topic = MQTT_TOPIC_PUBLISH_DATA_RAW_PREFIX;
						break;
				}

				// send your message
				if (message != null) {
					mqttClient.publish(topic, message);
					System.out.println("Message published");
				}
				if (LOOP) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						LOOP = false;
					}
				}
			} while (LOOP);

			// disconnect
			mqttClient.disconnect();
			System.out.println("Disconnected from Live Objects");

		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}
	}

	public MqttClient createAndConnectMqttClientAsDevice() throws MqttException {
		return createAndConnectMqttClient("json+device",
				DEVICE_URN + deviceId,
				"mqtt.liveobjects.orange-business.com", // june 2021 : using new device-specific endpoint
				"DigiCertGlobalRootG2.crt"); // june 2021 : new device-specific endpoint certificate
	}
	public static MqttClient createAndConnectMqttClientAsApplication() throws MqttException {
		return createAndConnectMqttClient("application",
				"RandomClientId" + new Random().nextInt(),
				"liveobjects.orange-business.com",
				"DigiCertSHA2SecureServerCA.crt");
	}
	private static MqttClient createAndConnectMqttClient(String username, String clientId, String host, String certificate) throws MqttException {
		// create and fill the connection options
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		connOpts.setPassword(API_KEY.toCharArray());
		connOpts.setUserName(username);
		connOpts.setKeepAliveInterval(30);               // 30 seconds, to keep the connection with Live Objects

		String server;
		if (SECURED) {
			server = "ssl://" + host + ":8883";
			connOpts.setSocketFactory(SSLUtils.getLiveObjectsSocketFactory(certificate));
		}
		else {
			if (HANDLE_APPMODE) {
				System.err.println("Secure mode must be defined when an API-Key has more than \"device\" rights");
				System.exit(1);
			}
			server = "tcp://" + host + ":1883";
		}

		MqttClient mqttClient = new RegulatedMqttClient(server, clientId, new MemoryPersistence(), 500);
		// now connect to LO
		mqttClient.connect(connOpts);
		return mqttClient;
	}
}
