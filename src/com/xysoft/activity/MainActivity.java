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
		adapter.add(new User("����ListView������֪ͨ", "Ů", 0));
		adapter.add(new User("�Ƿ�֧������?", "Ů", 1));
		adapter.add(new User("�����Ƿ���?", "Ů", 2));
		adapter.add(new User("��������", "Ů", 3));
		adapter.add(new User("�ر�����", "Ů", 4));
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
				showToast("���豸��֧��������", Toast.LENGTH_SHORT);
			}else {
				if(!penClientCtrl.isConnected()) {
					showToast("�������������豸...", Toast.LENGTH_SHORT);
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
			showToast(String.format("����: %s, �Ա�%s, ���䣺%d", user.getName(), user.getSex(), user.getAge()), Toast.LENGTH_SHORT);
			showNotification(this, R.layout.activity_main, R.drawable.ic_launcher, "����һ������Ϣ", "����");
			intent = new Intent(this, ListViewActivity.class);
			startActivity(intent);
			break;
		case 1:
			showToast("�Ƿ�֧��������"+BluetoothUtil.isSuportBluetooth(), Toast.LENGTH_SHORT);
			break;
		case 2:
			showToast("�����Ƿ�����"+BluetoothUtil.getBluetoothStatus(), Toast.LENGTH_SHORT);
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
			showToast("�ٰ�һ���˳�����", Toast.LENGTH_SHORT);
			lastClickTime = System.currentTimeMillis();
		}else {
			long currentClickTime = System.currentTimeMillis();
			if(currentClickTime - lastClickTime <= 1000) {
				finish();
			}else {
				lastClickTime = currentClickTime;
				showToast("�ٰ�һ���˳�����", Toast.LENGTH_SHORT);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_BLUETOOTH_TURNON:
				showToast("�����ѿ���", Toast.LENGTH_SHORT);
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
