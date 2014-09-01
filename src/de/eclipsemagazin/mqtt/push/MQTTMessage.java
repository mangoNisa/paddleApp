package de.eclipsemagazin.mqtt.push;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

import android.util.Log;

public class MQTTMessage {
	static MQTTMessage instance;
	String topic;
	String message;
	MqttClient client;

	public void sendMessage(String topic, String key) throws Exception {
		this.topic = "house/pill/notification/" + topic;
		this.message = key;
		sendMessage();
	}

	public void request(String topic) {
		this.topic = "house/pill/" + topic;
		this.message = "";
		sendMessage();
		
	}

	/*
	 * Publishes the message
	 */
	public void sendMessage(){
		
		MqttMessage msg = new MqttMessage(message.getBytes());
		msg.setRetained(false);
		msg.setQos(1);

		MqttTopic t = client.getTopic(topic);
		Log.e("Sending Message", t.toString());
		try {
			t.publish(msg);
		} catch (MqttPersistenceException e) {
			Log.e("Sending Persistence Error", e.getMessage().toString());
		} catch (MqttException e) {
			Log.e("Sending Message Error", e.getMessage().toString());
		}
	}
	
	public static MQTTMessage getInstance(){
		if(instance == null){
			instance = new MQTTMessage();
		}
		return instance;
	}

	//runs thread to create connection
	public MQTTMessage() {
		Thread background = new Thread(new Runnable() {

			MemoryPersistence persistence = new MemoryPersistence();

			public void run() {

				try {

					client = new MqttClient(
							"tcp://gateway.cairnsolutions.com:1883",
							"client-android-90", persistence);

					MqttConnectOptions opts = new MqttConnectOptions();

					opts.setUserName("nabin");
					opts.setPassword("M4rk3r".toCharArray());
					// opts.setKeepAliveInterval(480);

					client.connect(opts);

					
					//client.disconnect();
				} catch (MqttException e) {
					Log.e("MQTTError Sending", e.getMessage().toString());
				}
			}

		});
		background.start();
	}

}