package com.xysoft.suport;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void showToast (String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	public void showNotification (Context context, int resource, int smallIcon, String title, String text) {
		Builder builder = new NotificationCompat.Builder(context);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setSmallIcon(smallIcon);
		Notification notification = builder.build();
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(resource, notification);
	}
	
	public void cancelNotification(int resource) {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.cancel(resource);
	}
}
