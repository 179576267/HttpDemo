package com.xiankezu.sirceo.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.fragments.Fragment_Main.HomeCourse;

public class HomeCourseAdapter extends CommonAdapter<HomeCourse> {
	private Context context;

	public HomeCourseAdapter(List<HomeCourse> mDatas, Context mContext,
			int mItemLayoutId) {
		super(mDatas, mContext, mItemLayoutId);
		this.context = mContext;
	}

	@Override
	public void convert(ViewHolder viewHolder, HomeCourse item) {
		ImageView im_pic = viewHolder.getView(R.id.im_pic);
		TextView tv_course_name = viewHolder.getView(R.id.tv_course_name);
		TextView tv_course_describe = viewHolder.getView(R.id.tv_course_describe);
		TextView tv_total_hours = viewHolder.getView(R.id.tv_total_hours);
		TextView tv_people_count = viewHolder.getView(R.id.tv_people_count);
		
		Picasso.with(context).load(item.imgUrl)
        .placeholder(R.drawable.icon_default_course)
        .error(R.drawable.icon_default_course).into(im_pic);
		
		tv_course_name.setText(item.courseName);
		tv_course_describe.setText(item.courseDescribe);
		tv_total_hours.setText("总课时:"+item.totalHours+"课时");
		tv_people_count.setText(item.peopleCount+"名小伙伴已经报名");
	}

}
