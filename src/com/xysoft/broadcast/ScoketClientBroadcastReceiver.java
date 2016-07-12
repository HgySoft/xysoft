package com.xysoft.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ScoketClientBroadcastReceiver extends BroadcastReceiver {
	
	private Context context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		String message = intent.getStringExtra("message");
		String action = intent.getAction();
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		
	}

}
