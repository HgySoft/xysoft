package com.xysoft.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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
	
	public static void enableVisibly(Context context, int time) {
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
		context.startActivity(intent);
	}
	
	public static void turnonBluetooth(Activity activity, int requestCode) {
		Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		activity.startActivityForResult(intent, requestCode);
	}
	
	public static void turnoffBluetooth() {
		adapter.disable();
	}
	
	/**查找设备**/
	public static void findDevice() {
		assert (adapter != null);
		adapter.startDiscovery();
	}
	
	/**获取已绑定设备**/
	public static List<BluetoothDevice> getBluetoothDeviceList() {
		return new ArrayList<BluetoothDevice>(adapter.getBondedDevices());
	}
	
	
}
