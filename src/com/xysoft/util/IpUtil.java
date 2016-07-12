package com.xysoft.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class IpUtil {
	
	public static String getIpV6Address() {    
        try {    
            for (Enumeration<NetworkInterface> en = NetworkInterface    
                    .getNetworkInterfaces(); en.hasMoreElements();) {    
                NetworkInterface intf = en.nextElement();    
                for (Enumeration<InetAddress> enumIpAddr = intf    
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {    
                    InetAddress inetAddress = enumIpAddr.nextElement();    
                    if (!inetAddress.isLoopbackAddress()) {    
                        return inetAddress.getHostAddress().toString();    
                    }    
                }    
            }    
        } catch (SocketException ex) {    
            Log.e("WifiPreference IpAddress", ex.toString());    
        }    
        return null;    
    }
	
    public static String getIpV4Address() {  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface  
                        .getNetworkInterfaces(); en.hasMoreElements();) {  
                    NetworkInterface intf = en.nextElement();  
                   for (Enumeration<InetAddress> enumIpAddr = intf  
                            .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                        InetAddress inetAddress = enumIpAddr.nextElement();  
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {  
                        return inetAddress.getHostAddress().toString();  
                        }  
                   }  
                }  
            } catch (SocketException ex) {  
                Log.e("WifiPreference IpAddress", ex.toString());  
            }  
         return null;  
    } 
    
    public static String getMacFromWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);   
        if (!wifiManager.isWifiEnabled()) {   
        	wifiManager.setWifiEnabled(true);    
        }   
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
        String mResult = wifiInfo.getMacAddress();
        return mResult;
	}

}
