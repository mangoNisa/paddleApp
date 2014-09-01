package de.eclipsemagazin.mqtt.push;

import android.app.Activity;
import android.content.ContextWrapper;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import android.util.Log;


public class PushCallback extends Activity implements MqttCallback {

	 ContextWrapper context;
	 SendNotification n;

    public PushCallback(ContextWrapper c) {
    	n = new SendNotification(c);
        this.context = c;
        
    }  

	@Override
    public void connectionLost(Throwable cause) {
		//reconnects though thread 
	}
	
	/*
	 * Sends message to the appropriate method in notification builder 
	 * 
	 * @param message The message received
	 * @param topic The topic name from which the message was received 
	 * 
	 * @return nothing
	 */
    @Override
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {

    	Log.e("Topic", topic.getName()); 
    	Log.e("Message", message.toString());
    	
       	if(topic.getName().equals("house/pill/notification/bad")) n.badPillNotfication(message.toString());
		else if(topic.getName().equals("house/pill/notification/good"))n.goodPillNotification(message.toString());
		else if(topic.getName().equals("house/pill/notification/alert"))n.alertNotification(message.toString());
		else if(topic.getName().equals("house/pill/notification/skip"))n.skipPillNotifcation(message.toString());
		else if(topic.getName().equals("house/pill/notification/snooze"))n.snoozePillNotifcation(message.toString());
		else if(topic.getName().equals("house/pill/schedule"))n.schedule(message.toString());
		else if(topic.getName().equals("house/pill/nextPill/response"))n.nextPill(message.toString());

    }

    @Override
    public void deliveryComplete(MqttDeliveryToken token) {
        //We do not need this because we do not publish here
    }
}
