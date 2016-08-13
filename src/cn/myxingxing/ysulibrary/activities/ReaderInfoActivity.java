package cn.myxingxing.ysulibrary.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.os.Bundle;
import android.widget.ListView;
import cn.myxingxing.ysulibrary.R;
import cn.myxingxing.ysulibrary.adapter.ReaderInfoAdapter;
import cn.myxingxing.ysulibrary.base.BaseActivity;
import cn.myxingxing.ysulibrary.config.Config;
import cn.myxingxing.ysulibrary.event.YsuEvent;
import cn.myxingxing.ysulibrary.net.IPUtil;
import cn.myxingxing.ysulibrary.net.OkHttpUtil;
import cn.myxingxing.ysulibrary.net.YsuCallback;
import cn.myxingxing.ysulibrary.util.ParseLibrary;

public class ReaderInfoActivity extends BaseActivity {
	
	private List<String> info;
	private ListView lv_info;
	private ReaderInfoAdapter readerInfoAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reder_info);
		EventBus.getDefault().register(this);
		initData();
		initView();
	}

	@Override
	public void initView() {
		lv_info = (ListView)findViewById(R.id.lv_info);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	// ��ui�߳�ִ��
	public void onUserEvent(YsuEvent event) {
		switch (event.getInfo()) {
		case Config.READERINFO_PARSE_SUCCESS:
			readerInfoAdapter = new ReaderInfoAdapter(mContext, info);
			lv_info.setAdapter(readerInfoAdapter);
			break;
		case Config.READERINFO_PARSE_ERROR:
			ShowToast("�����쳣,������...");
			break;
		default:
			break;
		}
	}
	
	@Override
	public void initData() {
		OkHttpUtil.enqueue(IPUtil.redr_info_rule, null, new YsuCallback(mContext){
			@Override
			public void onSuccess(String result) throws IOException {
				super.onSuccess(result);
				info = new ArrayList<String>();
				info = ParseLibrary.getReaderInfo(result);
				if (info.size() != 0) {
					EventBus.getDefault().post(new YsuEvent(Config.READERINFO_PARSE_SUCCESS));
				}else {
					EventBus.getDefault().post(new YsuEvent(Config.READERINFO_PARSE_ERROR));
				}
			}

			@Override
			public void onFailure(String error) throws IOException {
				super.onFailure(error);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
