package com.hdik.main.db;

import com.hdik.main.R;
import com.hdik.main.common.utils.AndyUtils;
import com.hdik.main.db.bean.Employee;
import com.hdik.main.db.helper.DatabaseHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InsertEmployeeActivity extends Activity {
	EditText etAddress, etName, etPhone;
	Button btnAddEmp;
	DatabaseHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_emp_activity);
		dbHandler = new DatabaseHandler(InsertEmployeeActivity.this);
		etName = (EditText) findViewById(R.id.etEmpName);
		etAddress = (EditText) findViewById(R.id.etEmpAddress);
		etPhone = (EditText) findViewById(R.id.etEmpPhone);
		btnAddEmp = (Button) findViewById(R.id.btnAddEmployee);
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
					if (dbHandler.addEmployee(new Employee(strName, strAdd,
							strPhone)) != -1)
						;
					{
						AndyUtils.showToast("Employee Successfully Inserted",
								InsertEmployeeActivity.this);
						etName.setText("");
						etAddress.setText("");
						etPhone.setText("");
						etName.requestFocus();
					}
				} else {
					switch (valid) {
					case 0:
						etName.requestFocus();
						AndyUtils.showToast("Please Enter Name",
								InsertEmployeeActivity.this);
						break;
					case 1:
						etAddress.requestFocus();
						AndyUtils.showToast("Please Enter Address",
								InsertEmployeeActivity.this);
						break;
					case 2:
						etPhone.requestFocus();
						AndyUtils.showToast("Please Enter Phone",
								InsertEmployeeActivity.this);
						break;

					}
				}

			}
		});
	}
}
