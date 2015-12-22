package com.xiankezu.sirceo.adapters;

import java.util.List;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.beans.ClassHoursBean;
import com.xiankezu.sirceo.globals.MyApplication;

public class ClassHoursAdapter extends CommonAdapter<ClassHoursBean> {
	private MyApplication myApplication;
	
	public ClassHoursAdapter(List<ClassHoursBean> mDatas, MyApplication application,
			int mItemLayoutId) {
		super(mDatas, application.getApplicationContext(), mItemLayoutId);
		this.myApplication = application;
	}

	@Override
	public void convert(ViewHolder viewHolder, final ClassHoursBean item) {
		CheckBox cb = viewHolder.getView(R.id.cb);
		TextView tv_name = viewHolder.getView(R.id.tv_name);
		TextView tv_class_length = viewHolder.getView(R.id.tv_class_length);
		TextView tv_price = viewHolder.getView(R.id.tv_price);
		
		cb.setChecked(item.selected);
		tv_name.setText(item.chname);
		tv_price.setText("课程单价："+item.price+"元");
		tv_class_length.setText(item.duration/60+"时"+item.duration%60+"分");
		
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				item.selected = isChecked;
			}
		});
		
	}

}
