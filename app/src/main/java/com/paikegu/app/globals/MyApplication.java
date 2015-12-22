package com.xiankezu.sirceo.globals;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;
import com.xiankezu.sirceo.singlefunction.PictureCache;

import android.annotation.SuppressLint;
import android.app.Application;

/**
 * @author zhenfei.wang
 */
public class MyApplication extends Application {

	/**
	 * sessionid
	 */
	private  String sessionId = "";
	
	public long loginPhoneNum;
	
	
	public  String getSessionId() {
		return sessionId;
	}

	public  void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 图片缓存类
	 */
	private PictureCache mPictureCache;
	public PictureCache getPictureCache() {
		if(mPictureCache==null){
			mPictureCache = new PictureCache(this);
		}
		return mPictureCache;
	}

	/**
	 * 全局的地址信息
	 */
	public double latitude = 31.018907;
	public double longitude = 121.24018;
	public String province = null;
	public String city= null;
	public String street= null;
	
	/**
	 * 数据库操作类
	 */
	public DatabaseHelper mDatabaseHelper;


	/**
	 * 这个才是程序的入口，这个类在不必要的情况下不要做修改
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(getApplicationContext());
		
		CrashReport.initCrashReport(this, "900010630", false);
//		CrashReport.testJavaCrash();
		myLocation();
		mDatabaseHelper = new DatabaseHelper(this);
	}

	/**
	 * 获得我的位置,登录要用
	 */
	private void myLocation() {
		final LocationClient mLocClient = new LocationClient(
				getApplicationContext());
		mLocClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null) {
					return;
				}
				street = location.getStreet() + location.getStreetNumber();
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				city = location.getCity();
				province = location.getProvince();
				mLocClient.stop();//目前值进行一次定位
			}
		});// 注册定位监听接口
		/**
		 * 设置定位参数
		 */
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开GPRS
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		mLocClient.setLocOption(option);
		mLocClient.start(); // 调用此方法开始定位
		mLocClient.requestLocation();

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}
}
