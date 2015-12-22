package com.xiankezu.sirceo.fragments;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.sliding.AbSlidingPlayView.AbOnChangeListener;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.old.CityChooseActivity;
import com.xiankezu.sirceo.activitys.old.CourseDetailActivity;
import com.xiankezu.sirceo.activitys.old.SearchTeacherActivity;
import com.xiankezu.sirceo.adapters.HomeCourseAdapter;
import com.xiankezu.sirceo.globals.IntentFileds;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.JsonUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 发现界面
 * 
 * @author allenjuns@yahoo.com
 *
 */
public class Fragment_Main extends BaseFragment implements OnClickListener,OnItemClickListener{
	
	public Fragment_Main(int resource) {
		super(resource);
	}
	private final int GET_LOCATION = 0X001;
	
	private TextView tv_location;
	private TextView tv_search;
	private TextView tv_message;
	
	private AbSlidingPlayView slidingView;
	private ListView lv_course;
	
	private List<Advert> advertList;
	private List<HomeCourse> homeCourseList;
	private HomeCourseAdapter adapter;
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_LOCATION:
				if (TextUtils.isEmpty(mApplication.city)){
					sendEmptyMessageDelayed(GET_LOCATION, 1000);
				}else{
					tv_location.setText(mApplication.city);
				}
				break;

			}
		};
	};
	
	@Override
	protected void initView() {
		tv_location = (TextView) layout.findViewById(R.id.tv_location);
		tv_location.setOnClickListener(this);
		tv_search = (TextView) layout.findViewById(R.id.tv_search);
		tv_search.setOnClickListener(this);
		tv_message = (TextView) layout.findViewById(R.id.tv_message);
		tv_message.setOnClickListener(this);
		slidingView = (AbSlidingPlayView) layout.findViewById(R.id.layout_sliding_advertisement);
		lv_course = (ListView) layout.findViewById(R.id.lv_course);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_location:
			mActivity.startActivityForResult(new Intent(mActivity, CityChooseActivity.class), IntentFileds.CITY_CHOOSE_BACK);
			break;
		case R.id.tv_search:
			startActivity(new Intent(mApplication.getApplicationContext(), SearchTeacherActivity.class));
			break;
		case R.id.tv_message:
			
			break;

		}
	}


	@Override
	protected void initSomething() {
		tv_location.setText("定位中");
		handler.sendEmptyMessage(GET_LOCATION);
		advertList = new ArrayList<Fragment_Main.Advert>();
		homeCourseList = new ArrayList<Fragment_Main.HomeCourse>();
		adapter = new HomeCourseAdapter(homeCourseList, mApplication.getApplicationContext(), R.layout.item_homepage_list);
		lv_course.setAdapter(adapter);
		lv_course.setOnItemClickListener(this);
		getOnlineData();
	}


	private void getOnlineData() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", mApplication.loginPhoneNum);
		api.post(URL.HOMEPAGE, params, new SIRCEOResponseHandler(mActivity,false) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					advertList.addAll(JsonUtils.getListFromJSON(Advert.class, result.getJSONArray("advertList").toString()));
					homeCourseList.addAll(JsonUtils.getListFromJSON(HomeCourse.class, result.getJSONArray("homeCourseList").toString()));
					fetchData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(String result,int error) {
				// TODO Auto-generated method stub
				
			}
		});
			
	}
	
	protected void fetchData() {
		 slidingView.removeAllViews();
         for (Advert ad:advertList) {
             ImageView imageView = new ImageView(mActivity);
             imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
             imageView.setScaleType(ScaleType.CENTER_CROP);
             if (!TextUtils.isEmpty(ad.imgUrl)) {
                 Picasso.with(mActivity).load(ad.imgUrl)
                         .placeholder(R.drawable.icon_default_ad)
                         .error(R.drawable.icon_default_ad).into(imageView);
             }
             slidingView.addView(imageView);
         }
         slidingView.setNavHorizontalGravity(Gravity.CENTER | Gravity.BOTTOM);
         slidingView.startPlay(3000);
         slidingView.setOnPageChangeListener(new AbOnChangeListener() {

             @Override
             public void onChange(int position) {
             }
         });
         
         adapter.notifyDataSetChanged();
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {
			case IntentFileds.CITY_CHOOSE_BACK:
				String city = data.getStringExtra("city");
				tv_location.setText(city);
				break;
			}
		}
	}
	
	private class Advert{
		public long halid;//广告id
		public String imgUrl;//广告图片URL
		public String linkUrl;//广告链接URL
		private int choiceSort;
	}
	
	
	public class HomeCourse{
		public long cid;//课程id
		public String courseName;//课程名称
		public String courseDescribe;//课程介绍
		public int totalHours;//课程总课时
		public String imgUrl;//课程展示图片
		public int guid;//发布课程的闲客id
		public int startTime;//课程开始时间
		public int endTime;//课程结束时间
		public int peopleCount;//报名人数
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		CourseDetailActivity.startCourseDetailActivity(mActivity, homeCourseList.get(arg2).cid);
	}
	
}
