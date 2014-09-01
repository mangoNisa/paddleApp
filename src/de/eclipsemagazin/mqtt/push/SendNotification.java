package de.eclipsemagazin.mqtt.push;

import android.app.Notification;
import android.content.ContextWrapper;
import android.content.Intent;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

public class SendNotification {

	private ContextWrapper context;
	Intent intent;
	GridAdapter grid = GridAdapter.getInstance();

	public SendNotification(ContextWrapper c) {
		this.context = c;
		intent = new Intent(this.context, Schedule.class);
	}

	/*
	 * Uses the badPill method in GridAdapter to changed the image of the pill
	 * then notifies the user that wrong pill had been taken.
	 * 
	 * @author Nisa
	 * 
	 * @param message Key of dictionary representing a pill compartment
	 * 
	 * @return nothing
	 */
	public void badPillNotfication(String message) {

		grid.changePill(Integer.parseInt(message), 2);

		PendingIntent pIntent = PendingIntent
				.getActivity(context, 0, intent, 0);

		Notification n = new Notification.Builder(context)
				.setContentTitle("Pill Warning!")
				.setContentText("Wrong pill opened")
				.setSmallIcon(R.drawable.nicon).setContentIntent(pIntent)
				.setAutoCancel(true).build();
		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_VIBRATE;
		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0, n);
	}

	/*
	 * Uses the goodPill method in GridAdapter to changed the image of the pill
	 * then notifies the user that pill had been taken.
	 * 
	 * @author Nisa
	 * 
	 * @param message Key of dictionary representing a pill compartment
	 * 
	 * @return nothing
	 */
	public void goodPillNotification(String message) {

		grid.changePill(Integer.parseInt(message), 1);

		PendingIntent pIntent = PendingIntent
				.getActivity(context, 1, intent, 0);

		Notification n = new Notification.Builder(context)
				.setContentTitle("Pill taken")
				.setContentText("Correct pill has been taken")
				.setSmallIcon(R.drawable.nicon).setContentIntent(pIntent)
				.setAutoCancel(true).build();
		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_VIBRATE;
		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(1, n);
	}

	/*
	 * Alerts user with notification that pill is due
	 * 
	 * @param message Key of dictionary representing a pill compartment
	 * 
	 * @return nothing
	 */
	public void alertNotification(String message) {

		String time = grid.alertPill(Integer.parseInt(message));
		grid.setKey(Integer.parseInt(message));
		Intent snoozeReceive = new Intent(context, SnoozeButtonListener.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 2,
				snoozeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
		// PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent,
		// 0);

		Notification n = new Notification.Builder(context)
				.setContentTitle("Take " + time + " pills")
				.setContentText("Time to take " + time + " pill")
				.setSmallIcon(R.drawable.nicon)
				.addAction(R.drawable.snoozecon, " Snooze", pIntent)
				.setAutoCancel(true).setAutoCancel(true).build();
		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_VIBRATE;
		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(2, n);
	}

	/*
	 * Uses the skipPill method in GridAdapter to changed the image of the pill
	 * then notifies the user that pill has been skipped
	 * 
	 * @author Nisa
	 * 
	 * @param message Key of dictionary representing a pill compartment
	 * 
	 * @return nothing
	 */
	public void skipPillNotifcation(String message) {

		// grid.changePill(Integer.parseInt(message), 0);

		PendingIntent pIntent = PendingIntent
				.getActivity(context, 3, intent, 0);

		Notification n = new Notification.Builder(context)
				.setContentTitle("Pill skipped").setSmallIcon(R.drawable.nicon)
				.setContentIntent(pIntent).setAutoCancel(true).build();
		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_VIBRATE;
		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(3, n);
	}

	/*
	 * Uses the goodPill method in GridAdapter to changed the image of the pill
	 * then notifies the user that pill had been taken.
	 * 
	 * @param message Key of dictionary representing a pill compartment
	 * 
	 * @return nothing
	 */
	public void snoozePillNotifcation(String message) {
		grid.saveSnooze(message);

		PendingIntent resetIntent = PendingIntent.getActivity(context, 4,
				intent, 0);

		Notification n = new Notification.Builder(context)
				.setContentTitle("Pill snoozed").setSmallIcon(R.drawable.nicon)
				.setContentIntent(resetIntent).setAutoCancel(true).build();
		n.defaults |= Notification.DEFAULT_SOUND;
		n.defaults |= Notification.DEFAULT_VIBRATE;
		final NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(4, n);

	}

	/*
	 * Forwards the message to setPills in gridView to parse
	 * 
	 * @param message JSON Dictionary of schedule
	 * 
	 * @return nothing
	 */
	public void schedule(String message) {
		grid.setPills(message);
	}

	/*
	 * Forwards the message to setNextTime in gridView to get time
	 * 
	 * @param message id of next pill
	 * 
	 * @return nothing
	 */
	public void nextPill(String message) {
		grid.setNextTime(message);
	}

}
