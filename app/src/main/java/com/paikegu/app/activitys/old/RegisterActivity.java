package com.xiankezu.sirceo.activitys.old;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.globals.REGX;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText et_phone;
	private EditText et_code;
	private EditText et_passsword1;
	private EditText et_passsword2;

	private TextView tv_phone;
	private TextView tv_smscode;
	private TextView tv_passsword1;
	private TextView tv_passsword2;

	private Button btn_register;
	private TextView btn_get_sms;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	protected void findViews() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_phone.setFilters(REGX.getFilters(REGX.REGX_MOBILE_INPUT));
		et_code = (EditText) findViewById(R.id.et_code);
		et_passsword1 = (EditText) findViewById(R.id.et_passsword1);
		et_passsword2 = (EditText) findViewById(R.id.et_passsword2);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_smscode = (TextView) findViewById(R.id.tv_smscode);
		tv_passsword1 = (TextView) findViewById(R.id.tv_passsword1);
		tv_passsword2 = (TextView) findViewById(R.id.tv_passsword2);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_get_sms = (TextView) findViewById(R.id.btn_get_sms);
		btn_register.setOnClickListener(this);
		btn_get_sms.setOnClickListener(this);
	}

	@Override
	protected void initSomething() {
		findViewById(R.id.tv_back).setOnClickListener(this);
		
		et_code.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = et_code.getText().toString().length();
				if (length ==4) {
					tv_smscode.setBackgroundResource(R.drawable.icon_has_smscode);
				} else {
					tv_smscode.setBackgroundResource(R.drawable.icon_no_smscode);
				}
			}
		});

		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = et_phone.getText().toString().length();
				if (length == 11) {
					tv_phone.setBackgroundResource(R.drawable.icon_has_phone);
				} else {
					tv_phone.setBackgroundResource(R.drawable.icon_no_phone);
				}
			}
		});

		et_passsword1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = et_passsword1.getText().toString().length();
				if (length > 5 && length <= 32) {
					tv_passsword1.setBackgroundResource(R.drawable.icon_has_password);
				} else {
					tv_passsword1.setBackgroundResource(R.drawable.icon_no_password);
				}
			}
		});
	
		et_passsword2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				int length = et_passsword2.getText().toString().length();
				if (length > 5 && length <= 32) {
					tv_passsword2.setBackgroundResource(R.drawable.icon_has_password);
				} else {
					tv_passsword2.setBackgroundResource(R.drawable.icon_no_password);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_sms:
			getSMSCode();
			break;
		case R.id.btn_register:
			register();
			break;
		case R.id.tv_back:
			finish();
			break;
		}
	}

	private void getSMSCode() {
		if(et_phone.getText().toString().length()!=11){
			showShortToast("请输入正确的手机号码");
			return;
		}
		RequestParams params = new RequestParams();
		params.put("phoneNum", et_phone.getText().toString());
		api.post(URL.GET_SMS_CODE, params, new SIRCEOResponseHandler(this,
				false) {

			@Override
			public void onSuccess(JSONObject result) {
				try {
					String code = result.getString("smsCode");
					et_code.setText(code);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String result,int error) {
				switch (error) {
				case 1001:
					result = "手机号已存在";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				case 1002:
					result = "验证码获取失败";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
	}

	private void register() {
		if(et_phone.getText().toString().length()!=11){
			showShortToast("请输入正确的手机号码");
			return;
		}
		if(et_code.getText().toString().length()!=4){
			showShortToast("请输入短信验证码");
			return;
		}
		
		if(et_passsword1.getText().toString().length()<6){
			showShortToast("请输入正确的密码");
			return;
		}
		
		if(!et_passsword1.getText().toString().equals(et_passsword2.getText().toString())){
			showShortToast("两次密码输入不一致");
			return;
		}
		
		RequestParams params = new RequestParams();
		params.put("phoneNum", et_phone.getText().toString().trim());
		params.put("smsCode", et_code.getText().toString().trim());
		params.put("password", et_passsword1.getText().toString().trim());
		api.post(URL.REGISTER, params, new SIRCEOResponseHandler(this, true) {

			@Override
			public void onSuccess(JSONObject result) {
				Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onFailure(String result,int error) {
				switch (error) {
				case 1003:
					result = "验证码不能为空";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				case 1004:
					result = "密码不能为空";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				case 1005:
					result = "验证码不正确";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				case 1006:
					result = "注册失败";
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
	}

}
