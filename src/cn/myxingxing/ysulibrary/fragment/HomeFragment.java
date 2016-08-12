package cn.myxingxing.ysulibrary.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.myxingxing.ysulibrary.activities.BookDetailActivity;
import cn.myxingxing.ysulibrary.adapter.SearchBookAdapter;
import cn.myxingxing.ysulibrary.base.BaseFragment;
import cn.myxingxing.ysulibrary.bean.SearchBook;
import cn.myxingxing.ysulibrary.config.Config;
import cn.myxingxing.ysulibrary.event.SearchBookEvent;
import cn.myxingxing.ysulibrary.net.IPUtil;
import cn.myxingxing.ysulibrary.net.OkHttpUtil;
import cn.myxingxing.ysulibrary.net.YsuCallback;
import cn.myxingxing.ysulibrary.util.ParseLibrary;
import cn.myxingxing.ysulibrary.view.xlist.XListView;

import com.example.ysulibrary.R;

public class HomeFragment extends BaseFragment implements OnItemClickListener,OnClickListener,OnItemSelectedListener{
	
	private int mScreenWidth;
	private int page = 1;
	private LinearLayout ly_no_data;
	private TextView tv_title;
	private Spinner first_spinner;
	private EditText et_book_name;
	private XListView lv_book;
	private TextView tv_no;
	private RelativeLayout ly_progress,layout_action;
	private PopupWindow morePop;
	private String strSearchType;
	private List<SearchBook> searchBookList;
	private SearchBookAdapter searchBookAdapter;
	private int LISTVIEW_TAG =1;
	private static final int LV_SEARCH = 1;
	private static final int LV_HOT = 2;
	private static final int LV_NEW = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frag_home, container, false);
		initData();
		initView();
		return view;
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initView() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		
		view.findViewById(R.id.ly_search_top).setOnClickListener(this);
		view.findViewById(R.id.btn_search_ok).setOnClickListener(this);;
		ly_no_data = (LinearLayout)view.findViewById(R.id.ly_no_data);
		tv_title = (TextView)view.findViewById(R.id.tv_title);
		first_spinner = (Spinner)view.findViewById(R.id.first_spinner);
		et_book_name = (EditText)view.findViewById(R.id.et_book_name);
		lv_book = (XListView)view.findViewById(R.id.lv_book);
		tv_no = (TextView)view.findViewById(R.id.tv_no);
		ly_progress = (RelativeLayout)view.findViewById(R.id.ly_progress);
		layout_action = (RelativeLayout)view.findViewById(R.id.layout_action);
		
		String[] firstsp = getResources().getStringArray(R.array.titlespinner);
		ArrayAdapter<String> firstAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,firstsp);
		first_spinner.setAdapter(firstAdapter);	
		first_spinner.setOnItemSelectedListener(this);	
	}
 
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onUserEvent(SearchBookEvent event) {
		switch (event.getInfo()) {
		case Config.SEARCH_BOOK_FAILED:
			showErrorView();
			ShowToast("���ݽ���ʧ��,������");
			break;
		case Config.SEARCH_BOOK_EMPTY:
			showErrorView();
			break;
		case Config.SEARCH_BOOK_SUCCESS:
			searchBookAdapter = new SearchBookAdapter(searchBookList, ct);
			showSuccessView();
			lv_book.setAdapter(searchBookAdapter);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_search_top:
			showPop();
			break;
		case R.id.btn_search_ok:
			searchBook();
			break;
		case R.id.btn_layout_book_search:
			changeTextView(v);
			break;
		case R.id.btn_hot_book:
			changeTextView(v);
			break;
		case R.id.btn_layout_new_book:
			changeTextView(v);
			break;
		default:
			break;
		}
	}
	
	private void searchBook() {
		if (TextUtils.isEmpty(et_book_name.getText().toString())) {
			Toast.makeText(ct, "������Ҫ�����Ĺؼ���", Toast.LENGTH_SHORT).show();
			return;
		}else {
			ly_progress.setVisibility(View.VISIBLE);
			ly_no_data.setVisibility(View.GONE);
			Map<String, String> map = new HashMap<String, String>();
			map.put("strSearchType", strSearchType);
			map.put("match_flag", "forward");
			map.put("historyCount", "1");
			map.put("strText", et_book_name.getText().toString());
			map.put("doctype", "ALL");//ͼ������
			map.put("displaypg", "20");//Ĭ��20��
			map.put("showmode", "list");
			map.put("sort", "CATA_DATE");
			map.put("orderby", "desc");
			map.put("location", "ALL");
			map.put("page", page+"");
			OkHttpUtil.enqueue(IPUtil.search_book, map, new YsuCallback(ct){
				@Override
				public void onSuccess(String result) throws IOException {
					super.onSuccess(result);
					searchBookList = ParseLibrary.getSearchBooks(result);
					if (searchBookList == null) {
						EventBus.getDefault().post(new SearchBookEvent(Config.SEARCH_BOOK_FAILED));
					}else if (searchBookList.size() != 0) {
						EventBus.getDefault().post(new SearchBookEvent(Config.SEARCH_BOOK_SUCCESS));
					}else {
						EventBus.getDefault().post(new SearchBookEvent(Config.SEARCH_BOOK_EMPTY));
					}
				}

				@Override
				public void onFailure(String error) throws IOException {
					super.onFailure(error);
				}
			});
		}
	}
	
	private void changeTextView(View view){
		switch (view.getId()) {
		case R.id.btn_hot_book:
			tv_title.setText("���Ž���");
			tv_title.setTag("���Ž���");
			LISTVIEW_TAG = LV_HOT;
			lv_book.setOnItemClickListener(this);
			break;
		case R.id.btn_layout_new_book:
			tv_title.setText("����ͨ��");
			tv_title.setTag("����ͨ��");
			LISTVIEW_TAG = LV_NEW;
			lv_book.setOnClickListener(this);
			break;
		case R.id.btn_layout_book_search:
			tv_title.setText("����");
			tv_title.setText("����");
			LISTVIEW_TAG = LV_SEARCH;
			lv_book.setOnClickListener(this);
			break;
		default:
			break;
		}
		morePop.dismiss();
	}
	
	private void showErrorView(){
		ly_no_data.setVisibility(View.VISIBLE);
		ly_progress.setVisibility(View.GONE);
		lv_book.setVisibility(View.GONE);
	}
	
	private void showSuccessView(){
		ly_no_data.setVisibility(View.GONE);
		ly_progress.setVisibility(View.GONE);
		lv_book.setVisibility(View.VISIBLE);
	}
	
	private void showPop() {
		View view = LayoutInflater.from(ct).inflate(R.layout.pop_book, null);
		view.findViewById(R.id.btn_layout_book_search).setOnClickListener(this);
		view.findViewById(R.id.btn_hot_book).setOnClickListener(this);
		view.findViewById(R.id.btn_layout_new_book).setOnClickListener(this);
		morePop = new PopupWindow(view, mScreenWidth, 600);
		morePop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					morePop.dismiss();
					return true;
				}
				return false;
			}
		});
		morePop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		morePop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		morePop.setTouchable(true);
		morePop.setFocusable(true);
		morePop.setOutsideTouchable(true);
		morePop.setBackgroundDrawable(new BitmapDrawable());
		morePop.setAnimationStyle(R.style.MenuPop);
		morePop.showAsDropDown(layout_action, 0, -dip2px(ct, 2.0F));
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(ct, BookDetailActivity.class);
		switch (LISTVIEW_TAG) {
		case LV_SEARCH:
			intent.putExtra("detailUrl", searchBookList.get(position-1).getBook_url().toString());
			break;
		case LV_NEW:
			
			break;
		case LV_HOT:
			
			break;
		default:
			break;
		}
		startActivity(intent);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String temp = parent.getItemAtPosition(position).toString();
		if("����".equals(temp)){
			strSearchType = "title";
		}else if("������".equals(temp)){
			strSearchType = "author";
		}else if("�����".equals(temp)){
			strSearchType = "keyword";
		}else if("ISBN/ISSN".equals(temp)){
			strSearchType = "isbn";
		}else if("������".equals(temp)){
			strSearchType = "asordno";
		}else if("�����".equals(temp)){
			strSearchType = "coden";
		}else if("�����".equals(temp)){
			strSearchType = "callno";
		}else if("������".equals(temp)){
			strSearchType = "publisher";
		}else if("������".equals(temp)){
			strSearchType = "series";
		}else if("����ƴ��".equals(temp)){
			strSearchType = "tpinyin";
		}else if("������ƴ��".equals(temp)){
			strSearchType = "apinyin";
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		strSearchType = "title";
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
