package com.hdik.main;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hdik.main.db.EmployeeMainActivity;
import com.hdik.main.fbdemo.FBLogin;
import com.hdik.main.fragmentapp.MainFragmentActivity;
import com.hdik.main.listviewfilter.ListViewFilterActivity;
import com.hdik.main.listviewmultichoice.ListViewMultiChoice;
import com.hdik.main.simpleparsing.ParsingActivity;
import com.hdik.main.tabfragment.main.FragmentTabHostActivity;
import com.hdik.main.twitterdemo.TwitterDemoActivity;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnParsingDemo, btnFbDemo, btnTwitterDemo, btnTabFrag,
			btnFrag, btnListViewFilter, btnListMulti, btnDatabaseDemo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		btnParsingDemo = (Button) findViewById(R.id.btnParsingDemo);
		btnFbDemo = (Button) findViewById(R.id.btnFbDemo);
		btnTwitterDemo = (Button) findViewById(R.id.btnTwitterDemo);
		btnTabFrag = (Button) findViewById(R.id.btnTabFrag);
		btnFrag = (Button) findViewById(R.id.btnFragmentDemo);
		btnListViewFilter = (Button) findViewById(R.id.btnListViewFilter);
		btnListMulti = (Button) findViewById(R.id.btnListViewMultiChoice);
		btnDatabaseDemo = (Button) findViewById(R.id.btnDataBaseDemo);
		btnListMulti.setOnClickListener(this);
		btnListViewFilter.setOnClickListener(this);
		btnFrag.setOnClickListener(this);
		btnParsingDemo.setOnClickListener(this);
		btnFbDemo.setOnClickListener(this);
		btnTwitterDemo.setOnClickListener(this);
		btnTabFrag.setOnClickListener(this);
		btnDatabaseDemo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnParsingDemo:
			startActivity(new Intent(this, ParsingActivity.class));
			break;
		case R.id.btnFbDemo:
			startActivity(new Intent(this, FBLogin.class));
			break;

		case R.id.btnTwitterDemo:
			startActivity(new Intent(this, TwitterDemoActivity.class));
			break;
		case R.id.btnTabFrag:
			startActivity(new Intent(this, FragmentTabHostActivity.class));
			break;
		case R.id.btnFragmentDemo:
			startActivity(new Intent(this, MainFragmentActivity.class));
			break;
		case R.id.btnListViewFilter:
			startActivity(new Intent(this, ListViewFilterActivity.class));
			break;
		case R.id.btnListViewMultiChoice:
			startActivity(new Intent(this, ListViewMultiChoice.class));
			break;
		case R.id.btnDataBaseDemo:
			startActivity(new Intent(this, EmployeeMainActivity.class));
			break;
		}
	}
}
