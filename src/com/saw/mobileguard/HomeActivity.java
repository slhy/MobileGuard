package com.saw.mobileguard;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Administrator
 * @创建时间 2016-9-3 下午11:18:36
 * @描述 主界面
 */
public class HomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();//初始化界面
	}

	private void initView() {
		setContentView(R.layout.activity_home);
	}
}
