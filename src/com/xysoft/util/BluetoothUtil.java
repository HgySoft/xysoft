package com.xysoft.util;

import android.bluetooth.BluetoothAdapter;

public class BluetoothUtil {

	private BluetoothAdapter adapter;
	
	public BluetoothUtil() {
		adapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	public Boolean isSuportBluetooth() {
		if(adapter != null) {
			return true;
		}else {
			return false;
		}
	}
	
	private Boolean getBluetoothStatus() {
		assert (adapter != null);
		return adapter.isEnabled();
	}
	
}
