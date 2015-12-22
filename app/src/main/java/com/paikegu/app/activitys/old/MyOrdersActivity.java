package com.xiankezu.sirceo.activitys.old;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.OrderFinishAdapter;
import com.xiankezu.sirceo.widghts.NestRadioGroup;
import com.xiankezu.sirceo.widghts.NestRadioGroup.OnCheckedChangeListener;

public class MyOrdersActivity extends BaseActivity implements OnClickListener{
	private NestRadioGroup radioGroup;
	private ListView lv;
	private List<Object> lists;
	private OrderFinishAdapter finishAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_orders);
	}

	@Override
	protected void findViews() {
		findViewById(R.id.tv_back).setOnClickListener(this);
		lv = (ListView) findViewById(R.id.lv);
		radioGroup = (NestRadioGroup) findViewById(R.id.main_radio);
	}

	@Override
	protected void initSomething() {
		lists = new ArrayList<Object>();
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		lists.add(new Object());
		finishAdapter = new OrderFinishAdapter(lists, application, R.layout.item_sirceo_finish);
		lv.setAdapter(finishAdapter);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(NestRadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button0:
					finishAdapter = new OrderFinishAdapter(lists, application, R.layout.item_sirceo_finish);
					lv.setAdapter(finishAdapter);
					break;
				case R.id.radio_button1:
					finishAdapter = new OrderFinishAdapter(lists, application, R.layout.item_sirceo_deal);
					lv.setAdapter(finishAdapter);
					break;
				case R.id.radio_button2:
					finishAdapter = new OrderFinishAdapter(lists, application, R.layout.item_sirceo_waiting);
					lv.setAdapter(finishAdapter);
					break;
				case R.id.radio_button3:
					finishAdapter = new OrderFinishAdapter(lists, application, R.layout.item_sirceo_classing);
					lv.setAdapter(finishAdapter);
					break;
				case R.id.radio_button4:
					finishAdapter = new OrderFinishAdapter(lists, application, R.layout.item_sirceo_comm);
					lv.setAdapter(finishAdapter);
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		}
	}

}
