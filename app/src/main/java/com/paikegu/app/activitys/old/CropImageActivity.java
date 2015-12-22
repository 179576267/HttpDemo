package com.xiankezu.sirceo.activitys.old;

import java.io.File;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.ab.util.AbFileUtil;
import com.ab.view.cropimage.CropImage;
import com.ab.view.cropimage.CropImageView;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.tools.ImageUtils;


/**
 * 裁剪界面
 */
public class CropImageActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "CropImageActivity";
    private CropImageView mImageView;
    private Bitmap mBitmap;

    private CropImage mCrop;

    private Button mSave;
    private Button mCancel, rotateLeft, rotateRight;
    private String mPath = "CropImageActivity";
    public int screenWidth = 0;
    public int screenHeight = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    break;

            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_crop_image);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBitmap != null) {
            mBitmap = null;
        }
    }

    private void init() {
        getWindowWH();
        mPath = getIntent().getStringExtra("PATH");
        mImageView = (CropImageView) findViewById(R.id.crop_image);
        mSave = (Button) this.findViewById(R.id.okBtn);
        mCancel = (Button) this.findViewById(R.id.cancelBtn);
        rotateLeft = (Button) this.findViewById(R.id.rotateLeft);
        rotateRight = (Button) this.findViewById(R.id.rotateRight);
        mSave.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        rotateLeft.setOnClickListener(this);
        rotateRight.setOnClickListener(this);
        //相册中原来的图片
        File mFile = new File(mPath);
        try {
            mBitmap = AbFileUtil.getBitmapFromSD(mFile, 1, 500, 500);
            if (mBitmap == null) {
                Toast.makeText(CropImageActivity.this, "没有找到图片", 0).show();
                finish();
            } else {
                resetImageView(mBitmap);
            }
        } catch (Exception e) {
            Toast.makeText(CropImageActivity.this, "没有找到图片", 0).show();
            finish();
        }
    }

    /**
     * 获取屏幕的高和宽
     */
    private void getWindowWH() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    private void resetImageView(Bitmap b) {
        mImageView.clear();
        mImageView.setImageBitmap(b);
        mImageView.setImageBitmapResetBase(b, true);
        mCrop = new CropImage(this, mImageView, mHandler);
        mCrop.crop(b);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBtn:
                finish();
                break;
            case R.id.okBtn:
                String path = ImageUtils.saveToLocal(CropImageActivity.this, mCrop.cropAndSave());
                Intent intent = new Intent();
                intent.putExtra("PATH", path);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.rotateLeft:
                mCrop.startRotate(270.f);
                break;
            case R.id.rotateRight:
                mCrop.startRotate(90.f);
                break;

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
