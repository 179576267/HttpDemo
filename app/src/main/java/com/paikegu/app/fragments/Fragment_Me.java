package com.xiankezu.sirceo.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.ab.util.AbStrUtil;
import com.loopj.android.http.RequestParams;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.old.CropImageActivity;
import com.xiankezu.sirceo.activitys.old.MyOrdersActivity;
import com.xiankezu.sirceo.activitys.old.PublishCourseActivity;
import com.xiankezu.sirceo.activitys.old.SettingActivity;
import com.xiankezu.sirceo.beans.Areas;
import com.xiankezu.sirceo.globals.URL;
import com.xiankezu.sirceo.https.SIRCEOResponseHandler;
import com.xiankezu.sirceo.tools.ContentUtils;
import com.xiankezu.sirceo.tools.JsonUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
/**
 * 我的
 */
public class Fragment_Me extends BaseFragment implements OnClickListener{
	//切换身份
	private ImageView im_change_role;
	private TextView tv_change_role;
	private boolean sirceo = false;
	
	private ImageView im_me;
	// 拍照照片的存储位置
	private File PHOTO_DIR;
	// 照相机拍照获取的图片
	private String mFileName;
	// 裁剪获取的图片
	private String compressImStr;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	/**
	 * 弹出菜单
	 */
	private PopupWindow popupWindow;

	public Fragment_Me(int resource) {
		super(resource);
	}

	@Override
	protected void initView() {
		im_change_role = (ImageView) layout.findViewById(R.id.im_change_role);
		im_me = (ImageView) layout.findViewById(R.id.im_me);
		im_me.setOnClickListener(this);
		tv_change_role = (TextView) layout.findViewById(R.id.tv_change_role);
		layout.findViewById(R.id.rl_setting).setOnClickListener(this);
		layout.findViewById(R.id.rl_publish_course).setOnClickListener(this);
		layout.findViewById(R.id.tv_orders).setOnClickListener(this);
		layout.findViewById(R.id.rl_change_role).setOnClickListener(this);
	}

	@Override
	protected void initSomething() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_change_role:
			if(sirceo){
				im_change_role.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_change_role));
				tv_change_role.setText("切换至学客身份");
			}else{
				im_change_role.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_sirceo_in));
				tv_change_role.setText("闲客入住");
			}
			sirceo = !sirceo;
			break;
		case R.id.rl_setting:
			startActivity(new Intent(mApplication, SettingActivity.class));
			break;
		case R.id.rl_publish_course:
			startActivity(new Intent(mApplication, PublishCourseActivity.class));
			break;
		case R.id.tv_orders:
			startActivity(new Intent(mApplication, MyOrdersActivity.class));
			break;
		case R.id.im_me:
			showPopup();
			break;
		case R.id.btn_camera:
			selectPhotoWithCamera();
			popupWindow.dismiss();
			break;
		case R.id.btn_gallery:
			selectPhoto();
			popupWindow.dismiss();
			break;
		case R.id.btn_cancel:
			popupWindow.dismiss();
			break;
		}
	}
	
	/**
	 * 选择相册图片
	 */
	private void selectPhoto() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
	}
	
	/**
	 * 从相机获取图片
	 */
	private void selectPhotoWithCamera() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			mActivity.showCustomToast("没有可用的存储卡");
		}
	}
	String mCurrentFilePath = null;
	private void doTakePhoto() {
		mFileName = System.currentTimeMillis() + ".jpg";

		File mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
		if (!mCurrentPhotoFile.exists()) {
			try {
				mCurrentPhotoFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		if (mCurrentPhotoFile != null) {
			mCurrentFilePath = mCurrentPhotoFile.getAbsolutePath();
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(mCurrentPhotoFile));
		}
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}
	
	/**
	 * 显示选择图片的popupwin
	 */
	private void showPopup() {
		View view = mActivity.getLayoutInflater()
				.inflate(R.layout.layout_popup_win, null);
		Button btn_camera = (Button) view.findViewById(R.id.btn_camera);
		Button btn_gallery = (Button) view.findViewById(R.id.btn_gallery);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		btn_camera.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_gallery.setOnClickListener(this);

		LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		ll_popup.startAnimation(AnimationUtils.loadAnimation(mActivity,R.anim.popup_anim));

		if (popupWindow == null) {
			popupWindow = new PopupWindow(mActivity);
			popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
			popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
			popupWindow.setBackgroundDrawable(new BitmapDrawable());
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
		}

		popupWindow.setContentView(view);
		popupWindow.showAtLocation(im_me, Gravity.BOTTOM, 0, 0);
		popupWindow.update();
	}
	
	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if (AbStrUtil.isEmpty(uri.getAuthority())) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = mActivity.managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,Intent mIntent) {
		if (Activity.RESULT_OK != resultCode) {
			return;
		}

		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA:// 相册选择
			Uri uri = mIntent.getData();
			String currentFilePath = getPath(uri);
			if (!AbStrUtil.isEmpty(currentFilePath)) {
				Intent intent1 = new Intent(mActivity, CropImageActivity.class);
				intent1.putExtra("PATH", currentFilePath);
				startActivityForResult(intent1, CAMERA_CROP_DATA);
			} else {
				mActivity.showCustomToast("未在存储卡中找到这个文件");
			}
			break;
		case CAMERA_WITH_DATA:// 相机选择
			Intent intent2 = new Intent(mActivity, CropImageActivity.class);
			if (!TextUtils.isEmpty(mCurrentFilePath)) {
				intent2.putExtra("PATH", mCurrentFilePath);
				startActivityForResult(intent2, CAMERA_CROP_DATA);
			}
			break;
		case CAMERA_CROP_DATA:// 切图选择
			compressImStr = mIntent.getStringExtra("PATH");
			im_me.setImageURI(Uri.parse(compressImStr));
			changeAvatar();
			break;
		}
		super.onActivityResult(requestCode, resultCode, mIntent);
	}

	private void changeAvatar() {
		RequestParams params = new RequestParams();
		params.put("phoneNum", mApplication.loginPhoneNum);
		try {
			params.put("avatar", new File(compressImStr));
		} catch (FileNotFoundException e1) {
			mActivity.showCustomToast("头像文件未找到");
			return;
		}
		api.post(URL.PUSH_MY_AVATAR, params, new SIRCEOResponseHandler(mActivity,true) {
			
			@Override
			public void onSuccess(JSONObject result) {
				mActivity.showCustomToast("头像修改成功");
			}
			
			@Override
			public void onFailure(String result,int error) {
				im_me.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_camear));
				switch (error) {
				case 1014:
					mActivity.showCustomToast("图片格式不正确");
					break;

				default:
					mActivity.showCustomToast("上传头像失败");
					break;
				}
			}
		});
	}

}
