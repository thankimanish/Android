package com.hdik.main.db;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hdik.main.R;

public class EmployeeMainActivity extends Activity implements OnClickListener {
	Button btnInsert, btnView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.employee_activity);
		btnInsert = (Button) findViewById(R.id.btnInsert);

		btnView = (Button) findViewById(R.id.btnView);
		btnInsert.setOnClickListener(this);

		btnView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnInsert:
			startActivity(new Intent(EmployeeMainActivity.this,
					InsertEmployeeActivity.class));
			break;

		case R.id.btnView:
			startActivity(new Intent(EmployeeMainActivity.this,
					ViewEmployeeActivity.class));
			break;

		}
	}

}
