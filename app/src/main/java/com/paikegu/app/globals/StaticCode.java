package com.xiankezu.sirceo.globals;

import android.R.integer;


/**
 * JSON的返回码
 * @author zhenfei.wang
 *
 */
public class StaticCode {
	//成功
	public final static int SUCCESS = 200;
	//系统错误
	public final static int UNCONNECTED = 404;
	
	//错误码
	public final static int PHINE_NUM_EXIST = 1001;
	public final static int SMS_CODE_FAILURE = 1002;
	
	public final static int SMS_NULL = 1003;
	public final static int PASSWORD_NULL = 1004;
	public final static int SMS_CODE_ERROR = 1005;
	public final static int REGISTER_FAILURE = 1006;
	
	public final static int PASSWORD_ERROR = 1007;
	public final static int LOGIN_ERROR = 1008;
	
	
	
	
	public static String getErrorCode(int type){
		switch (type) {
		case PHINE_NUM_EXIST:
			return "手机号已存在";
			
		case SMS_CODE_FAILURE:
			return "验证码获取失败";
			
		case SMS_NULL:
			return "短信不能为空";
			
		case PASSWORD_NULL:
			return "密码不能为空";
			
		case SMS_CODE_ERROR:
			return "验证码不正确";
			
		case REGISTER_FAILURE:
			return "注册失败";
			
		case PASSWORD_ERROR:
			return "密码不正确";
			
		case LOGIN_ERROR:
			return "登录失败";
			
		}
		return "";
	}
	
	
	/**
	 * SharedPreferences 字段
	 */
	public static final String USER_TABLE = "USER";
	
	public static final String USER_PHONE = "PHONE";
	
	public static final String USER_PASSWORD = "PASSWORD";
}
