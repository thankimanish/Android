package com.hdik.main.common.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Hardik A. Bhalodi
 * 
 */
public abstract class BaseFragmentActivity extends FragmentActivity {
	private int mFragmentId = 0;
	private String mFragmentTag = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	final public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		Fragment fragment = null;
		if (mFragmentId > 0) {
			fragment = getSupportFragmentManager()
					.findFragmentById(mFragmentId);
		} else if (mFragmentTag != null && !mFragmentTag.equalsIgnoreCase("")) {
			fragment = getSupportFragmentManager().findFragmentByTag(
					mFragmentTag);
		}
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void startActivityForResult(Intent intent, int requestCode,
			int fragmentId) {
		mFragmentId = fragmentId;
		mFragmentTag = null;
		super.startActivityForResult(intent, requestCode);
	}

	public void startActivityForResult(Intent intent, int requestCode,
			String fragmentTag) {
		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startActivityForResult(intent, requestCode);
	}

	public void startActivityForResult(Intent intent, int requestCode,
			int fragmentId, Bundle options) {
		mFragmentId = fragmentId;
		mFragmentTag = null;
		super.startActivityForResult(intent, requestCode, options);
	}

	public void startActivityForResult(Intent intent, int requestCode,
			String fragmentTag, Bundle options) {
		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startActivityForResult(intent, requestCode, options);
	}

	@Override
	@Deprecated
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	@Deprecated
	public void startActivityForResult(Intent intent, int requestCode,
			Bundle options) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode, options);
	}

}
