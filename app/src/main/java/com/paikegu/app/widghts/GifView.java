package com.xiankezu.sirceo.widghts;

import java.io.InputStream;

import com.xiankezu.sirceo.R;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

/**
 * gif播放View
 * @author zhengfei.wang
 */
public class GifView extends View {

	private Resources resources;
	private Movie mMovie;
	private long mMovieStart;

	public GifView(Context context) {
		this(context,null);
	}

	public GifView(Context context, AttributeSet attrs) {
		this(context, attrs,-1);
	}
	
	public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		 TypedArray a = context.obtainStyledAttributes(attrs,
	             R.styleable.GifView, defStyleAttr, -1);
		 resources = context.getResources();
	     setGifResource(a.getResourceId(R.styleable.GifView_src, -1));
	     a.recycle();
	}

	public void setGifResource(int resourceId) {
		if(resourceId==-1){
			return;
		}
		InputStream is = resources.openRawResource(resourceId);
		setGifStream(is);
	}
	
	public void setGifStream(InputStream is){
		mMovie = Movie.decodeStream(is);
		requestLayout();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long now = SystemClock.uptimeMillis();
		if(mMovieStart==0){
			mMovieStart = now;
		}
		
		if(mMovie!=null){
			int dur = mMovie.duration();
			if(dur==0){
				dur = 1000;
			}
			
//			int relTime = (int)((now-mMovieStart)%dur);
//			mMovie.setTime(relTime);
//			mMovie.draw(canvas, 0, 0);
//			invalidate();
		}
		
	}
}
