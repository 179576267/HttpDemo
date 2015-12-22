package com.xiankezu.sirceo.fragments;

import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.globals.MyApplication;
import com.xiankezu.sirceo.https.CacheRestTemplate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 基础的fragment
 * @author zhenfei.wang
 */
public abstract class BaseFragment extends Fragment {
	private int resource;
	
	protected MyApplication mApplication;
	protected BaseActivity mActivity;
	protected View layout;
	
	/**
	 * api工具
	 */
	protected CacheRestTemplate api;
	
	public BaseFragment(int resource) {
		super();
		this.resource = resource;
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = (BaseActivity) activity;
		mApplication = (MyApplication) activity.getApplication();
		api = new CacheRestTemplate(mApplication);
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (layout == null) {
			layout = mActivity.getLayoutInflater().inflate(resource,
					null);
			initView();
			initSomething();
		} else {
			ViewGroup parent = (ViewGroup) layout.getParent();
			if (parent != null) {
				parent.removeView(layout);
			}
		}
		return layout;
	}
	
	protected abstract void initView();
	protected abstract void initSomething();
	
}
