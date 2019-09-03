package com.orange.mqttDeviceModePublishData;

import com.orange.mqttDeviceModePublishData.messages.BinaryEncodedMessage;
import com.orange.mqttDeviceModePublishData.messages.BinaryMessage;
import com.orange.mqttDeviceModePublishData.messages.HashMapMessage;
import com.orange.mqttDeviceModePublishData.messages.SimpleMessage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * This sample will publish data for a device : device1
 * At this end you can take a look in the data zone of live objects to see the data sent.
 **/
public class DataPublisher {
	// Connection parameters
	private static final String  API_KEY   = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";           // <-- REPLACE by YOUR API_KEY!
	private static final String  CLIENT_ID = "urn:lo:nsid:samples:device1";                // in device mode : should be the syntax urn:lo:nsid:{namespace}:{id}
	private static final String  STREAM    = "device1stream";                              // timeseries this message belongs to
	private static final String  MODEL     = "devtype1";                                   // data indexing model
	private static final boolean SECURED   = true;                                         // TLS-secured connection ?

	/*
	 * MSG_SRC=1: simple message built with objects
     * MSG_SRC=2: simple message built with hash map
     * MSG_SRC=3: raw message to be decoded by Live Objects as a float number
     * MSG_SRC=4: raw message (image) NOT to be decoded by Live Objects
	 */
    private static final int     MSG_SRC   = 4;

	private static       boolean LOOP      = false;                                         // Run in a loop, or just send 1 message ?

	@SuppressWarnings("ConstantConditions")
    public static void main(String[] args) {
		try {
			// create and fill the connection options
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setPassword(API_KEY.toCharArray());
			connOpts.setUserName("json+device");             // needed to publish as a device

			String server;
			if (SECURED) {
				server = "ssl://liveobjects.orange-business.com:8883";
				connOpts.setSocketFactory(SSLUtils.getLiveObjectsSocketFactory());
            }
			else {
				server = "tcp://liveobjects.orange-business.com:1883";
			}

			// now connect to LO
			MqttClient sampleClient = new MqttClient(server, CLIENT_ID, new MemoryPersistence());
			sampleClient.connect(connOpts);
			System.out.println("Connected to Live Objects in Device Mode" + (SECURED ? " with TLS" : ""));

			do {
				MqttMessage message;
				String topic;
                switch (MSG_SRC) {
					default:
                    case 1:
						message = new SimpleMessage().getMessage(STREAM, MODEL);
						topic = "dev/data";
						break;
					case 2:
						message = new HashMapMessage().getMessage(STREAM, MODEL);
						topic = "dev/data";
						break;
					case 3:
						message = new BinaryEncodedMessage().getMessage();
						topic = "dev/data/raw/test";
						break;
					case 4:
						message = new BinaryMessage().getMessage();
						topic = "dev/data/raw/none";
						break;
				}

				// send your message
				sampleClient.publish(topic, message);
				System.out.println("Message published");
				if (LOOP) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						LOOP = false;
					}
				}
			} while (LOOP);

			// disconnect
			sampleClient.disconnect();
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
}
