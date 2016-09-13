package com.saw.mobileguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-9-3 下午11:18:36
 * @描述 主界面
 */
public class HomeActivity extends Activity {
	private GridView gv_menus;//主界面的按钮
	private int icons[] = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app
			,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan
			,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
	private String names[] = {"手机防盗","通讯卫士","软件管家","进程管理","流量统计"
			,"病毒查杀","缓存清理","高级工具","设置中心"};
	private MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();//初始化界面
		initData();//初始化数据
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		adapter = new MyAdapter();
		gv_menus.setAdapter(adapter);//设置gridview数据
	}
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return icons.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_home_gridview, null);
			//获取组件
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_item_home_gv_icon);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_item_home_gv_name);
			//设置数据
			//设置图片
			iv_icon.setImageResource(icons[position]);
			//设置文字
			tv_name.setText(names[position]);
			return view;
		}
		
	}

	private void initView() {
		setContentView(R.layout.activity_home);
		gv_menus = (GridView) findViewById(R.id.gv_home_menus);
	}
}
