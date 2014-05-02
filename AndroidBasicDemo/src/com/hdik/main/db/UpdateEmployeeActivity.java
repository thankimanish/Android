package com.hdik.main.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.hdik.main.R;
import com.hdik.main.common.utils.AndyUtils;
import com.hdik.main.db.adapter.EmployeeListAdapter;
import com.hdik.main.db.bean.Employee;
import com.hdik.main.db.helper.DatabaseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class UpdateEmployeeActivity extends Activity implements OnClickListener {
	EditText etAddress, etName, etPhone;
	Button btnAddEmp;
	DatabaseHandler dbHandler;
	Employee emp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_emp_activity);
		dbHandler = new DatabaseHandler(UpdateEmployeeActivity.this);
		emp = (Employee) getIntent().getSerializableExtra("emp");
		etName = (EditText) findViewById(R.id.etEmpName);
		etAddress = (EditText) findViewById(R.id.etEmpAddress);
		etPhone = (EditText) findViewById(R.id.etEmpPhone);
		btnAddEmp = (Button) findViewById(R.id.btnAddEmployee);
		etName.setText(emp.getName());
		etAddress.setText(emp.getAddress());
		etPhone.setText(emp.getPhone());
		btnAddEmp.setText("Update Employee");

		btnAddEmp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String strName = etName.getText().toString();
				String strAdd = etAddress.getText().toString();
				String strPhone = etPhone.getText().toString();
				final int valid = AndyUtils.isValidate(strName, strAdd,
						strPhone);
				if (valid == -1) {

					emp.setName(strName);
					emp.setAddress(strAdd);
					emp.setPhone(strPhone);
					dbHandler.updateEmployee(emp,
							String.valueOf(emp.getEmpId()));

					AndyUtils.showToast("Employee Successfully Updated",
							UpdateEmployeeActivity.this);
					finish();

				} else {
					switch (valid) {
					case 0:
						etName.requestFocus();
						AndyUtils.showToast("Please Enter Name",
								UpdateEmployeeActivity.this);
						break;
					case 1:
						etAddress.requestFocus();
						AndyUtils.showToast("Please Enter Address",
								UpdateEmployeeActivity.this);
						break;
					case 2:
						etPhone.requestFocus();
						AndyUtils.showToast("Please Enter Phone",
								UpdateEmployeeActivity.this);
						break;

					}
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
