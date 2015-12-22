package com.xiankezu.sirceo.adapters;

import java.util.List;

import com.xiankezu.sirceo.R;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class ClassifyAdapter extends CommonAdapter<String> {

	private boolean ret;
	
	public ClassifyAdapter(List<String> mDatas, Context mContext,
			int mItemLayoutId,boolean ret) {
		super(mDatas, mContext, mItemLayoutId);
		this.ret = ret;
	}

	@Override
	public void convert(ViewHolder viewHolder, String item) {
		TextView tv = viewHolder.getView(R.id.tv);
		tv.setText(item);
		
		TextView tv_right =  viewHolder.getView(R.id.tv_right);
		TextView tv_divide =  viewHolder.getView(R.id.tv_divide);
		if(ret){
			tv_right.setVisibility(View.VISIBLE);
			tv_divide.setVisibility(View.VISIBLE);
		}else{
			tv_right.setVisibility(View.INVISIBLE);
			tv_divide.setVisibility(View.INVISIBLE);
		}
	}

}
