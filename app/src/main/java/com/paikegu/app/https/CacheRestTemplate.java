package com.xiankezu.sirceo.https;

import java.util.HashMap;
import java.util.Map;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.globals.MyApplication;
import com.xiankezu.sirceo.globals.StaticCode;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

public class CacheRestTemplate extends RestTemplate {
	private static final String TAG = "CacheRestTemplate";

	/**
	 * 网络连接状态
	 * TelephonyManager.DATA_DISCONNECTED// 网络断开
	 * TelephonyManager.DATA_CONNECTING// 网络正在连接
	 * case TelephonyManager.DATA_CONNECTED// 网络连接上
	 */
	private static int 			webConnectionStatus;
	
	private MyApplication application;
	public static void setWebConnectionStatus(int status){
		webConnectionStatus = status;
	}

	private static HashMap<String, RequestParams> requestMap;
	private static HashMap<String, String> responseMap;
	
	public CacheRestTemplate(MyApplication app) {
		super(app.getApplicationContext());
		this.application = app;
		if(requestMap == null)requestMap = new HashMap<String, RequestParams>();
		if(responseMap == null)responseMap = new HashMap<String, String>();
	}

	protected boolean dealWebConnection(){
		return getNetworkState();
	}

	/**
	 * 重复请求处理
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param url
	 * @param params
	 * @param allowRepeat
	 */
	protected boolean dealRepeat(String url, RequestParams params, boolean allowRepeat){
		if(!allowRepeat){
			if(hasRequested(url, params)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 缓存请求处理
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @param allowCache
	 * @return
	 */
	protected boolean dealCache(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, boolean allowCache){
		MezoneRequestContent requester = new MezoneRequestContent(url, params);
		SIRCEOResponseHandler responser = null;
		try {
			responser = (SIRCEOResponseHandler) responseHandler;
		} catch (Exception e) {
			return true;
		}
		if(responser != null){
			responser.setResponseContent(requester);
		}
		
		if(allowCache){
			if(requester != null){
				String result = getResultCache(requester);
				if(result != null){
					boolean isContinue = responser.onCacheSuccess(result);
					return isContinue;
				}
			}
		}
		return true;
	}

	/**
	 * GET请求，默认允许重复请求，默认允许使用缓存
	 */
	@Override
	public void get(String url, Map<String, String> map,AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams(map);
		get(url, params, responseHandler);
	}
	
	/**
	 * GET请求，默认允许重复请求，默认允许使用缓存
	 */
	@Override
	public void get(String url, RequestParams params,AsyncHttpResponseHandler responseHandler) {
		get(url, params, responseHandler, true, true);
	}

	/**
	 * GET请求，处理重复请求，处理缓存请求
	 */
	public void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, boolean allowRepeat, boolean allowCache) {
		
		boolean isContinue = dealWebConnection();
		if(!isContinue){
			if(responseHandler instanceof SIRCEOResponseHandler){
				SIRCEOResponseHandler handler = (SIRCEOResponseHandler) responseHandler;
				handler.onFailure("网络连接失败，请检查网络连接后再次尝试",StaticCode.UNCONNECTED);
			}else{
				Log.e(TAG, "网络连接失败，请检查网络连接后再次尝试");
			}
			return;
		}
		
		isContinue = dealRepeat(url, params, allowRepeat);
		if(!isContinue){
			return;
		}
		
		isContinue = dealCache(url, params, responseHandler, allowCache);
		if(!isContinue){
			return;
		}
		
		requestMap.put(url, params);
		super.get(url, params, responseHandler);
	}

	/**
	 * POST请求，默认允许重复请求，默认不允许使用缓存
	 */
	@Override
	public void post(String url, Map<String, String> map, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams(map);
		post(url, params, responseHandler);
	}

	/**
	 * POST请求，默认允许重复请求，默认不允许使用缓存
	 */
	@Override
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		if(!TextUtils.isEmpty(application.getSessionId())){
			params.put("sessionId", application.getSessionId());
		}
		post(url, params, responseHandler, true, false);
	}

	/**
	 * POST请求，处理重复请求，处理缓存请求
	 */
	public void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, boolean allowRepeat, boolean allowCache) {
		boolean isContinue = dealWebConnection();
		if(!isContinue){
			if(responseHandler != null && responseHandler.getClass() == SIRCEOResponseHandler.class){
				SIRCEOResponseHandler handler = (SIRCEOResponseHandler) responseHandler;
				handler.onFailure("网络连接失败，请检查网络连接后再次尝试",StaticCode.UNCONNECTED);
			}else{
				Log.e(TAG, "网络连接失败，请检查网络连接后再次尝试");
			}
			return;
		}
		
		isContinue = dealRepeat(url, params, allowRepeat);
		if(!isContinue){
			return;
		}
		
		requestMap.put(url, params);
		super.post(url, params, responseHandler);
	}

	/**
	 * 获得是否已有重复请求
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param url
	 * @param params
	 * @return
	 */
	protected boolean hasRequested(String url, RequestParams params){
		String key = parseUrl(url, params);
		return requestMap.containsKey(key);
	}
	
	/**
	 * 清除请求记录
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param url
	 */
	public void clearHistory(MezoneRequestContent requester){
		String key = parseUrl(requester);
		if(requestMap.containsKey(key)){
			requestMap.remove(key);
		}
	}
	
	/**
	 * 清除缓存对象
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param url
	 */
	public void clearRequestCache(MezoneRequestContent requester){
		String key = parseUrl(requester);
		if(responseMap.containsKey(key)){
			responseMap.remove(key);
		}
	}
	
	/**
	 * 获得缓存对象
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param requester
	 * @return
	 */
	public String getResultCache(MezoneRequestContent requester){
		if(requester != null){
			String key = parseUrl(requester);
			if(responseMap.containsKey(key)){
				String result = responseMap.get(key);
				return result;
			}
		}
		return null;
	}
	
	/**
	 * 放入缓存对象
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param requester
	 * @param result
	 */
	public void putResultCache(MezoneRequestContent requester, String result){
		if(requester != null && result != null){
			String key = parseUrl(requester);
			responseMap.put(key, result);
		}
	}
	
	private String parseUrl(MezoneRequestContent requester){
		if(requester == null) return "";
		return parseUrl(requester.getUrl(), requester.getParams());
	}
	
	private String parseUrl(String url, RequestParams params){
		if(url == null || params == null) return "";
		return url + params.toString();
	}
	
	 private boolean getNetworkState() {
	       ConnectivityManager manager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);  
	       NetworkInfo activeInfo = manager.getActiveNetworkInfo();
	       if (activeInfo != null && activeInfo.isConnectedOrConnecting()) {
	           return true;
	       }
	       
	       return false;
	   }
}
