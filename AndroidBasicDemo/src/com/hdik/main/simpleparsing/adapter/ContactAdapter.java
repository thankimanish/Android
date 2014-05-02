package com.hdik.main.simpleparsing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdik.main.simpleparsing.beans.BeanContact;

public class ContactAdapter extends BaseAdapter {

	private ArrayList<BeanContact> listContact;
	private ViewHolder vHolder;

	public ContactAdapter(Context context, ArrayList<BeanContact> listContact) {
		this.listContact = listContact;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listContact.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listContact.get(position);
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
		vHolder.tv.setText(listContact.get(position).name);
		return convertView;
	}

	class ViewHolder {
		public TextView tv;
	}
}
