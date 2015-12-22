package com.xiankezu.sirceo.activitys.old;

import com.xiankezu.sirceo.R;
import com.xiankezu.sirceo.activitys.base.BaseActivity;
import com.xiankezu.sirceo.fragments.Fragment_Course;
import com.xiankezu.sirceo.fragments.Fragment_Main;
import com.xiankezu.sirceo.fragments.Fragment_Me;
import com.xiankezu.sirceo.fragments.Fragment_Room;
import com.xiankezu.sirceo.widghts.NestRadioGroup;
import com.xiankezu.sirceo.widghts.NestRadioGroup.OnCheckedChangeListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * @author zhenfei.wang
 */
public class MenuActivity extends BaseActivity {
	private NestRadioGroup rg;
	
	private Fragment[] fragments;
	public Fragment_Main mainFragment;
	private Fragment_Course courseFragment;
	private Fragment_Room roomFragment;
	private Fragment_Me meFragment;
	private int index;
	private int currentTabIndex;// 当前fragment的index

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void findViews() {
		rg = (NestRadioGroup) findViewById(R.id.main_radio);
	}

	@Override
	protected void initSomething() {
		mainFragment = new Fragment_Main(R.layout.fragment_main);
		courseFragment = new Fragment_Course(R.layout.fragment_course);
		roomFragment = new Fragment_Room(R.layout.fragment_room);
		meFragment = new Fragment_Me(R.layout.fragment_me);
		fragments = new Fragment[] { mainFragment, courseFragment,
				roomFragment, meFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction()
				.add(R.id.fragment_container, mainFragment)
				.add(R.id.fragment_container, courseFragment)
				.add(R.id.fragment_container, roomFragment)
				.add(R.id.fragment_container, meFragment)
				.hide(courseFragment).hide(roomFragment)
				.hide(meFragment).show(mainFragment).commit();	
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(NestRadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button0:
					index = 0;
					break;
				case R.id.radio_button1:
					index = 1;
					break;
				case R.id.radio_button2:
					index = 2;
					break;
				case R.id.radio_button3:
					index = 3;
					break;
				}
				
				if (currentTabIndex != index) {
					FragmentTransaction trx = getSupportFragmentManager()
							.beginTransaction();
					trx.hide(fragments[currentTabIndex]);
					if (!fragments[index].isAdded()) {
						trx.add(R.id.fragment_container, fragments[index]);
					}
					trx.show(fragments[index]).commit();
				}
				currentTabIndex = index;
			}
			
			
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fragments[currentTabIndex].onActivityResult(requestCode, resultCode, data);
	}

}
