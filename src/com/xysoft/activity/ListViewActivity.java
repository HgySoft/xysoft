package com.xysoft.activity;

import com.example.app.R;
import com.xysoft.entity.User;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.BaseListAdapter;
import com.xysoft.util.BluetoothUtil;
import com.xysoft.util.InjectView;
import com.xysoft.util.Injector;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewActivity extends BaseActivity implements OnItemClickListener{
	
	@InjectView(R.id.listView2)
	private ListView lv;
	private BaseListAdapter<User> adapter; 
	public static final int REQUEST_BLUETOOTH_TURNON = 1;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		getActionBar().setTitle("返回");
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		initUI();
	}
	
	private void initUI() {
		Injector.get(this).inject();
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
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		User user = adapter.getItem(position);
		switch (user.getAge()) {
		case 0:
			showToast(String.format("名字: %s, 性别：%s, 年龄：%d", user.getName(), user.getSex(), user.getAge()), Toast.LENGTH_SHORT);
			showNotification(this, R.layout.activity_main, R.drawable.ic_launcher, "你有一条新消息", "哈哈");
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_BLUETOOTH_TURNON:
			showToast("蓝牙已开启", Toast.LENGTH_SHORT);
			break;
		}
	}
	
	
	
}
