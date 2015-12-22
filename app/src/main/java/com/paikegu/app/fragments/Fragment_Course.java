package com.xiankezu.sirceo.fragments;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.old.CourseDetailActivity;
import com.xiankezu.sirceo.adapters.ClassifyAdapter;
import com.xiankezu.sirceo.adapters.CourseListAdapter;
import com.xiankezu.sirceo.beans.Areas;
import com.xiankezu.sirceo.beans.CourseClassifyMainBean;
import com.xiankezu.sirceo.beans.CourseDetialBean;
import com.xiankezu.sirceo.beans.FilterConditionBean;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.JsonUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;


/**
 * 课程界面
 * @author zhenfei.wang 
 *
 */
public class Fragment_Course extends BaseFragment implements OnItemClickListener,OnClickListener{
	public final int LOAD_MORE = 0X001;
	public final int REFRESH = 0X002;
	public final int NORMAL = 0X003;
	
	private AbPullToRefreshView abPullToRefreshView;
	private ListView lv_course;
	private List<CourseDetialBean> lists;
	private CourseListAdapter adapter;
	
	private RadioGroup rg_classify;
	private FilterConditionBean filter = new FilterConditionBean();
	private int page = 1;
	private int selectTime;
	/**
	 * 课程
	 */
	private RelativeLayout layout_course;
	private List<CourseClassifyMainBean> classifyCoursrs;
	private int mainPosition;
	private ListView lv_course_second;
	private ListView lv_course_main;
	
	/**
	 * 区域
	 */
	private RelativeLayout layout_area;
	private List<Areas> classifyArea;
	private ListView lv_areas;
	
	/**
	 * 方式
	 */
	private RelativeLayout layout_way;
	private List<String> classifyWay;
	private ListView lv_way;
	/**
	 * 价格
	 */
	private RelativeLayout layout_price;
	private EditText et_min;
	private EditText et_max;
	private List<String> classifyprice;
	private ListView lv_price;
	/**
	 * 闲客
	 */
	private RelativeLayout layout_sirceo;
	private List<String> classifySirceo;
	private ListView lv_sirceo;
	
	public Fragment_Course(int resource) {
		super(resource);
	}
	
