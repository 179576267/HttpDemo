package com.xiankezu.sirceo.adapters;

import java.util.List;
import android.widget.ImageView;
import android.widget.TextView;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.beans.User;
import com.xiankezu.sirceo.globals.MyApplication;

public class SearchTeacherAdapter extends CommonAdapter<User> {

	private MyApplication mApplication;
	
	
	public SearchTeacherAdapter(List<User> mDatas, MyApplication application,
			int mItemLayoutId) {
		super(mDatas, application, mItemLayoutId);
		this.mApplication = application;
	}

	@Override
	public void convert(ViewHolder viewHolder, User item) {
		ImageView im_avatar = viewHolder.getView(R.id.im_avatar);
		TextView tv_name = viewHolder.getView(R.id.tv_name);
		TextView tv_code = viewHolder.getView(R.id.tv_code);
		TextView tv_detial = viewHolder.getView(R.id.tv_detial);
		
		mApplication.getPictureCache().loadNetPicture(im_avatar, item.avatar);
		
		tv_code.setText("码族："+item.guid);
		
		tv_name.setText(item.nickName);
		
		//男   教龄10年   守约率：89%
		StringBuffer sb = new StringBuffer();
		sb.append(item.gender==1?"男   教龄":"女   教龄");
		sb.append(item.seniority);
		sb.append("年   守约率：");
		sb.append(item.complianceRate);
		sb.append("%");
		tv_detial.setText(sb.toString());
	}

}
