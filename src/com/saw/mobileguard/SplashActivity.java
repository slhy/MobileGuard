package com.saw.mobileguard;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
/**
 * 
 * @author Administrator
 * @创建时间 2016-9-3 下午8:53:13
 * @描述 TODO
 */
public class SplashActivity extends Activity {

	private RelativeLayout rl_root;//界面的根布局组件

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();//初始化界面
		initAnimation();//初始化动画
	}
	/**
	 * 界面组件的初始化
	 */
	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
	}

	private void initAnimation() {
		//定义Alpha动画
		//创建动画，Alpha动画 0.0完全透明，1.0完全显示
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		//设置播放的时间
		aa.setDuration(3000);
		//界面停留在动画结束的状态
		aa.setFillAfter(true);
		//定义旋转动画
		RotateAnimation ra = new RotateAnimation(0, 360,
				//设置锚点
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		//设置播放的时间
		ra.setDuration(3000);
		//界面停留在动画结束的状态
		ra.setFillAfter(true);
		//定义比例动画
		
		//显示动画,从小到大
		ScaleAnimation sa = new ScaleAnimation(
				0.0f, 1.0f, 
				0.0f, 1.0f, 
				//设置锚点(中间)
				Animation.RELATIVE_TO_SELF,0.5f,
				Animation.RELATIVE_TO_SELF,0.5f);
		//设置播放的时间
		sa.setDuration(3000);
		//界面停留在动画结束的状态
		sa.setFillAfter(true);
		//创建动画集合
		AnimationSet as = new AnimationSet(true);
		as.addAnimation(sa);//加载比例动画
		as.addAnimation(ra);//加载旋转动画
		as.addAnimation(aa);//加载渐变动画
		//显示动画，根布局设置动画
		rl_root.startAnimation(as);//同时播放三个动画
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
