package com.hdik.main.simpleparsing.adapter;

import java.util.ArrayList;

import com.hdik.main.simpleparsing.adapter.ContactAdapter.ViewHolder;
import com.hdik.main.simpleparsing.beans.BeanItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemsAdapter extends BaseAdapter {

	private ArrayList<BeanItems> listItems;
	private ViewHolder vHolder;

	public ItemsAdapter(Context context, ArrayList<BeanItems> listItems) {
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) parent.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(android.R.layout.simple_list_item_1,
					null);
			vHolder = new ViewHolder();
			vHolder.tv = (TextView) convertView
					.findViewById(android.R.id.text1);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		vHolder.tv.setText(listItems.get(position).name);
		return convertView;
	}

	class ViewHolder {
		public TextView tv;
	}
}
