package com.hdik.main.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hdik.main.R;
import com.hdik.main.common.utils.AndyUtils;
import com.hdik.main.db.adapter.EmployeeListAdapter;
import com.hdik.main.db.bean.Employee;
import com.hdik.main.db.helper.DatabaseHandler;

public class ViewEmployeeActivity extends Activity implements OnClickListener,
		OnItemLongClickListener,OnItemClickListener {
	ListView listViewEmp;
	EmployeeListAdapter adapter;
	DatabaseHandler dbHandler;
	Button btnasc, btndesc;
	private ArrayList<Employee> eList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_emp_activity);
		btnasc = (Button) findViewById(R.id.btnAscending);
		btndesc = (Button) findViewById(R.id.btnDescending);
		btnasc.setOnClickListener(this);
		btndesc.setOnClickListener(this);
		dbHandler = new DatabaseHandler(this);
		listViewEmp = (ListView) findViewById(R.id.listViewEmployee);
		listViewEmp.setOnItemLongClickListener(this);
		listViewEmp.setOnItemClickListener(this);
		

	}
	@Override
	protected void onResume() {
		super.onResume();
		AndyUtils.showSimpleProgressDialog(this, "", "Getting Records", false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				eList = dbHandler.getEmployees();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (eList != null) {
							adapter = new EmployeeListAdapter(
									ViewEmployeeActivity.this, eList);
							listViewEmp.setAdapter(adapter);
						}
						AndyUtils.removeSimpleProgressDialog();
					}
				});
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.btnAscending:
			if (eList != null && eList.size() > 1) {

				Collections.sort(eList, new Comparator<Employee>() {

					@Override
					public int compare(Employee lhs, Employee rhs) {
						// TODO Auto-generated method stub

						try {

							String namelhs = lhs.getName();
							String namerhs = rhs.getName();
							return namelhs.compareTo(namerhs);
						} catch (NumberFormatException ne) {
							ne.printStackTrace();
						}
						return 0;
					}

				});
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.btnDescending:
			if (eList != null && eList.size() > 1) {

				Collections.sort(eList, new Comparator<Employee>() {

					@Override
					public int compare(Employee lhs, Employee rhs) {
						// TODO Auto-generated method stub

						try {

							String namelhs = lhs.getName();
							String namerhs = rhs.getName();
							return namerhs.compareTo(namelhs);
						} catch (NumberFormatException ne) {
							ne.printStackTrace();
						}
						return 0;
					}

				});
				adapter.notifyDataSetChanged();
			}
			break;

		}

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos,
			long arg3) {
		// TODO Auto-generated method stub
		final int position = pos;
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage("Delete this Record?");
		dialog.setCancelable(false);
		dialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dbHandler.deleteEmployee(eList.get(position));
						eList.remove(position);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								adapter.notifyDataSetChanged();
							}
						});
					}
				}).start();
				
			}
		});
		dialog.show();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent updateEmp=new Intent(ViewEmployeeActivity.this,UpdateEmployeeActivity.class);
		updateEmp.putExtra("emp", eList.get(arg2));
		startActivity(updateEmp);
	}
	
}
