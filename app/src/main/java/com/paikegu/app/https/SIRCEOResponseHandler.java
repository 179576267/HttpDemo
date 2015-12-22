package com.xiankezu.sirceo.https;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.globals.StaticCode;
import com.xiankezu.sirceo.widghts.FlippingLoadingDialog;

/**
 * 返回消息处理
 * @author shaowei.ma
 * @date 2014年9月23日
 * @param <T>
 */
public abstract class SIRCEOResponseHandler extends AsyncHttpResponseHandler{
	
	
	private Activity		activity;
	private CacheRestTemplate api;
	private MezoneRequestContent responseContent;
	
	private boolean showDialog;
	private FlippingLoadingDialog dialog;
	
	public SIRCEOResponseHandler(Activity activity,boolean show){
		this.activity = activity;
		this.showDialog = show;
		try {
			BaseActivity mzAct = (BaseActivity) activity;
			api = mzAct.getApi();
		} catch (Exception e) {
		}
	}

	public MezoneRequestContent getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(MezoneRequestContent responseContent) {
		this.responseContent = responseContent;
	}

	/**
	 * 获得缓存成功，直接返回缓存对象
	 * @author shaowei.ma
	 * @date 2014年10月29日
	 * @param result
	 * @return 是否在取得缓存对象后继续发送请求
	 */
	public boolean onCacheSuccess(String result){
		return true;
	}
	
	/**
	 * 提交进度
	 */
	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		super.onProgress(bytesWritten, totalSize);
	}
	
	/**
	 * 提交取消
	 */
	@Override
	public void onCancel() {
		super.onCancel();
	}
	
	/**
	 * 再次提交
	 */
	@Override
	public void onRetry(int retryNo) {
		super.onRetry(retryNo);
	}
	
	
	@Override
	public void onStart() {
		if(showDialog){
			if(dialog == null){
				dialog = new FlippingLoadingDialog(activity, "数据加载中...");
			}
			dialog.show();
		}
		super.onStart();
	}
	
	/**
	 * 流程结束
	 */
	@Override
	public void onFinish() {
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
		}
		
		if(responseContent != null && api != null)api.clearHistory(responseContent);
		super.onFinish();
	}
	
	
	/**
	 * 因为服务器框架原因需要进行封装
	 */
	@Override
	public final void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		String json = new String(responseBody);
		try {
			JSONObject jsonObject = new JSONObject(json);
			statusCode = Integer.parseInt(jsonObject.getString("resultCode"));
			if(statusCode == 200){
				if(TextUtils.isEmpty(json)){
					onFailure("服务器返回空字符串",statusCode);
					return;
				}else{
					onSuccess(jsonObject);
				}
			}else{
				onFailure(StaticCode.getErrorCode(statusCode), statusCode);
			}
		} catch (Exception e1) {
			onFailure("json解析异常",statusCode);
		}
		
	}
	
	/**
	 * 因为服务器框架原因进行封装
	 */
	@Override
	public final void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
		
		String json = null;
		if(responseBody == null || responseBody.length == 0) json = "";
		else json = new String(responseBody);
		if(TextUtils.isEmpty(json)){
			onFailure("未连接到服务器",statusCode);
			return;
		}
	}
	
	/**
	 * 提交成功，返回成功消息
	 * @author zhenfei.wang
	 * @date 2015年8月31日
	 * @param result
	 */
	public abstract void onSuccess(JSONObject result);
	
	/**
	 * 提交失败，返回失败字段消息
	 * @author zhenfei.wang
	 * @date 2015年8月31日
	 * @param result
	 */
	public abstract void onFailure(String result,int errorCode);
	
}