package com.xysoft.activity;

import com.example.app.R;
import com.xysoft.entity.User;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.BaseListAdapter;
import com.xysoft.suport.PenClientCtrl;
import com.xysoft.util.BluetoothUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnItemClickListener {
	
	private Intent intent;
	private ListView lv;
	private BaseListAdapter<User> adapter;
	private PenClientCtrl penClientCtrl;
	public static final int REQUEST_BLUETOOTH_TURNON = 1;
	private static final int REQUEST_CONNECT_PEN = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.listView1);
		
		adapter = new BaseListAdapter<User>(this, android.R.layout.simple_list_item_1) {
			@Override
			protected void initView(int position, View convertView, ViewGroup parent) {
				TextView tv = (TextView) convertView;
				tv.setText(getItem(position).toString());
			}
		};
		lv.setAdapter(adapter);
		adapter.add(new User("跳到ListView并弹出通知", "女", 0));
		adapter.add(new User("是否支持蓝牙?", "女", 1));
		adapter.add(new User("蓝牙是否开启?", "女", 2));
		adapter.add(new User("开启蓝牙", "女", 3));
		adapter.add(new User("关闭蓝牙", "女", 4));
		lv.setOnItemClickListener(this);
		cancelNotification(R.layout.activity_main);
		penClientCtrl = PenClientCtrl.getInstance(getApplicationContext());
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		User user = adapter.getItem(position);
		switch (user.getAge()) {
		case 0:
			showToast(String.format("名字: %s, 性别：%s, 年龄：%d", user.getName(), user.getSex(), user.getAge()), Toast.LENGTH_SHORT);
			showNotification(this, R.layout.activity_main, R.drawable.ic_launcher, "你有一条新消息", "哈哈");
			intent = new Intent(this, ListViewActivity.class);
			startActivity(intent);
			break;
		case 1:
			showToast("是否支持蓝牙："+BluetoothUtil.isSuportBluetooth(), Toast.LENGTH_SHORT);
			break;
		case 2:
			showToast("蓝牙是否开启："+BluetoothUtil.getBluetoothStatus(), Toast.LENGTH_SHORT);
			break;
		case 3:
			BluetoothUtil.turnonBluetooth(this, REQUEST_BLUETOOTH_TURNON);
			break;
		case 4:
			BluetoothUtil.turnoffBluetooth();
			break;
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
