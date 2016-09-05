package com.saw.mobileguard;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.saw.mobileguard.domain.UrlBean;
/**
 * 
 * @author Administrator
 * @创建时间 2016-9-3 下午8:53:13
 * @描述 TODO
 */
public class SplashActivity extends Activity {

	private static final int LOADMAIN = 1;//加载主界面
	private static final int SHOWUPDATEDIALOG = 2;//显示是否更新对话框
	protected static final int ERROR = 3;//错误统一代码
	private RelativeLayout rl_root;//界面的根布局组件
	private int versionCode;//版本号
	private String versionName;//版本名
	private TextView tv_versionName;//显示版本名的组件
	private UrlBean parseJson;//url信息封装bean
	private long startTimeMillis;//毫秒显示当前时间

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();//初始化界面
		initData();//初始化数据
		initAnimation();//初始化动画
		checkVersion();//检查服务器的版本
	}
	private void initData() {
		//获取自己的版本信息
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			//版本号
			versionCode = packageInfo.versionCode;
			//版本名
			versionName = packageInfo.versionName;
			//设置TextView
			tv_versionName.setText(versionName);
			
		} catch (NameNotFoundException e) {
			//can not reach 异常不会发送
		}
	}
	/**
	 * 访问服务器，获取最新的版本信息
	 */
	private void checkVersion() {
		//访问服务器，获取数据url
		//子线程中执行，访问服务器
		new Thread() {

			

			public void run() {
				BufferedReader reader = null;
				HttpURLConnection conn = null;
				int errorCode = -1;//正常，没有错误
				try {
					//毫秒显示当前时间
					startTimeMillis = System.currentTimeMillis();
					URL url = new URL("http://192.168.1.101/zhbj/guardversion.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(5000);//读取超时时间
					conn.setConnectTimeout(5000);//网络连接的超时设置
					conn.setRequestMethod("GET");//设置请求方式
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {//请求成功
						InputStream is = conn.getInputStream();
						//把字节流转化成字符流
						reader = new BufferedReader(new InputStreamReader(is));
						String line = reader.readLine();//读取一行信息
						StringBuffer jsonString  = new StringBuffer(); 
						while (line != null) {//有数据
							jsonString.append(line);
							//继续读取
							line = reader.readLine();
						}
						//解析json数据
						parseJson = parseJson(jsonString);
					} else {
						//404
						errorCode = 404;
					}
					
				} catch (MalformedURLException e) {//4002
					errorCode = 4002;
					e.printStackTrace();
				} catch (IOException e) {//4001 网络连接失败
					errorCode = 4002;
					e.printStackTrace();
				} catch (JSONException e) {//4003
					errorCode = 4003;
					e.printStackTrace();
				} finally {
//					if (errorCode == -1) {
//						isNewVersion(parseJson);//是否有新版本
//					} else {
//						Message msg = Message.obtain();
//						msg.what = ERROR;
//						msg.arg1 = errorCode;
//						handler.sendMessage(msg);//发送错误信息
//					}
					Message msg = Message.obtain();
					if (errorCode == -1) {
						msg.what = isNewVersion(parseJson);//检测是否有版本
					} else {
						msg.what = ERROR;
						msg.arg1 = errorCode;
					}
					long endTimeMillis = System.currentTimeMillis();//执行结束时间
					if (endTimeMillis - startTimeMillis < 3000) {
						//设置休眠时间，保证至少要休眠3秒
						SystemClock.sleep(3000-(endTimeMillis - startTimeMillis));
					}
					handler.sendMessage(msg);//发送消息
					try {
						//关闭资源
						if (reader == null || conn == null) {
							return;
						}
						reader.close();//关闭输入流
						conn.disconnect();//断开连接
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			

			
		}.start();
		
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			//处理消息
			switch (msg.what) {
			case LOADMAIN://加载主界面
				loadMain();
				break;
			case ERROR://有异常
				switch (msg.arg1) {
				case 404://资源找不到
					Toast.makeText(getApplicationContext(), "404资源找不到", Toast.LENGTH_SHORT).show();
					break;
				case 4001://网络连接失败
					Toast.makeText(getApplicationContext(), "4001没有网络", Toast.LENGTH_SHORT).show();
					break;
				case 4002://url地址错误
					Toast.makeText(getApplicationContext(), "4002url地址错误", Toast.LENGTH_SHORT).show();
					break;
				case 4003://json格式错误
					Toast.makeText(getApplicationContext(), "4003json格式错误", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				loadMain();//进入主界面
				break;
			case SHOWUPDATEDIALOG://显示更新版本的对话框
				showupdatedialog();
				break;
			default:
				break;
			}
		}

		

		
	};
	/**
	 * 进入主界面
	 */
	private void loadMain() {
		Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
		startActivity(intent);//进入主界面
		finish();//关闭自己
	}
	/**
	 * 显示是否更新版本的对话框
	 */
	protected void showupdatedialog() {
		//对话框的上下文是Activity的class,AlertDialog是Activity的一部分
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//让用户禁用取消操作
		//builder.setCancelable(false);
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				//用户点击取消事件处理
				//进入主界面
				loadMain();
			}
		})
		.setTitle("提醒")
		.setMessage("是否更新新版本？新版本具有如下特性:"+parseJson.getDesc())
		.setPositiveButton("更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//更新apk
				System.out.println("更新apk");
				//访问网络，下载新的APK
				downLoadNewApk();//下载新版本
			}

			
		}).setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//进入主界面
				loadMain();
			}
		});
		builder.show();//显示对话框
	};
	/**
	 * 新版本的下载安装
	 */
	protected void downLoadNewApk() {
		HttpUtils utils = new HttpUtils();
		//parseJson.getUrl()下载地址
		//target 本地路径
		//回调地址 callback
		utils.download(parseJson.getUrl(), "/mnt/sdcard/xx.apk", new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				//下载成功
				//在主线程中执行
				Toast.makeText(getApplicationContext(), "下载新版本成功", Toast.LENGTH_LONG).show();
				//安装apk
				installApk();//安装apk
			}
			
			

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				//下载失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
			}
		});
	}
	/**
	 * 安装下载的新版本apk
	 */
	protected void installApk() {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		Uri data = Uri.fromFile(new File("/mnt/sdcard/xx.apk"));
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		startActivity(intent);
		startActivityForResult(intent, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//如果用户取消更新apk,那么久直接进入主界面
		loadMain();
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 判断是否有新版本 在子线程中执行
	 * @param bean 
	 */
	protected int isNewVersion(UrlBean bean) {
		int version = bean.getVersionCode();//获取服务器版本
		if (version == versionCode) {
			//版本一致 进入主界面
			return LOADMAIN;
//			Message msg = Message.obtain();
//			msg.what = LOADMAIN;
//			handler.sendMessage(msg);
//			System.out.println("版本一致！");
			
		} else {
			//弹出对话框，显示新版本的描述信息，让用户点击是否更新
			return SHOWUPDATEDIALOG;
		}
		
	}
	/**
	 * 解析json数据
	 * @param jsonString
	 * 从服务器获取的json数据
	 * @return url信息的封装对象
	 * @throws JSONException 
	 */
	protected UrlBean parseJson(StringBuffer jsonString) throws JSONException {
		UrlBean bean = new UrlBean();
		//把json字符串数据封装成json对象
		JSONObject jobject = new JSONObject(jsonString + "");
		int version = jobject.getInt("version");
		String apkPath = jobject.getString("url");
		String desc = jobject.getString("desc");
		bean.setDesc(desc);//描述信息
		bean.setUrl(apkPath);//新apk下载路径
		bean.setVersionCode(version);//新版本号
		return bean;
		
	};
	/**
	 * 界面组件的初始化
	 */
	private void initView() {
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		tv_versionName = (TextView) findViewById(R.id.tv_splash_version_name);
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
