package com.hdik.main.db.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hdik.main.R;
import com.hdik.main.db.bean.Employee;

public class EmployeeListAdapter extends BaseAdapter {

	ArrayList<Employee> empList;
	Context context;
	ViewHolder holder;

	public EmployeeListAdapter(Context context, ArrayList<Employee> empList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.empList = empList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return empList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return empList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.emp_llist_ayout, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvListEmpName);
			holder.tvPhone = (TextView) convertView
					.findViewById(R.id.tvListEmpPhone);
			holder.tvAddress = (TextView) convertView
					.findViewById(R.id.tvListEmpAddress);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(empList.get(position).getName());
		holder.tvAddress.setText(empList.get(position).getAddress());
		holder.tvPhone.setText(empList.get(position).getPhone());
		return convertView;
	}

	class ViewHolder {
		TextView tvName;
		TextView tvAddress;
		TextView tvPhone;
	}
}
