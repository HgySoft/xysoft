package com.xysoft.activity;

import com.example.app.R;
import com.xysoft.broadcast.PenBroadcastReceiver;
import com.xysoft.common.PenCtrlConst.Broadcast;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.PenClientCtrl;
import com.xysoft.util.BluetoothUtil;
import com.xysoft.zdy.surfaceview.SampleView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	
	private Intent intent;
	private PenClientCtrl penClientCtrl;
	private SampleView sampleView;
	private PenBroadcastReceiver penBroadcastReceiver;
	public static final int REQUEST_BLUETOOTH_TURNON = 1;
	private static final int REQUEST_CONNECT_PEN = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
	}
	
	private void initUI() {
		sampleView = new SampleView(this);
		penClientCtrl = PenClientCtrl.getInstance(getApplicationContext());
		penBroadcastReceiver = new PenBroadcastReceiver(this, penClientCtrl, sampleView);
		setContentView(sampleView);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter( Broadcast.ACTION_PEN_MESSAGE );
		filter.addAction( Broadcast.ACTION_PEN_DOT );
		filter.addAction( "firmware_update" );
		registerReceiver(penBroadcastReceiver, filter);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(penBroadcastReceiver);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_activity_menu, menu); 
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_connectPen:
			if(!BluetoothUtil.isSuportBluetooth()) {
				showToast("该设备不支持蓝牙！", Toast.LENGTH_SHORT);
			}else {
				if(!penClientCtrl.isConnected()) {
					showToast("正在搜索蓝牙设备...", Toast.LENGTH_SHORT);
					intent = new Intent(this, BluetoothDeviceActivity.class);
					startActivityForResult(intent, REQUEST_CONNECT_PEN);
				}else {
					penClientCtrl.disconnect();
				}
			}
			return true;
		case R.id.menu_setting: 
			intent = new Intent(this, ListViewActivity.class);
			startActivityForResult(intent, Activity.RESULT_OK);
			return true;
		case R.id.menu_red: 
			penClientCtrl.reqSetupPenTipColor(-444912);//红
			return true;
		case R.id.menu_blue: 
			penClientCtrl.reqSetupPenTipColor(Color.BLUE);//蓝
			return true;
		case R.id.menu_yellow: 
			penClientCtrl.reqSetupPenTipColor(-275674);//黄
			return true;
		case R.id.menu_pink: 
			penClientCtrl.reqSetupPenTipColor(-57212);//粉
			return true;
		case R.id.menu_mint: 
			penClientCtrl.reqSetupPenTipColor(-14163768);//绿
			return true;
		case R.id.menu_violet: 
			penClientCtrl.reqSetupPenTipColor(-6537267);//紫
			return true;
		case R.id.menu_gray: 
			penClientCtrl.reqSetupPenTipColor(-4342339);//灰
			return true;
		case R.id.menu_black: 
			penClientCtrl.reqSetupPenTipColor(-16777216);//黑
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private long lastClickTime = 0;
	@Override
	public void onBackPressed() {
		if(lastClickTime<=0) {
			showToast("再按一次退出程序", Toast.LENGTH_SHORT);
			lastClickTime = System.currentTimeMillis();
		}else {
			long currentClickTime = System.currentTimeMillis();
			if(currentClickTime - lastClickTime <= 1000) {
				finish();
			}else {
				lastClickTime = currentClickTime;
				showToast("再按一次退出程序", Toast.LENGTH_SHORT);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_BLUETOOTH_TURNON:
				showToast("蓝牙已开启", Toast.LENGTH_SHORT);
				break;
			case REQUEST_CONNECT_PEN:
				if ( resultCode == Activity.RESULT_OK ) {
					String address = null;
					if ( (address = data.getStringExtra( BluetoothDeviceActivity.EXTRA_DEVICE_ADDRESS )) != null ) {
						penClientCtrl.connect( address );
					}
				}
				break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
