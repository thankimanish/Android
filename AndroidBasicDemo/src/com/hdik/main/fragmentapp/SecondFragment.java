package com.hdik.main.fragmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hdik.main.R;

public class SecondFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.first_frag, container, false);
		TextView tv = (TextView) v.findViewById(R.id.textView1);
		TextView tv2 = (TextView) v.findViewById(R.id.textView);
		tv2.setVisibility(View.GONE);
		tv.setOnClickListener(this);
		tv.setText("Click to Choose Image");
		return v;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				11, "MY_FRAG");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Toast.makeText(mMainActivity, "okkkkk" + requestCode, Toast.LENGTH_LONG)
				.show();
	}
}
