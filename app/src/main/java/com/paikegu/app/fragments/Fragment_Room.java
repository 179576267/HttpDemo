package com.xiankezu.sirceo.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.old.PublishRoomActivity;
import com.xiankezu.sirceo.activitys.old.SearchTeacherActivity;

/**
 * 闲客厅页面
 * @author zhenfei.wang
 */
public class Fragment_Room extends BaseFragment implements OnClickListener{
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	
	public Fragment_Room(int resource) {
		super(resource);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void initView() {
		layout.findViewById(R.id.tv_publish).setOnClickListener(this);
		
		layout.findViewById(R.id.tv_mylocation).setOnClickListener(this);
		mMapView = (MapView) layout.findViewById(R.id.id_bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12.0f);// 500米的比例尺
		mBaiduMap.setMapStatus(msu);
		mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
		mMapView.removeViewAt(2);// 隐藏地图上比例尺
		View child = mMapView.getChildAt(1);
		// 隐藏百度logo和缩放控件ZoomControl
		if (child instanceof ImageView || child instanceof ZoomControls) {
			child.setVisibility(View.INVISIBLE);
		}
		moveMyLocation();
	}
	
	
	
	
	private void moveMyLocation() {
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(
				mApplication.latitude, mApplication.longitude));
		mBaiduMap.animateMapStatus(msu);
	}

	@Override
	protected void initSomething() {
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				createPopForRoomDetial();
				return false;
			}
		});
		OverlayOptions options = new MarkerOptions().position(new LatLng(mApplication.latitude, mApplication.longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_locationpin)).zIndex(5);
		Marker marker = (Marker) mBaiduMap.addOverlay(options);
	}

	protected void createPopForRoomDetial() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
		alertDialog.setView(LayoutInflater.from(mActivity).inflate(R.layout.layout_pop_room_detial, null, false));
		alertDialog.setCancelable(true);
		alertDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_mylocation:
			moveMyLocation();
			break;
		case R.id.tv_publish:
			startActivity(new Intent(mApplication.getApplicationContext(), PublishRoomActivity.class));
			break;
		}
	}
	
}
