package com.xysoft.zdy.dialog;

import com.example.app.R;
import com.xysoft.suport.PenClientCtrl;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputPasswordDialog extends Dialog {
	
	private PenClientCtrl penClientCtrl;

	public Button btnLogin;
	
	public EditText edPass;
	
	public InputPasswordDialog( final Context context, PenClientCtrl penClientCtrl, int retryCount, int resetCount )
	{
		super( context );
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		getWindow().clearFlags( WindowManager.LayoutParams.FLAG_DIM_BEHIND );
		
		setContentView( R.layout.input_password_dialog );
		
		edPass = (EditText) findViewById( R.id.inputPassword );
		
		btnLogin = (Button) findViewById( R.id.btnInputPassword );
		btnLogin.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View v )
			{
				if ( edPass.length() <= 0 )
				{
					Toast.makeText( context, "please input your password !!", Toast.LENGTH_SHORT ).show();
				}
				else
				{
					submit();
					cancel();
				}
			}
		} );
	}
	
	public void submit()
	{
		penClientCtrl.inputPassword( edPass.getText().toString() );
	}
	
	@Override
	public void cancel()
	{
		super.cancel();
	}
}
