package cn.myxingxing.ysulibrary.adapter;

import java.util.List;

import cn.myxingxing.ysulibrary.R;
import cn.myxingxing.ysulibrary.bean.NowLend;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NowLendAdapter extends BaseAdapter {
	
	private Context context;
	private List<NowLend> lends;
	
	public NowLendAdapter(Context context, List<NowLend> lends) {
		this.context = context;
		this.lends = lends;
	}

	@Override
	public int getCount() {
		return lends.size();
	}

	@Override
	public Object getItem(int position) {
		return lends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_lend_now, null);
		TextView tv_book_title = (TextView)view.findViewById(R.id.tv_book_title);
		TextView tv_book_isbn = (TextView)view.findViewById(R.id.tv_book_isbn);
		TextView tv_book_lend_date = (TextView)view.findViewById(R.id.tv_book_lend_date);
		TextView tv_book_return_date = (TextView)view.findViewById(R.id.tv_book_return_date);
		TextView tv_book_relend_num = (TextView)view.findViewById(R.id.tv_book_relend_num);
		TextView tv_book_location = (TextView)view.findViewById(R.id.tv_book_location);
		TextView tv_book_extra = (TextView)view.findViewById(R.id.tv_book_extra);
		NowLend nowLend = lends.get(position);
		tv_book_title.setText("" + nowLend.getTit_aut());
		tv_book_isbn.setText("������: " + nowLend.getIsbn());
		tv_book_lend_date.setText("��������:" + nowLend.getLendday());
		tv_book_return_date.setText("Ӧ������:" + nowLend.getGiveday());
		tv_book_relend_num.setText("������: " + nowLend.getLennum());
		tv_book_location.setText("�ݲص�: " + nowLend.getLocation());
		tv_book_extra.setText("��  ��:" + nowLend.getOther());
		return view;
	}

}
