package com.xysoft.suport;

import java.io.File;
import org.json.JSONObject;
import com.xysoft.common.PenCtrlConst;
import com.xysoft.util.PreferenceUtils;
import android.content.Context;
import android.content.Intent;
import kr.neolab.sdk.pen.PenCtrl;
import kr.neolab.sdk.pen.penmsg.IPenMsgListener;
import kr.neolab.sdk.pen.penmsg.PenMsg;
import kr.neolab.sdk.pen.penmsg.PenMsgType;
import kr.neolab.sdk.util.NLog;

public class PenClientCtrl implements IPenMsgListener{
	
	private static PenCtrl iPenCtrl;
	private static PenClientCtrl penClientCtrl;
	private static boolean isConnected = false;
	private static boolean isAuthorized = false;
	private static String curPass = "0000";
	private static String newPass = "0000";
	public static int USING_SECTION_ID = 3;
	public static int USING_OWNER_ID = 27;
	public static int[] USING_NOTES = new int[] { 301, 302, 303, 28, 50, 101, 102, 103, 201, 202, 203, 600, 601, 602, 603, 605, 606, 607, 608 };
	private Context context;
	
	static {
		iPenCtrl = PenCtrl.getInstance();
	}
	
	public PenClientCtrl( Context context ) {
		this.context = context;
		iPenCtrl.startup();
		iPenCtrl.setListener(this);
	}
	
	public static PenClientCtrl getInstance( Context context ) {
		if(penClientCtrl == null) {
			synchronized(PenClientCtrl.class) {
				if(penClientCtrl == null) {
					penClientCtrl = new PenClientCtrl(context);
				}
			}
		}
		return penClientCtrl;
	}
	
	public String getCurrentPassword(){
		return curPass;
	}
	
