package com.xysoft.broadcast;

import kr.neolab.sdk.pen.penmsg.PenMsgType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.app.R;
import com.xysoft.common.PenCtrlConst.Broadcast;
import com.xysoft.common.PenCtrlConst.JsonTag;
import com.xysoft.suport.PenClientCtrl;
import com.xysoft.zdy.dialog.InputPasswordDialog;
import com.xysoft.zdy.surfaceview.SampleView;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

public class PenBroadcastReceiver extends BroadcastReceiver{
	
	private Context context;
	private PenClientCtrl penClientCtrl;
	private SampleView sampleView;
	public static final String TAG = "pensdk.sample";
	// Notification
	protected Builder mBuilder;
	protected NotificationManager mNotifyManager;
	protected Notification mNoti;
	public InputPasswordDialog inputPassDialog;
	
	public PenBroadcastReceiver(Context context, PenClientCtrl PenClientCtrl, SampleView sampleView) {
		this.context = context;
		this.sampleView = sampleView;
		PendingIntent pendingIntent = PendingIntent.getBroadcast( context, 0, new Intent( "firmware_update" ), PendingIntent.FLAG_UPDATE_CURRENT );
		mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle( "Update Pen" );
		mBuilder.setSmallIcon( R.drawable.ic_launcher );
		mBuilder.setContentIntent( pendingIntent );  
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if ( Broadcast.ACTION_PEN_MESSAGE.equals( action ) )
		{
			int penMsgType = intent.getIntExtra( Broadcast.MESSAGE_TYPE, 0 );
			String content = intent.getStringExtra( Broadcast.CONTENT );

			handleMsg( penMsgType, content );
		}
		else if ( Broadcast.ACTION_PEN_DOT.equals( action ) )
		{
			int sectionId = intent.getIntExtra( Broadcast.SECTION_ID, 0 );
			int ownerId = intent.getIntExtra( Broadcast.OWNER_ID, 0 );
			int noteId = intent.getIntExtra( Broadcast.NOTE_ID, 0 );
			int pageId = intent.getIntExtra( Broadcast.PAGE_ID, 0 );
			int x = intent.getIntExtra( Broadcast.X, 0 );
			int y = intent.getIntExtra( Broadcast.Y, 0 );
			int fx = intent.getIntExtra( Broadcast.FX, 0 );
			int fy = intent.getIntExtra( Broadcast.FY, 0 );
			int force = intent.getIntExtra( Broadcast.PRESSURE, 0 );
			long timestamp = intent.getLongExtra( Broadcast.TIMESTAMP, 0 );
			int type = intent.getIntExtra( Broadcast.TYPE, 0 );
			int color = intent.getIntExtra( Broadcast.COLOR, 0 );

			handleDot( sectionId, ownerId, noteId, pageId, x, y, fx, fy, force, timestamp, type, color );
		}
		else if ( Broadcast.ACTION_PEN_DOT.equals( action ))
		{
			penClientCtrl.suspendPenUpgrade();
		}
	}
	
