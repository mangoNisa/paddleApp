package de.eclipsemagazin.mqtt.push;



import android.app.Service;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

public class MQTTService extends Service {

    public static final String BROKER_URL = "tcp://gateway.cairnsolutions.com:1883";

    public static final String clientId = "android-client90";
    ContextWrapper c = this;
    private MqttClient mqttClient;
    
   
    public IBinder onBind(Intent intent) {
    	Log.e("Before Callback", "Connecting...");

        return null;
    }
   
    /*
     * Creates thread to run in background creating mqtt connection
     * and subscribing to topics
     */
    @Override
	public void onStart(Intent intent, int startId) {
    	
    	Thread background = new Thread(new Runnable() {
    	
	        public void run() {
	        	Looper.prepare();
	        	 try{

	            	
	            	MqttClient mqttClient = new MqttClient(BROKER_URL, "nisa", new MemoryPersistence());
	                MqttConnectOptions opts = new MqttConnectOptions();
	    	    	opts.setKeepAliveInterval(6000);
	    	    	opts.setUserName("nabin");
	    	    	opts.setPassword("M4rk3r".toCharArray());	

	    	    	
	    	    	
	    	    	Log.e("Before Callback", "Connecting..."); 
	                mqttClient.setCallback(new PushCallback(c));

	                mqttClient.connect(opts);

	            		mqttClient.subscribe("house/pill/notification/bad");
	            		mqttClient.subscribe("house/pill/notification/good");
	            		mqttClient.subscribe("house/pill/notification/skip");
	            		mqttClient.subscribe("house/pill/notification/alert");
	            		mqttClient.subscribe("house/pill/notification/snooze");
	            		mqttClient.subscribe("house/pill/schedule");
	            		mqttClient.subscribe("house/pill/nextPill/response");
	            		mqttClient.subscribe("house/pill/schedule/request");
	            		
	            		Log.e("CONNECTED!", "COOOONNECTED!!!!!");
	            }catch(MqttException e){
	            	Log.e("Internet Error", "No connection available  " + e.getMessage() );
	            }catch(Exception e){
	                Log.e("ERRRRRORRRRR ", e.getMessage());
	            }
	        }

			        
    });  
	background.start();
    }
    	
	@Override
	public void onDestroy(){
		try {
			Log.e("Disconnecting!", "onDestroy Disconnecting");
			mqttClient.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			System.out.println(e + " Error");
		}
	}

}
