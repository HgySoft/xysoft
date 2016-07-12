package com.xysoft.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScoketServerBroadcastReceiver extends BroadcastReceiver{
	
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		
	}

}