	private void handleMsg( int penMsgType, String content) {
		Log.d( TAG, "handleMsg : " + penMsgType );
		switch ( penMsgType ) {
			// Message of the attempt to connect a pen
			case PenMsgType.PEN_CONNECTION_TRY:
				Toast.makeText(this.context, "try to connect.", Toast.LENGTH_SHORT);
				break;
			// Pens when the connection is completed (state certification process is not yet in progress)
			case PenMsgType.PEN_CONNECTION_SUCCESS:
				Toast.makeText( this.context, "connection is successful.", Toast.LENGTH_SHORT);
				break;
			// Message when a connection attempt is unsuccessful pen
			case PenMsgType.PEN_CONNECTION_FAILURE:
				Toast.makeText( this.context, "connection has failed.", Toast.LENGTH_SHORT);
				break;
			// When you are connected and disconnected from the state pen
			case PenMsgType.PEN_DISCONNECTED:
				Toast.makeText( this.context, "connection has been terminated.", Toast.LENGTH_SHORT);
				break;
			// Pen transmits the state when the firmware update is processed.
			case PenMsgType.PEN_FW_UPGRADE_STATUS: {
				try{
					JSONObject job = new JSONObject( content );
					int total = job.getInt( JsonTag.INT_TOTAL_SIZE );
					int sent = job.getInt( JsonTag.INT_SENT_SIZE );
					this.onUpgrading( total, sent );
					Log.d( TAG, "pen fw upgrade status => total : " + total + ", progress : " + sent );
				} catch ( JSONException e ) {
					e.printStackTrace();
				}
			}
				break;
			// Pen firmware update is complete
			case PenMsgType.PEN_FW_UPGRADE_SUCCESS:
				this.onUpgradeSuccess();
				Toast.makeText( this.context, "file transfer is complete.", Toast.LENGTH_SHORT);
				break;
			// Pen Firmware Update Fails
			case PenMsgType.PEN_FW_UPGRADE_FAILURE:
				this.onUpgradeFailure( false );
				Toast.makeText( this.context, "file transfer has failed.", Toast.LENGTH_SHORT);
				break;
			// When the pen stops randomly during the firmware update
			case PenMsgType.PEN_FW_UPGRADE_SUSPEND:
				this.onUpgradeFailure( true );
				Toast.makeText( this.context, "file transfer is suspended." ,Toast.LENGTH_SHORT);
				break;
			// Offline Data List response of the pen
			case PenMsgType.OFFLINE_DATA_NOTE_LIST:
				try{
					JSONArray list = new JSONArray( content );
					for ( int i = 0; i < list.length(); i++ ) {
						JSONObject jobj = list.getJSONObject( i );
						int sectionId = jobj.getInt( JsonTag.INT_SECTION_ID );
						int ownerId = jobj.getInt( JsonTag.INT_OWNER_ID );
						int noteId = jobj.getInt( JsonTag.INT_NOTE_ID );
						Log.d( TAG, "offline(" + (i + 1) + ") note => sectionId : " + sectionId + ", ownerId : " + ownerId + ", noteId : " + noteId );
					}
				}catch ( JSONException e ) {
					e.printStackTrace();
				}
				// if you want to get offline data of pen, use this function.
				// you can call this function, after complete download.
				//
				// iPenCtrl.reqOfflineData( sectionId, ownerId, noteId );
				Toast.makeText( this.context, "offline data list is received.", Toast.LENGTH_SHORT);
				break;
			// Messages for offline data transfer begins
			case PenMsgType.OFFLINE_DATA_SEND_START:
				
				break;
			// Offline data transfer completion
			case PenMsgType.OFFLINE_DATA_SEND_SUCCESS:

				break;
			// Offline data transfer failure
			case PenMsgType.OFFLINE_DATA_SEND_FAILURE:

				break;
			// Progress of the data transfer process offline
			case PenMsgType.OFFLINE_DATA_SEND_STATUS: {
				try{
					JSONObject job = new JSONObject( content );
					int total = job.getInt( JsonTag.INT_TOTAL_SIZE );
					int received = job.getInt( JsonTag.INT_RECEIVED_SIZE );
					Log.d( TAG, "offline data send status => total : " + total + ", progress : " + received );
				} catch ( JSONException e ) {
					e.printStackTrace();
				}
			}
				break;
			// When the file transfer process of the download offline
			case PenMsgType.OFFLINE_DATA_FILE_CREATED: {
				try {
					JSONObject job = new JSONObject( content );
					int sectionId = job.getInt( JsonTag.INT_SECTION_ID );
					int ownerId = job.getInt( JsonTag.INT_OWNER_ID );
					int noteId = job.getInt( JsonTag.INT_NOTE_ID );
					int pageId = job.getInt( JsonTag.INT_PAGE_ID );
					String filePath = job.getString( JsonTag.STRING_FILE_PATH );
					Log.d( TAG, "offline data file created => sectionId : " + sectionId + ", ownerId : " + ownerId + ", noteId : " + noteId + ", pageId : " + pageId + " filePath : " + filePath );
				} catch ( JSONException e ) {
					e.printStackTrace();
				}
			}
				break;
			// Ask for your password in a message comes when the pen
			case PenMsgType.PASSWORD_REQUEST: {
				int retryCount = -1, resetCount = -1;
				try {
					JSONObject job = new JSONObject( content );
					retryCount = job.getInt( JsonTag.INT_PASSWORD_RETRY_COUNT );
					resetCount = job.getInt( JsonTag.INT_PASSWORD_RESET_COUNT );
				} catch ( JSONException e ) {
					e.printStackTrace();
				}
				inputPassDialog = new InputPasswordDialog( this.context, penClientCtrl, retryCount, resetCount );
				inputPassDialog.show();
			}
				break;
		}
	}
	
	private void onUpgrading( int total, int progress ) {
		mBuilder.setContentText( "Sending" ).setProgress( total, progress, false );
		mNotifyManager.notify( 0, mBuilder.build() );
	}
	
	private void onUpgradeSuccess() {
		mBuilder.setContentText( "The file transfer is complete." ).setProgress( 0, 0, false );
		mNotifyManager.notify( 0, mBuilder.build() );
	}
	
	private void onUpgradeFailure( boolean isSuspend ) {
		if ( isSuspend ) {
			mBuilder.setContentText( "file transfer is suspended." ).setProgress( 0, 0, false );
		}
		else {
			mBuilder.setContentText( "file transfer has failed." ).setProgress( 0, 0, false );
		}
		mNotifyManager.notify( 0, mBuilder.build() );
	}

	private void handleDot( int sectionId, int ownerId, int noteId, int pageId, int x, int y, int fx, int fy, int force, long timestamp, int type, int color ) {
		sampleView.addDot( sectionId, ownerId, noteId, pageId, x, y, fx, fy, force, timestamp, type, color );
	}
	
	
	
}
