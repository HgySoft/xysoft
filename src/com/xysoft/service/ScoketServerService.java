package com.xysoft.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import com.xysoft.common.ScoketConst.Clientcast;
import com.xysoft.util.IpUtil;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ScoketServerService extends Service {
	
	private Socket mSocketServer = null;
	private Thread mThreadServer = null;
    private ServerSocket serverSocket = null;
    private boolean serverRuning = false;
	static BufferedReader mBufferedReaderServer	= null;
	static PrintWriter mPrintWriterServer = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (serverRuning) {
			serverRuning = false;
			try {
				if(serverSocket!=null) {
					serverSocket.close();
					serverSocket = null;
				}
				if(mSocketServer!=null) {
					mSocketServer.close();
					mSocketServer = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mThreadServer.interrupt();
		}else {
			serverRuning = true;
			mThreadServer = new Thread(mcreateRunnable);
			mThreadServer.start();
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
	private Runnable	mcreateRunnable	= new Runnable() {
		public void run() {				
			try {
				serverSocket = new ServerSocket(0);
				
				SocketAddress address = null;	
				if(!serverSocket.isBound())	{
					serverSocket.bind(address, 0);
				}
				
				sendBroadcastToActivity("请连接IP："+IpUtil.getIpV4Address()+":"+serverSocket.getLocalPort());

                //方法用于等待客服连接 
                mSocketServer = serverSocket.accept();	                	               
                
                //接受客服端数据BufferedReader对象
                mBufferedReaderServer = new BufferedReader(new InputStreamReader(mSocketServer.getInputStream()));
                //给客服端发送数据
                mPrintWriterServer = new PrintWriter(mSocketServer.getOutputStream(),true);
                //mPrintWriter.println("服务端已经收到数据！");

                sendBroadcastToActivity("client已经连接上！");
                
			} catch (IOException e) {
				sendBroadcastToActivity("创建异常:" + e.getMessage() + e.toString());
				e.printStackTrace();
				return;
			}
			char[] buffer = new char[256];
			int count = 0;
			while(serverRuning) {
				try {
					//if( (recvMessageServer = mBufferedReaderServer.readLine()) != null )//获取客服端数据
					if((count = mBufferedReaderServer.read(buffer))>0) {						
						sendBroadcastToActivity(getInfoBuff(buffer, count));
					}
				}catch (Exception e) {
					sendBroadcastToActivity("接收异常:" + e.getMessage());
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
