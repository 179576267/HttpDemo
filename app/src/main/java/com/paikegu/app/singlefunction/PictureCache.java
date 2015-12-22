package com.xiankezu.sirceo.singlefunction;

import java.io.File;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.globals.MyApplication;
import com.xiankezu.sirceo.tools.ImageUtils;
import com.xiankezu.sirceo.tools.MD5Utils;

/**
 * 一级缓存：本地保存
 * 二级缓存：内存保存，OOM/3
 * @author zhenfei.wang
 */
public class PictureCache {
	private MyApplication mApplication;
	private LruCache<String, Bitmap> cache;// 缓存图片用
	private String mPicturePath;
	
	
	
	private LruCache<String, Bitmap> getCache() {
		if(cache == null){
			cache = initImageCache();
		}
		return cache;
	}

	public PictureCache(MyApplication application) {
		super();
		if(mApplication==null){
			this.mApplication = application;
		}
//		mPicturePath = mApplication.getFilesDir() + File.separator+"images"+File.separator;
		mPicturePath = Environment.getExternalStorageDirectory() + File.separator+"SIRCEO"+File.separator+"images"+File.separator;
		File file = new File(mPicturePath);
		if(!file.exists()){
			file.mkdirs();
		}
		cache = initImageCache();
	}
	
	@SuppressLint("NewApi")
	public  LruCache<String, Bitmap> initImageCache() {
		return new LruCache<String, Bitmap>(ImageUtils.getOOMSize(mApplication.getApplicationContext()) / 3) {// 和版本号有关系
			@Override
			protected int sizeOf(String key, Bitmap value) {
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
					value.getAllocationByteCount();
				} else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
					value.getByteCount();
				}
				return value.getRowBytes() * value.getHeight();// 通用
			}
		};
	}
	
	/**
	 * 获取网络图片， 并是配到ImageView
	 */
	public void loadNetPicture(final ImageView iv,String path){
		if(TextUtils.isEmpty(path)){
			return;
		}
		final String md5Path = MD5Utils.GetMD5Code(path);//MD5转换的路径
		Bitmap bitmap = getCache().get(md5Path);
		if(bitmap!=null){//内存缓存有
			iv.setImageBitmap(bitmap);
			return;
		}
		final String cacheString = mPicturePath+md5Path+".jpg";//真实的文件路径
		File file = new File(cacheString);
		if(file.exists()){//本地缓存有
			bitmap = BitmapFactory.decodeFile(cacheString);
			iv.setImageBitmap(bitmap);
			if(bitmap!=null){//图片资源可能被删除
				getCache().put(md5Path, bitmap);				
			}
			return;
		}
		//需要请求网络去加载图片
		RequestCreator creator = Picasso.with(mApplication).load(path).error(R.drawable.icon_default_boy);
		creator.into(iv,null);
		creator.into(new Target() {
			
			@Override
			public void onPrepareLoad(Drawable drawable) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
				if(bitmap!=null){
					//保存到本地
					ImageUtils.saveBitmap(cacheString, bitmap);
					getCache().put(md5Path, bitmap);
					iv.setImageBitmap(bitmap);
				}
			}
			
			@Override
			public void onBitmapFailed(Drawable drawable) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 获取网络图片，取缩略图， 并是配到ImageView
	 */
	public void loadNetPictureForThumbnails(final ImageView iv,String path,final int w,final int h){
		if(TextUtils.isEmpty(path)){
			return;
		}
		final String md5Path = MD5Utils.GetMD5Code(path)+"SMALL";//MD5转换的路径
		Bitmap bitmap = getCache().get(md5Path);
		if(bitmap!=null){//内存缓存有
			iv.setImageBitmap(bitmap);
			return;
		}
		final String cacheString = mPicturePath+md5Path+".jpg";//真实的文件路径
		File file = new File(cacheString);
		if(file.exists()){//本地缓存有
			bitmap =ImageUtils.getBitmapByBytes(ImageUtils.BitmapToBytes(BitmapFactory.decodeFile(cacheString)),w,h);
			iv.setImageBitmap(bitmap);
			if(bitmap!=null){//图片资源可能被删除
				getCache().put(md5Path, bitmap);				
				return;
			}
		}
		//需要请求网络去加载图片
		RequestCreator creator = Picasso.with(mApplication).load(path).error(R.drawable.ic_launcher);
		creator.into(iv,null);
		creator.into(new Target() {
			
			@Override
			public void onPrepareLoad(Drawable drawable) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
				if(bitmap!=null){
					//保存到本地
					bitmap =ImageUtils.getBitmapByBytes(ImageUtils.BitmapToBytes(bitmap),w,h);
					ImageUtils.saveBitmap(cacheString, bitmap);
					getCache().put(md5Path, bitmap);
					iv.setImageBitmap(bitmap);
				}
			}
			@Override
			public void onBitmapFailed(Drawable drawable) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