	@Override
	protected void initView() {
		abPullToRefreshView = (AbPullToRefreshView) layout.findViewById(R.id.lv_list_refresh);
		// listView的刷新监听
		abPullToRefreshView.setPullRefreshEnable(true);
		abPullToRefreshView.setLoadMoreEnable(true);
		abPullToRefreshView.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
					page = 1;
					getOnlineData(REFRESH);
				}
		});
		abPullToRefreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				getOnlineData(LOAD_MORE);
			}
		});
		
		lv_course = (ListView) layout.findViewById(R.id.lv_course);
		lv_course.setOnItemClickListener(this);
		
		layout_course = (RelativeLayout) layout.findViewById(R.id.layout_course);
		lv_course_second = (ListView) layout.findViewById(R.id.lv_second);
		lv_course_second.setOnItemClickListener(this);
		lv_course_main = (ListView) layout.findViewById(R.id.lv_main);
		lv_course_main.setOnItemClickListener(this);
		
		layout_area = (RelativeLayout) layout.findViewById(R.id.layout_area);
		lv_areas = (ListView) layout.findViewById(R.id.lv_area);
		lv_areas.setOnItemClickListener(this);
		
		layout_way = (RelativeLayout) layout.findViewById(R.id.layout_way);
		lv_way = (ListView) layout.findViewById(R.id.lv_way);
		lv_way.setOnItemClickListener(this);
		
		layout_price = (RelativeLayout) layout.findViewById(R.id.layout_price);
		et_min = (EditText) layout.findViewById(R.id.et_min);
		et_max = (EditText) layout.findViewById(R.id.et_max);
		layout.findViewById(R.id.tv_sure).setOnClickListener(this);
		lv_price = (ListView) layout.findViewById(R.id.lv_price);
		lv_price.setOnItemClickListener(this);
		
		layout_sirceo = (RelativeLayout) layout.findViewById(R.id.layout_sirceo);
		lv_sirceo= (ListView) layout.findViewById(R.id.lv_sirceo);
		lv_sirceo.setOnItemClickListener(this);
		
		rg_classify = (RadioGroup) layout.findViewById(R.id.rg_classify);
	}
	@Override
	protected void initSomething() {
		lists = new ArrayList<CourseDetialBean>();
		adapter = new CourseListAdapter(lists, mActivity, R.layout.item_course);
		lv_course.setAdapter(adapter);
		rg_classify.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				goneAllLayout();
				switch (checkedId) {
				case R.id.rb_course:
					if(classifyCoursrs!=null){
						showForCourse();
					}else{
						getOnlineCourseClassify();
					}
					break;
				case R.id.rb_area:
					if(classifyArea!=null){
						showForAreas();
					}else{
						getOnlineAreaClassify();
					}
					break;
				case R.id.rb_way:
					showForWay();
					break;
				case R.id.rb_price:
					showForPrice();
					break;
				case R.id.rb_sirceo:
					showForSirceo();
					break;
				}
			}
		});
		getOnlineData(NORMAL);
	}
	protected void showForPrice() {
		if(classifyprice==null){
			classifyprice = new ArrayList<String>();
			classifyprice.add("由低到高");
			classifyprice.add("由高到低");
		}
		lv_price.setAdapter(new ClassifyAdapter(classifyprice, mApplication, R.layout.item_just_one_text, true));
		layout_price.setVisibility(View.VISIBLE);
	}

	protected void showForSirceo() {
		if(classifySirceo==null){
			classifySirceo = new ArrayList<String>();
			classifySirceo.add("等级由高到低");
			classifySirceo.add("等级由低到高");
			classifySirceo.add("守约率由高到低");
		}
		lv_sirceo.setAdapter(new ClassifyAdapter(classifySirceo, mApplication, R.layout.item_just_one_text, true));
		layout_sirceo.setVisibility(View.VISIBLE);
	}

	protected void showForWay() {
		if(classifyWay==null){
			classifyWay = new ArrayList<String>();
			classifyWay.add("闲客上门");
			classifyWay.add("学客上门");
			classifyWay.add("网络授课");
		}
		lv_way.setAdapter(new ClassifyAdapter(classifyWay, mApplication, R.layout.item_just_one_text, true));
		layout_way.setVisibility(View.VISIBLE);
	}

	private void goneAllLayout() {
		layout_course.setVisibility(View.GONE);
		layout_area.setVisibility(View.GONE);
		layout_way.setVisibility(View.GONE);
		layout_sirceo.setVisibility(View.GONE);
		layout_price.setVisibility(View.GONE);
	}

	protected void getOnlineAreaClassify() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", mApplication.loginPhoneNum);
		api.post(URL.GET_AREAS, params, new SIRCEOResponseHandler(mActivity,false) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					classifyArea = JsonUtils.getListFromJSON(Areas.class, result.getJSONArray("areas").toString());
					showForAreas();
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

	protected void showForAreas() {
		List<String> strings = new ArrayList<String>();
		for(Areas area:classifyArea){
			strings.add(area.areaName);
		}
		lv_areas.setAdapter(new ClassifyAdapter(strings, mApplication, R.layout.item_just_one_text, true));
		layout_area.setVisibility(View.VISIBLE);
	}

	protected void getOnlineCourseClassify() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", mApplication.loginPhoneNum);
		api.post(URL.GET_COURSES_CLASSIFY, params, new SIRCEOResponseHandler(mActivity,false) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					classifyCoursrs = JsonUtils.getListFromJSON(CourseClassifyMainBean.class, result.getJSONArray("categorys").toString());
					showForCourse();
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

	protected void showForCourse() {
		List<String> mainStrings = new ArrayList<String>();
		for(CourseClassifyMainBean bean:classifyCoursrs){
			mainStrings.add(bean.ocName);
		}
		lv_course_main.setAdapter(new ClassifyAdapter(mainStrings, mApplication, R.layout.item_just_one_text, true));
		layout_course.setVisibility(View.VISIBLE);
	}

	private void getOnlineData(final int type) {
		RequestParams params = new RequestParams();
		params.put("phoneNum", mApplication.loginPhoneNum);
		if(page!=1){
			filter.setSelectTime(selectTime);
		}
		
		String jsonString = new Gson().toJson(filter);
		params.put("screenParam",jsonString);
		api.post(URL.GET_COURSES, params, new SIRCEOResponseHandler(mActivity,false) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					selectTime = result.getInt("selectTime");
					page++;
					switch (type) {
					case LOAD_MORE:
						abPullToRefreshView.onFooterLoadFinish();
						break;
					case REFRESH:
						abPullToRefreshView.onHeaderRefreshFinish();
						lists.clear();
						break;
					case NORMAL:
						lists.clear();
						break;
					}
					List<CourseDetialBean> newList = JsonUtils.getListFromJSON(CourseDetialBean.class, result.getJSONArray("courses").toString());
					lists.addAll(newList);
					adapter.notifyDataSetChanged();
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (parent.getId()) {
		case R.id.lv_course:
			CourseDetailActivity.startCourseDetailActivity(mActivity, lists.get(position).cid);
			break;
		case R.id.lv_main:
			mainPosition = position;
			List<String> secondStrings = new ArrayList<String>();
			for(CourseClassifyMainBean.SecondClassifyBean bean:classifyCoursrs.get(position).category){
				secondStrings.add(bean.tcName);
			}
			lv_course_second.setAdapter(new ClassifyAdapter(secondStrings, mApplication, R.layout.item_just_one_text, false));
			break;
		case R.id.lv_second:
			filter.setTcid(classifyCoursrs.get(mainPosition).category.get(position).tcid);
			getOnlineData(NORMAL);
			goneAllLayout();
			break;
		case R.id.lv_area:
			filter.setAid(classifyArea.get(position).aid);
			getOnlineData(NORMAL);
			goneAllLayout();
			break;
		case R.id.lv_way:
			filter.setTmid(position+1);
			getOnlineData(NORMAL);
			goneAllLayout();
			break;
		case R.id.lv_price:
			filter.setPriceSort(position==0?-1:1);
			getOnlineData(NORMAL);
			goneAllLayout();
			break;
		case R.id.lv_sirceo:
			filter.setLevelSort(position==0?-1:1);
			getOnlineData(NORMAL);
			goneAllLayout();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:
			try {
				int min = Integer.parseInt(et_min.getText().toString());
				int max = Integer.parseInt(et_max.getText().toString());
				if(min>=max){
					Toast.makeText(mApplication, "价格区间填写错误", Toast.LENGTH_SHORT).show();
					break;
				}
				filter.setMinMoney(min);
				filter.setMaxMoney(max);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
