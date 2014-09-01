package de.eclipsemagazin.mqtt.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {
    public static final String SERVICE_CLASSNAME = "de.eclipsemagazin.mqtt.push.MQTTService";
	EditText id;
	static String clientID;
	EditText pass;
	Button login;
    GridAdapter grid = GridAdapter.getInstance();
    MQTTMessage message = MQTTMessage.getInstance() ;

	 	@Override
	    protected void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_login);
 
	        id =(EditText)findViewById(R.id.txtId);
	        pass = (EditText)findViewById(R.id.txtPass);
	        login = (Button)findViewById(R.id.btnLogin);
	        login.setOnClickListener(this);
	        
	        final Intent intent = new Intent(this, MQTTService.class);
		    startService(intent); 
	}

/*
 * Checks if getPills is empty, if it is the schedule has not been received and a toast 
 * informing the user is sent. 
 * Otherwise the the Main activity is opened and login is closed.
 * 
 * @param View creates the view
 * 
 */
	 public void onClick(View v) {
		//String i = id.getText().toString();
		//String pa = pass.getText().toString();
				
		//if(pa.equals("welcome")){
	
			message.request("schedule/request");

			if(grid.getPillList().isEmpty()){
				Toast.makeText(this, "No Schedule Recived" , Toast.LENGTH_LONG).show();
			}else{
				//message.request("nextPill/request"); //gets the time of the next pill 
				Intent in = new Intent(this, Schedule.class);
				startActivity(in);
			    finish();
			}
		//}
		
	}
	
}
