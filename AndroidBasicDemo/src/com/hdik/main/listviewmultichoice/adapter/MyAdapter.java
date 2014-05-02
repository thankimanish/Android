package com.hdik.main.listviewmultichoice.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.hdik.main.R;
import com.hdik.main.listviewmultichoice.bean.DummyData;

public class MyAdapter extends ArrayAdapter<DummyData> implements
		OnCheckedChangeListener {

	private Context context;
	private ArrayList<DummyData> listItems;
	private ViewHolder holder;
	private LayoutInflater inflater;

	public MyAdapter(Context context, ArrayList<DummyData> listItems) {
		super(context, R.layout.row_layout, listItems);
		this.listItems = listItems;
		this.context = context;
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listmulit_row_layout, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.textView1);
			holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
			holder.cb.setOnCheckedChangeListener(this);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cb.setTag(position);
		if (listItems.get(position).isChecked())
			holder.cb.setChecked(true);
		else
			holder.cb.setChecked(false);
		holder.tv.setText(listItems.get(position).getTitle());
		return convertView;
	}

	class ViewHolder {
		TextView tv;
		CheckBox cb;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int pos = (Integer) buttonView.getTag();
		listItems.get(pos).setChecked(isChecked);

	}

}
