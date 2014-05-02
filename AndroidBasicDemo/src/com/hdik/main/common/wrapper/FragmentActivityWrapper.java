package com.hdik.main.common.wrapper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * @author Hardik A. Bhalodi
 *
 */
public class FragmentActivityWrapper {
	private int mFragmentId = 0;
	private String mFragmentTag = null;

	public void onActivityResult(FragmentActivity activity, int requestCode,
			int resultCode, Intent data) {
		Fragment fragment = null;
		if (mFragmentId > 0) {
			fragment = activity.getSupportFragmentManager().findFragmentById(
					mFragmentId);
		} else if (mFragmentTag != null && !mFragmentTag.equalsIgnoreCase("")) {
			fragment = activity.getSupportFragmentManager().findFragmentByTag(
					mFragmentTag);
		}
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	protected void startActivityForResult(FragmentActivity activity,
			Intent intent, int requestCode, int fragmentId) {
		mFragmentId = fragmentId;
		mFragmentTag = null;
		activity.startActivityForResult(intent, requestCode);
	}

	protected void startActivityForResult(FragmentActivity activity,
			Intent intent, int requestCode, String fragmentTag) {
		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		activity.startActivityForResult(intent, requestCode);
	}

	protected void startActivityForResult(FragmentActivity activity,Intent intent, int requestCode,
			int fragmentId, Bundle options) {
		mFragmentId = fragmentId;
		mFragmentTag = null;
		activity.startActivityForResult(intent, requestCode, options);
	}

	protected void startActivityForResult(FragmentActivity activity,Intent intent, int requestCode,
			String fragmentTag, Bundle options) {
		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		activity.startActivityForResult(intent, requestCode, options);
	}
}
