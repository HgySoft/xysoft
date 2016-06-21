package com.example.app;

import com.xysoft.entity.User;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.BaseListAdapter;
import com.xysoft.util.BluetoothUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu); 
		return true;
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
			BluetoothUtil.turnonBluetooth(this, 0);
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
		if(requestCode == 0) showToast("成功开启", Toast.LENGTH_SHORT);
		else showToast("开启失败", Toast.LENGTH_SHORT);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
