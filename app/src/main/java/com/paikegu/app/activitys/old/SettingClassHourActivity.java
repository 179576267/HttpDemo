package com.xiankezu.sirceo.activitys.old;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.ClassHourSettingAdapter;

public class SettingClassHourActivity extends BaseActivity {
	private ListView lv;
	private List<Object> list;
	private ClassHourSettingAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_class_hour);
	}
	
	@Override
	protected void findViews() {
		lv = (ListView) findViewById(R.id.lv);
	}

	@Override
	protected void initSomething() {
		list = new ArrayList<Object>();
		list.add(new Object());
		adapter = new ClassHourSettingAdapter(list, application, R.layout.item_setting_class_hour);
		lv.setAdapter(adapter);
	}

}
