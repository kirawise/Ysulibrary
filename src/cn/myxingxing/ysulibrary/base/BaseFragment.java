package cn.myxingxing.ysulibrary.base;

import cn.myxingxing.ysulibrary.util.SingleManager;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {
	
	public Context ct;
	public View view;
	Toast mToast;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ct = getActivity();
	}
	
	/**
	 * �û��Ƿ��¼
	 */
	protected boolean isLogined(){
		return SingleManager.getInstance().getCurrentUser().isLogined();
	}
	
	/**
	 * ���������Ҫ�ǳ�ʼ��view
	 * 
	 * @param inflater
	 */
	public abstract void initView();

	/**
	 * ���������Ҫ�ǳ�ʼ����ʼ�����ݣ����дӷ�������ȥ����
	 */
	public abstract void initData();
	
	public void ShowToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(ct, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public void ShowToast(int text) {
		if (mToast == null) {
			mToast = Toast.makeText(ct, text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		mToast.show();
	}

	public static int dip2px(Context context,float dipValue){
		float scale=context.getResources().getDisplayMetrics().density;		
		return (int) (scale*dipValue+0.5f);		
	}
}
