package com.xysoft.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.xysoft.common.ScoketConst;
import com.xysoft.common.ScoketConst.Clientcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ScoketClientService extends Service {
	
	private String ipAddress;
	private boolean isConnecting = false;
	
	private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	private static BufferedReader mBufferedReaderClient	= null;
	private static PrintWriter mPrintWriterClient = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 绑定操作
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		ipAddress = intent.getStringExtra(ScoketConst.IP_NAME);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		if (isConnecting) {				
			isConnecting = false;
			try {
				if(mSocketClient!=null) {
					mSocketClient.close();
					mSocketClient = null;
					
					mPrintWriterClient.close();
					mPrintWriterClient = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mThreadClient.interrupt();
			
		} else {				
			isConnecting = true;
			mThreadClient = new Thread(mRunnable);
			mThreadClient.start();				
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
    public void sendBroadcastToActivity(String message) {
        Intent it = new Intent(Clientcast.CONNECT_START);
        it.putExtra("message", message);
        super.sendBroadcast(it);
    }
	
	//线程:监听服务器发来的消息
	private Runnable	mRunnable	= new Runnable() {
		public void run() {
			if(ipAddress.length()<=0) {
				sendBroadcastToActivity("IP不能为空！");
				return;
			}
			int start = ipAddress.indexOf(":");
			if( (start == -1) ||(start+1 >= ipAddress.length()) ) {
				sendBroadcastToActivity("IP地址不合法!");
				return;
			}
			String sIP = ipAddress.substring(0, start);
			String sPort = ipAddress.substring(start+1);
			int port = Integer.parseInt(sPort);				
			
			Log.d("gjz", "IP:"+ sIP + ":" + port);		

			try {				
				//连接服务器
				mSocketClient = new Socket(sIP, port);	//portnum
				//取得输入、输出流
				mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
				
				mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
				
				sendBroadcastToActivity("已经连接server!");
				//break;
			} catch (Exception e) {
				sendBroadcastToActivity("连接IP异常:" + e.toString() + e.getMessage());
				e.printStackTrace();
				return;
			}			

			char[] buffer = new char[256];
			int count = 0;
			while (isConnecting) {
				try {
					//if ( (recvMessageClient = mBufferedReaderClient.readLine()) != null )
					if((count = mBufferedReaderClient.read(buffer))>0) {						
						sendBroadcastToActivity(getInfoBuff(buffer, count));
					}
				}catch (Exception e) {
					sendBroadcastToActivity("接收异常:" + e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}
	};
	
	private String getInfoBuff(char[] buff, int count){
		char[] temp = new char[count];
		for(int i=0; i<count; i++) {
			temp[i] = buff[i];
		}
		return new String(temp);
	}

}