	public boolean isAuthorized() {
		return isAuthorized;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void connect( String address ) {
		iPenCtrl.connect( address );
	}

	public void disconnect() {
		iPenCtrl.disconnect();
	}

	public void upgradePen( File fwFile ) {   
		iPenCtrl.upgradePen(fwFile);
	}
	
	public void suspendPenUpgrade() {
		iPenCtrl.suspendPenUpgrade();
	}
	
	public void inputPassword( String password ) {
		curPass = password;
		iPenCtrl.inputPassword( password );
	}

	public void reqSetupPassword( String oldPassword, String newPassword ) {
		iPenCtrl.reqSetupPassword( oldPassword, newPassword );
	}

	public void reqOfflineDataList() {
		iPenCtrl.reqOfflineDataList();
	}
	
	public void reqPenStatus() {
		iPenCtrl.reqPenStatus();
	}
	
	public void reqSetupAutoPowerOnOff(boolean setOn) {
		iPenCtrl.reqSetupAutoPowerOnOff( setOn );
	}
	
    public void reqSetupPenBeepOnOff( boolean setOn ) {
		iPenCtrl.reqSetupPenBeepOnOff( setOn );
    }

	public void reqSetupPenTipColor( int color ) {
		iPenCtrl.reqSetupPenTipColor( color );
	}

	public void reqSetupAutoShutdownTime( short minute ) {
		iPenCtrl.reqSetupAutoShutdownTime( minute );
	}

	public void reqSetupPenSensitivity( short level ) {
		iPenCtrl.reqSetupPenSensitivity( level );
	}
	
	private void sendPenDotByBroadcast( int sectionId, int ownerId, int noteId, int pageId, int x, int y, int fx, int fy, int pressure, long timestamp, int type, int color ) {
		Intent intent = new Intent( PenCtrlConst.Broadcast.ACTION_PEN_DOT );
		intent.putExtra( PenCtrlConst.Broadcast.SECTION_ID, sectionId );
		intent.putExtra( PenCtrlConst.Broadcast.OWNER_ID, ownerId );
		intent.putExtra( PenCtrlConst.Broadcast.NOTE_ID, noteId );
		intent.putExtra( PenCtrlConst.Broadcast.PAGE_ID, pageId );
		intent.putExtra( PenCtrlConst.Broadcast.X, x );
		intent.putExtra( PenCtrlConst.Broadcast.Y, y );
		intent.putExtra( PenCtrlConst.Broadcast.FX, fx );
		intent.putExtra( PenCtrlConst.Broadcast.FY, fy );
		intent.putExtra( PenCtrlConst.Broadcast.PRESSURE, pressure );
		intent.putExtra( PenCtrlConst.Broadcast.TIMESTAMP, timestamp );
		intent.putExtra( PenCtrlConst.Broadcast.TYPE, type );
		intent.putExtra( PenCtrlConst.Broadcast.COLOR, color );
		context.sendBroadcast( intent );
	}

	@Override
	public void onReceiveDot(int sectionId, int ownerId, int noteId, int pageId, int x, int y, int fx, int fy, int pressure, long timestamp, int type, int color) {
		sendPenDotByBroadcast(sectionId, ownerId, noteId, pageId, x, y, fx, fy, pressure, timestamp, type, color);
	}

	@Override
	public void onReceiveMessage(PenMsg penMsg) {
		JSONObject jsonObject = null;
		switch (penMsg.penMsgType) {
			case PenMsgType.PEN_CONNECTION_SUCCESS:
				isConnected = true;
				break;
			case PenMsgType.PEN_AUTHORIZED:
				isAuthorized = true;
				iPenCtrl.reqAddUsingNote( USING_SECTION_ID, USING_OWNER_ID, USING_NOTES );
				iPenCtrl.reqOfflineDataList();
				iPenCtrl.reqOfflineData( USING_SECTION_ID, USING_OWNER_ID, 301 );
				break;
			case PenMsgType.PEN_DISCONNECTED:
				isConnected = false;
				isAuthorized = false;
				break;
			case PenMsgType.PASSWORD_REQUEST:
				jsonObject = penMsg.getContentByJSONObject();
				try {
					int count = jsonObject.getInt( PenCtrlConst.JsonTag.INT_PASSWORD_RETRY_COUNT );
					NLog.d("password count : " + count);
					if ( count == 0 ) {
						inputPassword(curPass);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case PenMsgType.PEN_STATUS:
				jsonObject = penMsg.getContentByJSONObject();
				if ( jsonObject == null ) {
					return;
				}
				NLog.d( jsonObject.toString() );
				try {
					String stat_version = jsonObject.getString( PenCtrlConst.JsonTag.STRING_PROTOCOL_VERSION );
					int stat_timezone = jsonObject.getInt( PenCtrlConst.JsonTag.INT_TIMEZONE_OFFSET );
					long stat_timetick = jsonObject.getLong( PenCtrlConst.JsonTag.LONG_TIMETICK );
					int stat_forcemax = jsonObject.getInt( PenCtrlConst.JsonTag.INT_MAX_FORCE );
					int stat_battery = jsonObject.getInt( PenCtrlConst.JsonTag.INT_BATTERY_STATUS );
					int stat_usedmem = jsonObject.getInt( PenCtrlConst.JsonTag.INT_MEMORY_STATUS );
					int stat_pencolor = jsonObject.getInt( PenCtrlConst.JsonTag.INT_PEN_COLOR );
					boolean stat_autopower = jsonObject.getBoolean( PenCtrlConst.JsonTag.BOOL_AUTO_POWER_ON );
					boolean stat_accel = jsonObject.getBoolean( PenCtrlConst.JsonTag.BOOL_ACCELERATION_SENSOR );
					boolean stat_hovermode = jsonObject.getBoolean( PenCtrlConst.JsonTag.BOOL_HOVER );
					boolean stat_beep = jsonObject.getBoolean( PenCtrlConst.JsonTag.BOOL_BEEP );
					int stat_autopower_time = jsonObject.getInt( PenCtrlConst.JsonTag.INT_AUTO_POWER_OFF_TIME );
					int stat_sensitivity = jsonObject.getInt( PenCtrlConst.JsonTag.INT_PEN_SENSITIVITY );
					
					PreferenceUtils.setString(context, PenCtrlConst.Setting.KEY_PROTOCOL_VERSION, stat_version);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_TIMEZONE_OFFSET, stat_timezone);
					PreferenceUtils.setLong(context, PenCtrlConst.Setting.KEY_TIMETICK, stat_timetick);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_MAX_FORCE, stat_forcemax);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_BATTERY_STATUS, stat_battery);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_MEMORY_STATUS, stat_usedmem);
					PreferenceUtils.setBoolean(context, PenCtrlConst.Setting.KEY_BOOL_HOVER, stat_hovermode);
					PreferenceUtils.setBoolean(context, PenCtrlConst.Setting.KEY_ACCELERATION_SENSOR, stat_accel);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_AUTO_POWER_OFF_TIME, stat_autopower_time);
					PreferenceUtils.setBoolean(context, PenCtrlConst.Setting.KEY_AUTO_POWER_ON, stat_autopower);
					PreferenceUtils.setBoolean(context, PenCtrlConst.Setting.KEY_BEEP, stat_beep);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_PEN_COLOR, stat_pencolor);
					PreferenceUtils.setInt(context, PenCtrlConst.Setting.KEY_SENSITIVITY, stat_sensitivity);
					PreferenceUtils.setString(context, PenCtrlConst.Setting.KEY_PASSWORD, getCurrentPassword());
				} catch ( Exception e ) {
					e.printStackTrace();
				}
				break;
			case PenMsgType.PASSWORD_SETUP_SUCCESS:
				if ( curPass != newPass ) {
					curPass = newPass;
				}
				break;
			case PenMsgType.PASSWORD_SETUP_FAILURE:
				if ( curPass != newPass ) {
					newPass = curPass;
				}
				break;
		}
		sendPenMsgByBroadcast(penMsg);
	}
	
	private void sendPenMsgByBroadcast( PenMsg penMsg ) {
		Intent intent = new Intent( PenCtrlConst.Broadcast.ACTION_PEN_MESSAGE );
		intent.putExtra( PenCtrlConst.Broadcast.MESSAGE_TYPE, penMsg.getPenMsgType() );
		intent.putExtra( PenCtrlConst.Broadcast.CONTENT, penMsg.getContent() );
		context.sendBroadcast( intent );
	}
	

}
