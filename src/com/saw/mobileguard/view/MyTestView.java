package com.saw.mobileguard.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-9-13 下午8:42:46
 * @描述 TODO
 */
public class MyTestView extends TextView {

	public MyTestView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public MyTestView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyTestView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

}
