package com.xiankezu.sirceo.activitys.old;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.CommonAdapter;
import com.xiankezu.sirceo.adapters.ViewHolder;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.JsonUtils;

public class CityChooseActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private final int GET_LOCATION = 0X001;
	private TextView tv_locateCity;
	private ListView lv_openCity;
	private CityAdapter adapter;
	private List<String> citys;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_LOCATION:
				if (TextUtils.isEmpty(application.city)){
					sendEmptyMessageDelayed(GET_LOCATION, 1000);
				}else{
					tv_locateCity.setText(application.city);
				}
				break;

			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city_choose);
	}

	@Override
	protected void findViews() {
		findViewById(R.id.tv_back).setOnClickListener(this);
		tv_locateCity = (TextView) findViewById(R.id.tv_name);
		tv_locateCity.setOnClickListener(this);
		tv_locateCity.setText("定位中");
		handler.sendEmptyMessage(GET_LOCATION);
		lv_openCity = (ListView) findViewById(R.id.lv_open_city);
		lv_openCity.setOnItemClickListener(this);
	}

	@Override
	protected void initSomething() {
		citys = new ArrayList<String>();
		adapter = new CityAdapter(citys, application.getApplicationContext(),
				R.layout.item_city);
		lv_openCity.setAdapter(adapter);
		getOnlineData();
	}

	private void getOnlineData() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", application.loginPhoneNum);
		api.post(URL.OPEN_CITYS, params, new SIRCEOResponseHandler(this, true) {

			@Override
			public void onSuccess(JSONObject result) {
				try {
					citys.addAll(JsonUtils.getListFromJSON(String.class, result
							.getJSONArray("citys").toString()));
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			setResult(Activity.RESULT_CANCELED);
			finish();
			break;
		case R.id.tv_name:
			Intent intent = new Intent();
			intent.putExtra("city", application.city);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		intent.putExtra("city", citys.get(arg2));
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	class CityAdapter extends CommonAdapter<String> {

		public CityAdapter(List<String> mDatas, Context mContext,
				int mItemLayoutId) {
			super(mDatas, mContext, mItemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder viewHolder, String item) {
			TextView tv_name = viewHolder.getView(R.id.tv_name);
			tv_name.setText(item);
		}

	}

}
