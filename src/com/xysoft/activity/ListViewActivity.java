package com.xysoft.activity;

import com.example.app.R;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.BaseListAdapter;

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
	
	private ListView lv;
	private BaseListAdapter<String> baseListAdapter; 
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		getActionBar().setTitle("返回");
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		lv = (ListView) findViewById(R.id.listView2);
		baseListAdapter = new BaseListAdapter<String>(this, android.R.layout.simple_list_item_1) {
			@Override
			protected void initView(int position, View convertView, ViewGroup parent) {
				((TextView)(convertView)).setText(getItem(position));
			}
		};
		for (int i = 0; i < 5; i++) {
			baseListAdapter.add("这是第"+i+"Item");
		}
		lv.setAdapter(baseListAdapter);
		lv.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		showToast("你点击了"+baseListAdapter.getItem(position), Toast.LENGTH_SHORT);
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
	
}
