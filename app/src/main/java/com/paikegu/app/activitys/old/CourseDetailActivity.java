package com.xiankezu.sirceo.activitys.old;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.ClassHoursAdapter;
import com.xiankezu.sirceo.beans.ClassHoursBean;
import com.xiankezu.sirceo.beans.CourseDetialBean;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.ContentUtils;
import com.xiankezu.sirceo.tools.JsonUtils;

public class CourseDetailActivity extends BaseActivity implements OnClickListener{
	private ImageView im_avatar;
	private TextView tv_name;
	private TextView tv_tmid;
	private TextView tv_total_hours;
	private TextView tv_start_time;
	private TextView tv_total;
	private TextView tv_course_describe;
	private ListView lv_class_hours;
	
	private List<ClassHoursBean> list;
	private ClassHoursAdapter adapter;
	
	private long cid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getIntentArg();
		setContentView(R.layout.activity_course_detail);
	}
	
	private void getIntentArg() {
		Intent intent = getIntent();
		if(intent.hasExtra("cid")){
			cid = intent.getLongExtra("cid", 0);
		}else{
			finish();
		}
	}

	@Override
	protected void findViews() {
		findViewById(R.id.im_back).setOnClickListener(this);
		findViewById(R.id.tv_user_detial).setOnClickListener(this);
		im_avatar = (ImageView) findViewById(R.id.im_avatar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_tmid = (TextView) findViewById(R.id.tv_tmid);
		tv_total_hours = (TextView) findViewById(R.id.tv_total_hours);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		tv_total = (TextView) findViewById(R.id.tv_total);
		tv_course_describe = (TextView) findViewById(R.id.tv_course_describe);
		lv_class_hours = (ListView) findViewById(R.id.lv_class_hours);
	}

	@Override
	protected void initSomething() {
		list = new ArrayList<ClassHoursBean>();
		adapter = new ClassHoursAdapter(list, application, R.layout.item_class_hours);
		lv_class_hours.setAdapter(adapter);
		RequestParams params = new RequestParams();
		params.put("phoneNum", application.loginPhoneNum);
		params.put("cid", cid);
		api.post(URL.GET_COURSE_DETAILS, params, new SIRCEOResponseHandler(this,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					JSONObject jsonObject = result.getJSONObject("course");
					CourseDetialBean bean = JsonUtils.fromJSON(CourseDetialBean.class, jsonObject.toString());
					JSONArray array = result.getJSONArray("classHours");
					List<ClassHoursBean> newList = JsonUtils.getListFromJSON(ClassHoursBean.class, array.toString());
					list.clear();
					list.addAll(newList);
					adapter.notifyDataSetChanged();
					ContentUtils.setListViewHeightBasedOnChildren(lv_class_hours);
					fillData(bean);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(String result,int error) {
			}
		});
	}

	protected void fillData(CourseDetialBean bean) {
		application.getPictureCache().loadNetPicture(im_avatar, bean.imgUrl);
		tv_name.setText(bean.courseName);
		tv_tmid.setText("授课方式:"+(bean.tmid==1?"学客上门":(bean.tmid==2?"闲客上门":"网上授课")));
		tv_total_hours.setText("总课时："+bean.totalHours+"课时");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date((long)(bean.startTime)*1000));
		tv_start_time.setText("开课时间："+
							 calendar.get(Calendar.YEAR)+"年"+
							 (calendar.get(Calendar.MONTH)+1)+"月"+
							 calendar.get(Calendar.DAY_OF_MONTH)+"日");
		tv_total.setText(bean.total+"元");
		tv_course_describe.setText(bean.courseDescribe);
	}

	/**
	 * 启动方法
	 * @param context
	 * @param cid
	 */
	public static void startCourseDetailActivity(Context context,long cid){
		Intent intent = new Intent(context,CourseDetailActivity.class);
		intent.putExtra("cid", cid);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.im_back:
			finish();
			break;
		case R.id.tv_user_detial:
			break;
		}
	}
	
}
