package com.xiankezu.sirceo.activitys.old;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.globals.StaticCode;
import com.xiankezu.sirceo.globals.REGX;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.BlurUtils;
import com.xiankezu.sirceo.tools.ContentUtils;

public class LogInActivity extends BaseActivity implements OnClickListener{
	private ImageView im_bg;
	
	private EditText et_phone;
	private EditText et_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	@Override
	protected void findViews() {
		im_bg = (ImageView) findViewById(R.id.im_bg);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_password = (EditText) findViewById(R.id.et_password);
	}

	@Override
	protected void initSomething() {
		et_phone.setText(ContentUtils.getSharePreString(getApplicationContext(), StaticCode.USER_TABLE, StaticCode.USER_PHONE));
		et_password.setText(ContentUtils.getSharePreString(getApplicationContext(), StaticCode.USER_TABLE, StaticCode.USER_PASSWORD));
		et_phone.setFilters(REGX.getFilters(REGX.REGX_MOBILE_INPUT));
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.tv_register).setOnClickListener(this);
		findViewById(R.id.tv_password_back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			logIn();
			break;
		case R.id.tv_register:
			startActivity(RegisterActivity.class);
			break;
		case R.id.tv_password_back:
			break;
		}
	}


	private void logIn() {
		if(et_phone.getText().toString().length()!=11){
			showCustomToast("手机号码错误");
			return;
		}
		
		if(et_password.getText().toString().length()==0){
			showCustomToast("请输入密码");
			return;
		}
		
		RequestParams params = new RequestParams();
		params.put("phoneNum", et_phone.getText().toString().trim());
		params.put("password", et_password.getText().toString().trim());
		api.post(URL.LOGIN, params, new SIRCEOResponseHandler(this,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				try {
					String sessionId = result.getString("sessionId");
					application.setSessionId(sessionId);
					ContentUtils.putSharePreString(getApplicationContext(), StaticCode.USER_TABLE, StaticCode.USER_PHONE, et_phone.getText().toString().trim());
					ContentUtils.putSharePreString(getApplicationContext(), StaticCode.USER_TABLE, StaticCode.USER_PASSWORD, et_password.getText().toString().trim());
					startActivity(MenuActivity.class);
					application.loginPhoneNum = Long.parseLong(et_phone.getText().toString().trim());
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(String result,int error) {
				switch (error) {
				case 1007:
					result = "密码不正确";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				case 1008:
					result = "登录失败";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				case 1010:
					result = "用户未注册";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
	}

}
