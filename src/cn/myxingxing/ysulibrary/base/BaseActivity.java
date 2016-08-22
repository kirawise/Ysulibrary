package cn.myxingxing.ysulibrary.base;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
	}
	
	//�����¼�
	public void back(View view){
		finish();
	}
	
	/**
	 * ���������Ҫ�ǳ�ʼ��view
	 * 
	 */
	public abstract void initView();

	/**
	 * ���������Ҫ�ǳ�ʼ����ʼ�����ݣ����дӷ�������ȥ����
	 */
	public abstract void initData();
	
	Toast mToast;

	public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {		
				@Override
				public void run() {
					if (mToast == null) {
						mToast = Toast.makeText(getApplicationContext(), text,Toast.LENGTH_LONG);
					} else {
						mToast.setText(text);
					}
					mToast.show();
				}
			});	
		}
	}

	public void ShowToast(final int resId) {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				if (mToast == null) {
					mToast = Toast.makeText(BaseActivity.this.getApplicationContext(), resId,Toast.LENGTH_LONG);
				} else {
					mToast.setText(resId);
				}
				mToast.show();
			}
		});
	}

	// ���������
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	// ��ʾ�����
	public void showSoftInputView(final EditText editText) {
		 Timer timer = new Timer();
		    timer.schedule(new TimerTask(){
		         public void run(){
		             InputMethodManager inputManager =(InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		             inputManager.showSoftInput(editText, 0);
		         }
		     },500);
	}
	
	public static int dip2px(Context context,float dipValue){
		float scale=context.getResources().getDisplayMetrics().density;		
		return (int) (scale*dipValue+0.5f);		
	}
}
