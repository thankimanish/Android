package com.hdik.main.tabfragment.firsttab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hdik.main.R;
import com.hdik.main.tabfragment.main.BaseFragment;

public class SecondScreen extends BaseFragment implements OnClickListener  {
	private Button backButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab1_secodscreen,
				container, false);

		backButton = (Button) view.findViewById(R.id.btnBack);
		backButton.setOnClickListener(this);
		System.out.println("replace");
		return view;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		fragmentTabActivity.onBackPressed();
		
	}
}
