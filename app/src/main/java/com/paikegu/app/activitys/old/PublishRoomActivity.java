package com.xiankezu.sirceo.activitys.old;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.ProvinceCityAreaBean;
import com.xiankezu.sirceo.globals.IntentFileds;
import com.xiankezu.sirceo.globals.REGX;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.JsonUtils;
import com.xiankezu.sirceo.widghts.wheel.OnWheelChangedListener;
import com.xiankezu.sirceo.widghts.wheel.WheelView;
import com.xiankezu.sirceo.widghts.wheel.adapters.ArrayWheelAdapter;

public class PublishRoomActivity extends BaseActivity implements OnClickListener,OnWheelChangedListener,OnCheckedChangeListener{
	private Spinner sp;
	private PopupWindow popupWindow;
	private ImageView tv_back;
	
	private EditText et_detial_address;
	private EditText et_people_count;
	private EditText et_price_morning;
	private EditText et_price_center;
	private EditText et_price_night;
	private EditText et_name;
	private EditText et_phone;
	
	
	Map<String, Integer> mapTimes = new HashMap<String, Integer>();
	
	private TextView tv_location;
	private TextView tv_map_location;
	private double lat;
	private double lng;
	private List<ProvinceCityAreaBean> lists;
	private int p_s = 0;
	private int c_s = 0;
	private int a_s = 0;
	WheelView wheel_province;
	WheelView wheel_city;
	WheelView wheel_area;
	
