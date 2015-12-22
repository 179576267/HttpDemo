package com.xiankezu.sirceo.activitys.old;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.adapters.SearchTeacherAdapter;
import com.xiankezu.sirceo.beans.User;
import com.xiankezu.sirceo.globals.StaticCode;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.ContentUtils;
import com.xiankezu.sirceo.tools.JsonUtils;

public class SearchTeacherActivity extends BaseActivity implements OnClickListener{
	private EditText et;
	private ListView lv;
	private List<User> list;
	private SearchTeacherAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_teacher);
	}
	
	
	@Override
	protected void findViews() {
		lv = (ListView) findViewById(R.id.lv);
		et = (EditText) findViewById(R.id.et);
		findViewById(R.id.im_back).setOnClickListener(this);
	}

	@Override
	protected void initSomething() {
		list = new ArrayList<User>();
		adapter = new SearchTeacherAdapter(list, application, R.layout.item_search_teacher);
		lv.setAdapter(adapter);
		et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(et.getText().toString().length()>0){
					getOnlineData();
				}
			}
		});
	}

	protected void getOnlineData() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", application.loginPhoneNum);
		params.put("nickName", et.getText().toString().trim());
		api.post(URL.GET_USER, params, new SIRCEOResponseHandler(this,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					JSONArray users = result.getJSONArray("users");
					List<User> newList = JsonUtils.getListFromJSON(User.class, users.toString());
					list.clear();
					list.addAll(newList);
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
		case R.id.im_back:
			finish();
			break;
		}
	}

}
