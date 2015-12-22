package com.xiankezu.sirceo.activitys.old;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.globals.IntentFileds;
import com.xiankezu.sirceo.widghts.FocusableTextView;

public class AddressChooseActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {
    private final String TAG = AddressChooseActivity.this.getClass().getSimpleName();
    private Button b_back;                          //返回
    private Button b_confirm;                       //确定
    private MapView mMapView;                       //地图
    private FocusableTextView tv_address;           //当前地址
    private View progressDialog;                    //当前dialog

    private BaiduMap mBaiduMap;                     // 地图的控制类
    GeoCoder mSearch = null;                        // 搜索模块
    private boolean isBackLocation = true;          //是否是定位当前位置到中心点的操作
    
    private LatLng latLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_address_choose_activity);
        initView();
        initMap();
        initData();
    }

    @Override
    protected void onPause() {
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        //隐藏dialog
        if (progressDialog != null && progressDialog.isShown()) {
            progressDialog.setVisibility(View.INVISIBLE);
        }

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        showLocationInCenter(result.getLocation().latitude, result.getLocation().longitude);
        //搜索详细信息
        reverseGeo(result.getLocation());
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        if (progressDialog != null && progressDialog.isShown()) {
            progressDialog.setVisibility(View.INVISIBLE);
        }

        tv_address.setText(result.getAddress());
        latLng = result.getLocation();
        
    }


    /**
     * 根据经纬度搜索详细地址
     *
     * @param latlng
     */
    private void reverseGeo(LatLng latlng) {
        if (progressDialog != null && !progressDialog.isShown()) {
            progressDialog.setVisibility(View.VISIBLE);
        }
        // 反Geo搜索
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latlng));
    }

    /**
     * 解析地址的经纬度
     *
     * @param city
     * @param key
     */
    private void geocode(String city, String key) {
        if (progressDialog != null) {
            progressDialog.setVisibility(View.VISIBLE);
        }
        mSearch.geocode(new GeoCodeOption().city(
                city).address(
                key));
    }

    /**
     * 返回到我的地址
     */
    private void backToMyLocation() {
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(
                application.latitude, application.longitude));
        isBackLocation = true;
        mBaiduMap.animateMapStatus(msu);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        backToMyLocation();
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        mSearch = GeoCoder.newInstance();       // 初始化搜索模块，注册事件监听
        mSearch.setOnGetGeoCodeResultListener(this);

        mBaiduMap = mMapView.getMap();       // 获取地图控件引用
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);// 500米的比例尺
        mBaiduMap.setMapStatus(msu);
        mMapView.removeViewAt(1);// 隐藏地图上百度地图logo图标
        mMapView.removeViewAt(2);// 隐藏地图上比例尺
        View child = mMapView.getChildAt(1);
        // 隐藏百度logo和缩放控件ZoomControl
        if (child instanceof ImageView || child instanceof ZoomControls) {
            child.setVisibility(View.INVISIBLE);
        }


        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            //手势操作地图，设置地图状态等操作导致地图状态开始改变。
            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
            }

            //地图状态改变结束
            @Override
            public void onMapStatusChangeFinish(MapStatus mMapStatus) {
                reverseGeo(getLatLngInMapCenter());
            }

            //地图状态变化中
            @Override
            public void onMapStatusChange(MapStatus mMapStatus) {
                mBaiduMap.hideInfoWindow();                 //将infowindow隐藏
            }
        });


    }

    private void initView() {
        progressDialog = findViewById(R.id.progress);
        b_back = (Button) findViewById(R.id.b_back);
        b_back.setOnClickListener(this);
        b_confirm = (Button) findViewById(R.id.b_confirm);
        b_confirm.setOnClickListener(this);
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        tv_address = (FocusableTextView) findViewById(R.id.tv_address);
        tv_address.setOnClickListener(this);
        progressDialog.setVisibility(View.INVISIBLE);
    }



    /**
     * 根据经纬度，将地图移动到以经纬度为中心的位置
     *
     * @param latitude
     * @param longitude
     */
    private void showLocationInCenter(double latitude, double longitude) {
        LatLng latLng = null;                                       // 记录经纬度
        latLng = new LatLng(latitude, longitude);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f); // 500米的比例尺
        mBaiduMap.animateMapStatus(msu);
    }

    /**
     * 根据百度地图的中心点获取经纬度信息
     *
     * @return
     */
    private LatLng getLatLngInMapCenter() {
        mBaiduMap = mMapView.getMap();
        MapStatus mMapStatus = mBaiduMap.getMapStatus();
        LatLng center = mMapStatus.target;
        return center;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_back:                               //返回
                finish();
                break;
            case R.id.b_confirm:                            //确认
                String address = tv_address.getText().toString().trim();
                if (TextUtils.isEmpty(address) || address.equals("家地址") || address.equals("公司地址")) {
                    showCustomToast("请选择地址");
                    return;
                }
                Intent data = new Intent();
                data.putExtra("string", address);
                data.putExtra("lat", latLng.latitude);
                data.putExtra("lng", latLng.longitude);
                setResult(RESULT_OK, data);
                finish();
                break;
            case R.id.tv_address:                           //当前地址，调到输入页面
                InputAddressActivity.startInputAddressActivity(this,IntentFileds.INPUT_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        
        if(resultCode != RESULT_OK){
        	return;
        }
        
        //选择地址结果返回
        if (requestCode == IntentFileds.INPUT_ADDRESS) {
            String city = intent.getStringExtra("city");
            String district = intent.getStringExtra("district");
            String key = intent.getStringExtra("key");
            //定位
            geocode(city, key);
        }
    }


    //index:0,代表家;1，代表公司
    public static void startAddressChooseActivity(Activity activity, int requestCose) {
        Intent intent = new Intent(activity, AddressChooseActivity.class);
        if (requestCose == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCose);
        }
    }

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initSomething() {
		// TODO Auto-generated method stub
		
	}
}
