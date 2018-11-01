package com.freddomoura.android.babytimer.feature.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.freddomoura.android.babytimer.feature.BabyTimerPrincipal;
import com.freddomoura.android.babytimer.feature.R;
import com.freddomoura.android.babytimer.feature.util.ServiceUtil;

public class ChronoAlarmService extends ServiceUtil {

	private NotificationManager mNM;

	private static final String CHANNEL_ID = "NOTIFICATION_CHANNEL_ID";
	
	public static boolean on;
	/**
	 * This is the object that receives interactions from clients. See
	 * RemoteService for a more complete example.
	 */
	private final IChronoAlarmBinder mBinder = new ChronoAlarmBinder();

	@Override
	public IBinder onBind(Intent pArg0) {
		((ChronoAlarmBinder)mBinder).setContext(this.getApplicationContext());
		return mBinder;
	}

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// criando o canal de notificacao
		createNotificationChannel();

		// Display a notification about us starting. We put an icon in the
		// status bar.
		showNotification();
		on = true;
	}

	@Override
	public void onDestroy() {
		
		on = false;
		// Cancel the persistent notification.
		mNM.cancel(R.string.alarm_started);

		// Tell the user we stopped.
		Toast.makeText(this, R.string.alarm_stopped, Toast.LENGTH_SHORT).show();
		
		((ChronoAlarmBinder)mBinder).destroy();
		
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification
		CharSequence text = getText(R.string.alarm_started);

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, BabyTimerPrincipal.class), 0);

		// Set the icon, scrolling text and timestamp
		//Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ID);
		builder.setAutoCancel(false);
		builder.setTicker("this is ticker text");
		builder.setContentTitle("WhatsApp Notification");
		builder.setContentText("You have a new message");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentIntent(contentIntent);
		builder.setOngoing(true);
		builder.setSubText("This is subtext...");   //API level 16
		builder.setNumber(100);
		builder.build();

		Notification notification = builder.build();
		mNM.notify(R.string.alarm_started, notification);



		// Set the info for the views that show in the notification panel.
		//CharSequence info = getText(R.string.alarm_info);
		//notification.setLatestEventInfo(this, getText(R.string.alarm_label), info, contentIntent);

		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		//mNM.notify(R.string.alarm_started, notification);
	}

	private void createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			//CharSequence name = getString(R.string.channel_name);
			//String description = getString(R.string.channel_description);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Nome do canal de notificacao", importance);
			channel.setDescription("Descricao do canal de notificacao");
			// Register the channel with the system; you can't change the importance
			// or other notification behaviors after this
			mNM.createNotificationChannel(channel);
		}
	}

	@Override
	public int executeCommand(Intent pIntent, int pFlags, int pStartId) {
		// TODO Auto-generated method stub
		return 0;
	}

}
