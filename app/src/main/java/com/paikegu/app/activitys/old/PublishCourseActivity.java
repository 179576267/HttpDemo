package com.xiankezu.sirceo.activitys.old;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;

public class PublishCourseActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_courses);
	}
	
	@Override
	protected void findViews() {
		findViewById(R.id.rl_setting_class_hour).setOnClickListener(this);
	}

	@Override
	protected void initSomething() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_setting_class_hour:
			startActivity(new Intent(application, SettingClassHourActivity.class));
			break;
		}
	}

}