	private String [] provinces;
	/** 
     * key - 省 value - 市 
     */  
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();  
    /** 
     * key - 市 values - 区 
     */  
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_room);
	}

	@Override
	protected void findViews() {
		
		et_detial_address = (EditText) findViewById(R.id.et_detial_address);
		et_people_count = (EditText) findViewById(R.id.et_people_count);
		et_price_morning = (EditText) findViewById(R.id.et_price_morning);
		et_price_center = (EditText) findViewById(R.id.et_price_center);
		et_price_night = (EditText) findViewById(R.id.et_price_night);
		et_name = (EditText) findViewById(R.id.et_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setFilters(REGX.getFilters(REGX.REGX_MOBILE_INPUT));
		
		((CheckBox)findViewById(R.id.cb_1_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_1_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_1_3)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_2_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_2_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_2_3)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_3_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_3_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_3_3)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_4_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_4_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_4_3)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_5_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_5_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_5_3)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_6_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_6_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_6_3)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_7_1)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_7_2)).setOnCheckedChangeListener(this);
		((CheckBox)findViewById(R.id.cb_7_3)).setOnCheckedChangeListener(this);
		findViewById(R.id.tv_publish).setOnClickListener(this);
		findViewById(R.id.tv_delete).setOnClickListener(this);
		
		sp = (Spinner) findViewById(R.id.sp);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_location.setOnClickListener(this);
		tv_map_location = (TextView) findViewById(R.id.tv_map_location);
		tv_map_location.setOnClickListener(this);
		tv_back = (ImageView) findViewById(R.id.tv_back);
		tv_back.setOnClickListener(this);
		sp.setAdapter(new ArrayAdapter<String>(application, R.layout.item_spinner, new String[]{"否","是"}));
	}

	@Override
	protected void initSomething() {
		mapTimes.put("isMonMorning",0);
		mapTimes.put("isMonAfternoon",0);
		mapTimes.put("isMonNight",0);
		mapTimes.put("isTueMorning",0);
		mapTimes.put("isTueAfternoon",0);
		mapTimes.put("isTueNight",0);
		mapTimes.put("isWedMorning",0);
		mapTimes.put("isWedAfternoon",0);
		mapTimes.put("isWedNight",0);
		mapTimes.put("isThuMorning",0);
		mapTimes.put("isThuAfternoon",0);
		mapTimes.put("isThuNight",0);
		mapTimes.put("isFriMorning",0);
		mapTimes.put("isFriAfternoon",0);
		mapTimes.put("isFriNight",0);
		mapTimes.put("isSatMorning",0);
		mapTimes.put("isSatAfternoon",0);
		mapTimes.put("isSatNight",0);
		mapTimes.put("isSunMorning",0);
		mapTimes.put("isSunAfternoon",0);
		mapTimes.put("isSunNight",0);
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mapTimes.put("isSeat", arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		getAreaOnline();
	}

	private void getAreaOnline() {
		if(lists!=null){
			showPopup();
			return;
		}
		
		RequestParams params = new RequestParams();
		params.put("phoneNum", application.loginPhoneNum);
		api.post(URL.GET_PROVINCE_CITY_AREA, params, new SIRCEOResponseHandler(this,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					lists = JsonUtils.getListFromJSON(ProvinceCityAreaBean.class, result.getJSONArray("provinces").toString());
					calculatedData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			private void calculatedData() {
				provinces  = new String[lists.size()];
				for(ProvinceCityAreaBean province : lists){
					provinces[lists.indexOf(province)] = province.provinceName;
					mCitisDatasMap.put(province.provinceName, p2c(province));
					for(ProvinceCityAreaBean.Citys citys:province.citys){
						mDistrictDatasMap.put(citys.cityName, c2a(citys));
					}
				}
				
				
			}
			/**
			 * 由省获得市
			 * @param bean
			 * @return
			 */
			private String[] p2c(ProvinceCityAreaBean bean){
				String[] cs = new String[bean.citys.size()];
				for(int i = 0 ; i <bean.citys.size();i++){
					cs[i] = bean.citys.get(i).cityName;
				}
				return cs;
			}
			/**
			 * 由市获得区
			 * @param bean
			 * @return
			 */
			private String[] c2a(ProvinceCityAreaBean.Citys citys){
				String[] cs = new String[citys.areas.size()];
				for(int i = 0 ; i <citys.areas.size();i++){
					cs[i] = citys.areas.get(i).areaName;
				}
				return cs;
			}

			@Override
			public void onFailure(String result,int error) {
				
			}
		});
	}

	/**
	 * 地址选择的popupwin
	 */
	private void showPopup() {

		if (popupWindow == null) {
			
			View view = getLayoutInflater()
					.inflate(R.layout.layout_pop_select_place, null);
			
			view.findViewById(R.id.tv_bg).setOnClickListener(this);
			wheel_province = (WheelView) view.findViewById(R.id.wheel_province);
			wheel_city = (WheelView) view.findViewById(R.id.wheel_city);
			wheel_area = (WheelView) view.findViewById(R.id.wheel_area);
	        wheel_province.addChangingListener(this);
	        wheel_city.addChangingListener(this);
	        wheel_area.addChangingListener(this);
	        // 设置可见条目数量
	        wheel_province.setVisibleItems(5);
	        wheel_city.setVisibleItems(5);
	        wheel_area.setVisibleItems(5);
	        //设置适配器
	        ArrayWheelAdapter<String> adapter_p = new ArrayWheelAdapter<String>(application, provinces);
	        adapter_p.setTextColor(0xFF000000);
	        wheel_province.setViewAdapter(adapter_p);
	        //设置适配器
	        ArrayWheelAdapter<String> adapter_c = new ArrayWheelAdapter<String>(application, mCitisDatasMap.get(provinces[p_s]));
	        adapter_c.setTextColor(0xFF000000);
	        wheel_city.setViewAdapter(adapter_c);
	        //设置适配器
	        ArrayWheelAdapter<String> adapter_a = new ArrayWheelAdapter<String>(application, mDistrictDatasMap.get(mCitisDatasMap.get(provinces[p_s])[c_s]));
	        adapter_a.setTextColor(0xFF000000);
	        wheel_area.setViewAdapter(adapter_a);
	        //设置默认选中
	        wheel_province.setCurrentItem(0);
	        wheel_city.setCurrentItem(0);
	        wheel_area.setCurrentItem(0);
	        
	        tv_location.setText(lists.get(p_s).provinceName+" "+
					lists.get(p_s).citys.get(0).cityName+" "+
					lists.get(p_s).citys.get(0).areas.get(0).areaName);
			
			popupWindow = new PopupWindow(this);
			popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setContentView(view);
		}

		popupWindow.showAtLocation(tv_back, Gravity.BOTTOM, 0, 0);
		popupWindow.update();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_location:
			showPopup();
			break;
		case R.id.tv_map_location:
			AddressChooseActivity.startAddressChooseActivity(this, IntentFileds.MAP_CHOOSE_LOCATION);
			break;
		case R.id.tv_bg:
			if(popupWindow.isShowing()){
				popupWindow.dismiss();
			}
			break;
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_publish:
			publishRoom();
			break;
		case R.id.tv_delete:
			deleteRoom();
			break;
			
		}
	}

	private void deleteRoom() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", application.loginPhoneNum);
		api.post(URL.DELETE_ROOM, params, new SIRCEOResponseHandler(this,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				showShortToast("闲客厅删除成功");
				finish();
			}
			
			@Override
			public void onFailure(String result,int error) {
				switch (error) {
				case 3010:
					showShortToast("用户没有发布闲客厅");
					break;
				default:
					showShortToast("删除闲客厅失败");
					break;
				}
			}
		});
	}

	private void publishRoom() {
		
		if("选择省市地".equals(tv_location.getText().toString())){
			showCustomToast("请选择省市地");
			return;
		}
		
		if(lat==0||lng==0){
			showCustomToast("请选择您的闲客厅地理位置");
			return;
		}
		
		if(TextUtils.isEmpty(et_detial_address.getText().toString())){
			showCustomToast("请输入闲客厅详细地址");
			return;
		}
		
		if(TextUtils.isEmpty(et_people_count.getText().toString())){
			showCustomToast("请输入闲客厅可容纳人数");
			return;
		}
		
		if(TextUtils.isEmpty(et_name.getText().toString())){
			showCustomToast("请输入姓名");
			return;
		}
		
		if(et_phone.getText().toString().length()!=11){
			showCustomToast("请输入正确的手机号码");
			return;
		}
		
		if(TextUtils.isEmpty(et_price_morning.getText().toString())||
		   TextUtils.isEmpty(et_price_center.getText().toString())||
		   TextUtils.isEmpty(et_price_night.getText().toString())){
			showCustomToast("请输入时间区间的价格");
			return;
		}
		
		if(!mapTimes.containsValue(1)){
			showCustomToast("请至少选择一个出租世间段");
			return;
		}
		
		RequestParams params = new RequestParams();
		params.put("phoneNum", application.loginPhoneNum);
		params.put("hallLongitude", lng);
		params.put("hallLatitude", lat);
		params.put("pid", lists.get(p_s).pid);
		params.put("cid", lists.get(p_s).citys.get(c_s).cid);
		params.put("aid", lists.get(p_s).citys.get(c_s).areas.get(a_s).aid);
		params.put("addressDescription", et_detial_address.getText().toString());
		params.put("peopleCount", Integer.parseInt(et_people_count.getText().toString()));
		params.put("userName", et_name.getText().toString());
		params.put("phone", Long.parseLong(et_phone.getText().toString()));
		params.put("morningPrice", Integer.parseInt(et_price_morning.getText().toString()));
		params.put("afternoonPrice", Integer.parseInt(et_price_center.getText().toString()));
		params.put("nightPrice", Integer.parseInt(et_price_night.getText().toString()));
		params.put("hallLatitude", lat);
		
		Iterator iter = mapTimes.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			int val = mapTimes.get(key);
			params.put(key, val);
		}
		api.post(URL.PUBLISH_ROOM, params, new SIRCEOResponseHandler(this,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				showShortToast("闲客厅发布成功");
				finish();
			}
			
			@Override
			public void onFailure(String result,int error) {
				switch (error) {
				case 1011:
					showShortToast("用户被冻结");
					break;
				case 3003:
					showShortToast("闲客厅具体地址长度错误");
					break;
				case 3004:
					showShortToast("闲客厅容纳人数错误");
					break;
				case 3005:
					showShortToast("用户姓名不正确");
					break;
				case 3006:
					showShortToast("手机号格式不正确");
					break;
				case 3007:
					showShortToast("价格不正确");
					break;
				case 3009:
					showShortToast("不能重复发布闲客厅");
					break;
				default :
					showShortToast("发布闲客厅失败");
					break;
				}
			}
		});
		
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		 ArrayWheelAdapter<String> adapter_a;
		 
		switch (wheel.getId()) {
		case R.id.wheel_province:
			p_s = newValue;
			c_s = 0;
			 //设置适配器
	        ArrayWheelAdapter<String> adapter_c = new ArrayWheelAdapter<String>(application, mCitisDatasMap.get(provinces[p_s]));
	        adapter_c.setTextColor(0xFF000000);
	        wheel_city.setViewAdapter(adapter_c);
	        
	        //设置适配器
	        adapter_a = new ArrayWheelAdapter<String>(application, mDistrictDatasMap.get(mCitisDatasMap.get(provinces[p_s])[c_s]));
	        adapter_a.setTextColor(0xFF000000);
	        wheel_area.setViewAdapter(adapter_a);
	        
	        wheel_city.setCurrentItem(0);
	        wheel_area.setCurrentItem(0);
	        
	        tv_location.setText(lists.get(p_s).provinceName+" "+
					lists.get(p_s).citys.get(0).cityName+" "+
					lists.get(p_s).citys.get(0).areas.get(0).areaName);
	        
			break;
		case R.id.wheel_city:
			c_s = newValue;
			 //设置适配器
	        adapter_a = new ArrayWheelAdapter<String>(application, mDistrictDatasMap.get(mCitisDatasMap.get(provinces[p_s])[c_s]));
	        adapter_a.setTextColor(0xFF000000);
	        wheel_area.setViewAdapter(adapter_a);
	        wheel_area.setCurrentItem(0);
	        
	        tv_location.setText(lists.get(p_s).provinceName+" "+
					lists.get(p_s).citys.get(c_s).cityName+" "+
					lists.get(p_s).citys.get(c_s).areas.get(0).areaName);
	        
			break;
		case R.id.wheel_area:
			a_s = newValue;
			tv_location.setText(lists.get(p_s).provinceName+" "+
								lists.get(p_s).citys.get(c_s).cityName+" "+
								lists.get(p_s).citys.get(c_s).areas.get(newValue).areaName);
			break;
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode!= RESULT_OK){
			return;
		}
		
		switch (requestCode) {
		case IntentFileds.MAP_CHOOSE_LOCATION:
			lat = data.getDoubleExtra("lat", 0);
			lng = data.getDoubleExtra("lng", 0);
			tv_map_location.setText(data.getStringExtra("string"));
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cb_1_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");
				mapTimes.put("isMonMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isMonMorning", 0);
			}
			break;
		case R.id.cb_1_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");
				mapTimes.put("isMonAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isMonAfternoon", 0);
			}
			break;
		case R.id.cb_1_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");	
				mapTimes.put("isMonNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isMonNight", 0);
			}
			break;
		case R.id.cb_2_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");	
				mapTimes.put("isTueMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isTueMorning", 0);
			}
			break;
		case R.id.cb_2_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");				
				mapTimes.put("isTueAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isTueAfternoon", 0);
			}
			break;
		case R.id.cb_2_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");				
				mapTimes.put("isTueNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isTueNight", 0);
			}
			break;
		case R.id.cb_3_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");				
				mapTimes.put("isWedMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isWedMorning", 0);
			}
			break;
		case R.id.cb_3_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");				
				mapTimes.put("isWedAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isWedAfternoon", 0);
			}
			break;
		case R.id.cb_3_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");				
				mapTimes.put("isWedNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isWedNight", 0);
			}
			break;
		case R.id.cb_4_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");				
				mapTimes.put("isThuMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isThuMorning", 0);
			}
			break;
		case R.id.cb_4_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");				
				mapTimes.put("isThuAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isThuAfternoon", 0);
			}
			break;
		case R.id.cb_4_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");				
				mapTimes.put("isThuNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isThuNight", 0);
			}
			break;
		case R.id.cb_5_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");				
				mapTimes.put("isFriMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isFriMorning", 0);
			}
			break;
		case R.id.cb_5_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");				
				mapTimes.put("isFriAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isFriAfternoon", 0);
			}
			break;
		case R.id.cb_5_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");				
				mapTimes.put("isFriNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isFriNight", 0);
			}
			break;
		case R.id.cb_6_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");				
				mapTimes.put("isSatMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isSatMorning", 0);
			}
			break;
		case R.id.cb_6_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");				
				mapTimes.put("isSatAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isSatAfternoon", 0);
			}
			break;
		case R.id.cb_6_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");				
				mapTimes.put("isSatNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isSatNight", 0);
			}
			break;
		case R.id.cb_7_1:
			if(isChecked){
				buttonView.setText("09:00-13:00\n可出租");				
				mapTimes.put("isSunMorning", 1);
			}else{
				buttonView.setText("09:00-13:00\n不可出租");				
				mapTimes.put("isSunMorning", 0);
			}
			break;
		case R.id.cb_7_2:
			if(isChecked){
				buttonView.setText("13:00-17:00\n可出租");				
				mapTimes.put("isSunAfternoon", 1);
			}else{
				buttonView.setText("13:00-17:00\n不可出租");				
				mapTimes.put("isSunAfternoon", 0);
			}
			break;
		case R.id.cb_7_3:
			if(isChecked){
				buttonView.setText("17:00-21:00\n可出租");				
				mapTimes.put("isSunNight", 1);
			}else{
				buttonView.setText("17:00-21:00\n不可出租");				
				mapTimes.put("isSunNight", 0);
			}
			break;
		}
	}
	
}
