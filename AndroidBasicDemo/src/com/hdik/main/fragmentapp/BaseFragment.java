package com.hdik.main.fragmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseFragment extends Fragment implements OnClickListener {
	protected MainFragmentActivity mMainActivity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		mMainActivity=(MainFragmentActivity) getActivity();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	public void startActivityForResult(Intent intent, int requestCode,int fragmentId) {
		mMainActivity.startActivityForResult(intent, requestCode,fragmentId);
		//mMainActivity.mFwrapper.startActivityForResult(mMainActivity, intent, requestCode, fragmentId);
	}
	public void startActivityForResult(Intent intent, int requestCode,String fragmentTag) {
		mMainActivity.startActivityForResult(intent, requestCode,fragmentTag);
		//mMainActivity.mFwrapper.startActivityForResult(mMainActivity, intent, requestCode, fragmentTag);
	}
	public void startActivityForResult(Intent intent, int requestCode,String fragmentTag,Bundle options) {
		mMainActivity.startActivityForResult(intent, requestCode,fragmentTag,options);
	}
	public void startActivityForResult(Intent intent, int requestCode,int fragmentId,Bundle options) {
		mMainActivity.startActivityForResult(intent, requestCode,fragmentId,options);
	}
	@Override
	@Deprecated
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	

}
