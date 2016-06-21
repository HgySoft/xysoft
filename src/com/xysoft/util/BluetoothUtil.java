package com.xysoft.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

public class BluetoothUtil {

	private static BluetoothAdapter adapter;
	
	static {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}
	public BluetoothUtil() {
		
	}
	
	public static Boolean isSuportBluetooth() {
		if(adapter != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public static Boolean getBluetoothStatus() {
		assert (adapter != null);
		return adapter.isEnabled();
	}
	
	public static void turnonBluetooth(Activity activity, int requestCode) {
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void turnoffBluetooth() {
		adapter.disable();
	}
	
}
