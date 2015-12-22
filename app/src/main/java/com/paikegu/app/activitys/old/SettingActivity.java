package com.xiankezu.sirceo.activitys.old;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;

public class SettingActivity extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
	}

	@Override
	protected void findViews() {
		findViewById(R.id.im_back).setOnClickListener(this);
	}

	@Override
	protected void initSomething() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.im_back:
			finish();
			break;
		}
	}

}
