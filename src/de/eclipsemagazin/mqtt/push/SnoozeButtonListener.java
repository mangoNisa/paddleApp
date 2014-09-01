package de.eclipsemagazin.mqtt.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/*******
 * 
 * @author Nisa
 * 
 * Listens for snooze button being press 
 * in the alert notification.
 *
 */
public class SnoozeButtonListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		GridAdapter grid = GridAdapter.getInstance();
		grid.snooze();
	}
	
}