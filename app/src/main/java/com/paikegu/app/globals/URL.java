package com.xiankezu.sirceo.globals;

import java.io.File;

/**
 * Author: wangzhenfei 
 * Description: 项目中所有的URL
 */
public class URL {

	/**
	 * 总控制开关
	 */
	private static final  boolean DEBUG = false;
	
	/**
	 * 线上地址
	 */
	private static final String ONLINE_ADDRESS = "http://www.xiankezu.com:8081/xiankezu/";
	
	/**
	 * 测试地址
	 */
	private static final String DEBUG_ADDRESS = "http://192.168.2.110:8080/xiankezu/";
	
	/**
	 * 获取真实地址
	 */
	public static String ROOT = DEBUG ? DEBUG_ADDRESS:ONLINE_ADDRESS;
	
	
	//*****************************登录注册*****************************
	/**
	 * 获取验证码
	 */
	public static final String GET_SMS_CODE = ROOT+"getSMSCode.do";
	/**
	 * 登录
	 */
	public static final String LOGIN= ROOT+"login.do";
	/**
	 * 注册
	 */
	public static final String REGISTER = ROOT+"register.do";
	/**
	 * 首页
	 */
	public static final String HOMEPAGE = ROOT+"login/getHomePage.do";
	
	/**
	 * 已开发的城市
	 */
	public static final String OPEN_CITYS = ROOT+"login/getOpenCity.do";
	/**
	 * 	根据昵称获取用户
	 */
	public static final String GET_USER = ROOT+"login/getUser.do";
	/**
	 * 	根据课程id获取课程详情
	 */
	public static final String GET_COURSE_DETAILS = ROOT+"login/getCourseDetails.do";
	/**
	 * 	获取区域
	 */
	public static final String GET_AREAS = ROOT+"login/getAreas.do";
	/**
	 * 	获取课程选项接口
	 */
	public static final String GET_COURSES_CLASSIFY = ROOT+"login/getCategorys.do";
	/**
	 * 	获取课程
	 */
	public static final String GET_COURSES = ROOT+"login/getCoursesByCondition.do";
	/**
	 * 	获取闲客厅省市区级联接口
	 */
	public static final String GET_PROVINCE_CITY_AREA = ROOT+"login/getProvinceDistrict.do";
	/**
	 * 	发布闲客厅
	 */
	public static final String PUBLISH_ROOM = ROOT+"login/releaseHall.do";
	/**
	 * 	删除闲客厅接口
	 */
	public static final String DELETE_ROOM = ROOT+"login/removeHall.do";
	/**
	 * 	上传用户头像接口
	 */
	public static final String PUSH_MY_AVATAR = ROOT+"login/uploadAvatar.do";
	
	
	/**
	 * SD卡存储路径
	 */
	public static String SD_CARD_PATH;

	static {
		SD_CARD_PATH = android.os.Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + "DouQu" + File.separator;
		File file = new File(android.os.Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator + "DouQu");
		if (!file.exists()) {
			file.mkdir();
		}

		file = new File(URL.SD_CARD_PATH + "image");
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(URL.SD_CARD_PATH + "voice");
		if (!file.exists()) {
			file.mkdirs();
		}
	}
}
