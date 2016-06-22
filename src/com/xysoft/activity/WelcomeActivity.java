package com.xysoft.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.app.R;
import com.xysoft.suport.BaseActivity;
import com.xysoft.util.InjectView;
import com.xysoft.util.Injector;


public class WelcomeActivity extends BaseActivity{
	
	@InjectView(R.id.welcome_start)
	private TextView start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		Injector.get(this).inject();
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent (WelcomeActivity.this, MainActivity.class);	
				startActivity(intent);
				finish();
			}
		});
	}
}
