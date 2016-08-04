package com.example.ysulibrary.activities;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.os.Bundle;

import com.example.ysulibrary.R;
import com.example.ysulibrary.adapter.AsordAdapter;
import com.example.ysulibrary.base.BaseActivity;
import com.example.ysulibrary.bean.BookAsord;
import com.example.ysulibrary.config.Config;
import com.example.ysulibrary.event.AsordHistoryEvent;
import com.example.ysulibrary.net.IPUtil;
import com.example.ysulibrary.net.OkHttpUtil;
import com.example.ysulibrary.net.YsuCallback;
import com.example.ysulibrary.util.ParseLibrary;
import com.example.ysulibrary.view.xlist.XListView;
import com.example.ysulibrary.view.xlist.XListView.IXListViewListener;

public class AsordHistoryActivity extends BaseActivity {
	
	private int page = 1;
	private List<BookAsord> asords;
	private XListView lv_asord_history;
	private AsordAdapter asordAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.activity_asord_history);
		initData();
		initView();
	}
	
	@Subscribe(threadMode = ThreadMode.MAIN)
	//��ui�߳�ִ��
	public void onUserEvent(AsordHistoryEvent asordHistoryEvent){
		switch (asordHistoryEvent.getInfo()) {
		case Config.ASORD_HISTORY_SUCCESS:
			asordAdapter = new AsordAdapter(asords, mContext);
			lv_asord_history.setAdapter(asordAdapter);
			break;
		case Config.ASORD_HISTORY_FAILED:
			ShowToast("���ݽ���ʧ��");
			break;
		case Config.ASPRD_HISTORY_EMPTY:
			ShowToast("����δ�������κ��鼮");
			break;
		case Config.ASORD_HISTORY_NOMORE:
			ShowToast("���޸�������");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void initView() {
		lv_asord_history = (XListView)findViewById(R.id.lv_asord_history);
		lv_asord_history.setPullLoadEnable(true);
		lv_asord_history.setPullRefreshEnable(true);
		lv_asord_history.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				page = 1;
				initData();
				lv_asord_history.stopRefresh();
			}
			
			@Override
			public void onLoadMore() {
				page++;
				loadMore();
			}
		});
	}

	private void loadMore() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", page+"");
		OkHttpUtil.enqueue(IPUtil.asord_history, map, new YsuCallback(mContext){

			@Override
			public void onSuccess(String result) throws IOException {
				super.onSuccess(result);
				List<BookAsord> list = ParseLibrary.getAsordHist(result);
				if (list.size() == 0) {
					EventBus.getDefault().post(new AsordHistoryEvent(Config.ASORD_HISTORY_NOMORE));
				}else if (list.size() != 0) {
					asords.addAll(list);
					EventBus.getDefault().post(new AsordHistoryEvent(Config.ASORD_HISTORY_SUCCESS));
				}else {
					EventBus.getDefault().post(new AsordHistoryEvent(Config.ASORD_HISTORY_FAILED));
				}
			}

			@Override
			public void onFailure(String error) throws IOException {
				super.onFailure(error);
			}
		});
	}

	@Override
	public void initData() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", page+"");
		OkHttpUtil.enqueue(IPUtil.asord_history, map, new YsuCallback(mContext){

			@Override
			public void onSuccess(String result) throws IOException {
				super.onSuccess(result);
				asords = ParseLibrary.getAsordHist(result);
				if (asords.size() == 0) {
					EventBus.getDefault().post(new AsordHistoryEvent(Config.ASPRD_HISTORY_EMPTY));
				}else if (asords.size() != 0) {
					EventBus.getDefault().post(new AsordHistoryEvent(Config.ASORD_HISTORY_SUCCESS));
				}else {
					EventBus.getDefault().post(new AsordHistoryEvent(Config.ASORD_HISTORY_FAILED));
				}
			}

			@Override
			public void onFailure(String error) throws IOException {
				super.onFailure(error);
			}
		});
	}

}
