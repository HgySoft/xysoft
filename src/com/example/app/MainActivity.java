package com.example.app;

import com.xysoft.entity.User;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.BaseListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
		adapter.add(new User("小楚", "女", 25));
		adapter.add(new User("小强", "男", 25));
		lv.setOnItemClickListener(this);
		cancelNotification(R.layout.activity_main);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		User user = adapter.getItem(position);
		showToast(String.format("名字: %s, 性别：%s, 年龄：%d", user.getName(), user.getSex(), user.getAge()));
		showNotification(this, R.layout.activity_main, R.drawable.ic_launcher, "你有一条新消息", "哈哈");
		intent = new Intent(this, ListViewActivity.class);
		startActivity(intent);
	}
	
	private long lastClickTime = 0;
	@Override
	public void onBackPressed() {
		if(lastClickTime<=0) {
			showToast("再按一次退出程序");
			lastClickTime = System.currentTimeMillis();
		}else {
			long currentClickTime = System.currentTimeMillis();
			if(currentClickTime - lastClickTime <= 1000) {
				finish();
			}else {
				lastClickTime = currentClickTime;
				showToast("再按一次退出程序");
			}
		}
	}
	
	
	
	
	
}
