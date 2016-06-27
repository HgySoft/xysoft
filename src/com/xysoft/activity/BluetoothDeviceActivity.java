package com.xysoft.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;
import com.xysoft.suport.BaseActivity;
import com.xysoft.suport.BaseListAdapter;
import com.xysoft.util.BluetoothUtil;
import com.xysoft.util.InjectView;
import com.xysoft.util.Injector;

public class BluetoothDeviceActivity extends BaseActivity{
	
	@InjectView(R.id.bluetoothdevice)
	private ListView bluetoothdevice;
	@InjectView(R.id.bluetoothdevice_pb)
	private ProgressBar mprogressBar;
	public static final int REQUEST_BLUETOOTH_TURNON = 1;
	public static String EXTRA_DEVICE_ADDRESS = "device_address";
	private List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
	private List<BluetoothDevice> mBondedDeviceList = new ArrayList<BluetoothDevice>();
	private BaseListAdapter<BluetoothDevice> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothdevice);
		initUI();
		
        IntentFilter filter = new IntentFilter();
        //开始查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //结束查找
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //查找设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //设备扫描模式改变
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //绑定状态
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //注册广播
        registerReceiver(receiver, filter);
        //开启蓝牙
        BluetoothUtil.turnonBluetooth(this, REQUEST_BLUETOOTH_TURNON);
        //开始扫描
        adapter.refresh(mDeviceList);
        BluetoothUtil.findDevice();
        bluetoothdevice.setOnItemClickListener(bindDeviceClick);
	}
	
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if( BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action) ) {
                //setProgressBarIndeterminateVisibility(true);
            	mprogressBar.setVisibility(View.VISIBLE);
                //初始化数据列表
                mDeviceList.clear();
                adapter.notifyDataSetChanged();
            }
            else if( BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //setProgressBarIndeterminateVisibility(false);
            	mprogressBar.setVisibility(View.INVISIBLE);
            }
            else if( BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //找到一个，添加一个
                mDeviceList.add(device);
                adapter.notifyDataSetChanged();
            }
            else if( BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
               int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if( scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                    //setProgressBarIndeterminateVisibility(true);
                	mprogressBar.setVisibility(View.VISIBLE);
                }
                else {
                    //setProgressBarIndeterminateVisibility(false);
                	mprogressBar.setVisibility(View.INVISIBLE);
                }
            }
            else if( BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action) ) {
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if( remoteDevice == null ) {
                    showToast("no device", Toast.LENGTH_SHORT);
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE,0);
                if( status == BluetoothDevice.BOND_BONDED) {
                    showToast("绑定 " + remoteDevice.getName(), Toast.LENGTH_SHORT);
                }
                else if( status == BluetoothDevice.BOND_BONDING){
                    showToast("正在绑定 " + remoteDevice.getName(), Toast.LENGTH_SHORT);
                }
                else if(status == BluetoothDevice.BOND_NONE){
                    showToast("没有绑定 " + remoteDevice.getName(), Toast.LENGTH_SHORT);
                }
            }
        }
    };
	
	private void initUI() {
		Injector.get(this).inject();
		//getActionBar().setTitle("返回");
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		adapter = new BaseListAdapter<BluetoothDevice>(this, android.R.layout.simple_list_item_2) {
			@Override
			protected void initView(int position, View convertView, ViewGroup parent) {
		        View itemView = convertView;
		        //复用View，优化性能
		        if( itemView == null) {
		            itemView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
		        }
		        TextView line1 = (TextView) itemView.findViewById(android.R.id.text1);
		        TextView line2 = (TextView) itemView.findViewById(android.R.id.text2);
		        //获取对应的蓝牙设备
		        BluetoothDevice device = (BluetoothDevice) getItem(position);
		        //显示名称
		        line1.setText(device.getName());
		        //显示地址
		        line2.setText(device.getAddress());
			}
		};
		bluetoothdevice.setAdapter(adapter);
		bluetoothdevice.setOnItemClickListener(bindDeviceClick);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bluetoothdevice_activity_menu, menu); 
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.refresh_device:
			adapter.refresh(mDeviceList);
			break;
		case R.id.enable_visiblity:
			BluetoothUtil.enableVisibly(this, 300);
			break;
		case R.id.find_device:
			//查找设备
			mprogressBar.setVisibility(View.VISIBLE);
            adapter.refresh(mDeviceList);
            BluetoothUtil.findDevice();
            bluetoothdevice.setOnItemClickListener(bindDeviceClick);
			break;
		case R.id.bonded_device:
            //查看已绑定设备
			mprogressBar.setVisibility(View.INVISIBLE);
            mBondedDeviceList = BluetoothUtil.getBluetoothDeviceList();
            adapter.refresh(mBondedDeviceList);
            bluetoothdevice.setOnItemClickListener(null);
			break;
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_CANCELED) finish();
		if(requestCode != REQUEST_BLUETOOTH_TURNON) finish();
	}
	
    @SuppressLint("NewApi")
	private AdapterView.OnItemClickListener bindDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = mDeviceList.get(i);
            device.createBond();
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, device.getAddress());
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
	
	
	
}
