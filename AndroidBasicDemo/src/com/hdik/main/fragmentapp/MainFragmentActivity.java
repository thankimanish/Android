package com.hdik.main.fragmentapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.hdik.main.R;
import com.hdik.main.common.library.BaseFragmentActivity;

public class MainFragmentActivity extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frag_activity);

		replaceFragment(new FirstFragment(), false, null);

	}

	protected void replaceFragment(Fragment fragment, boolean addToBackStack,
			String tag) {

		FragmentTransaction fTrasaction = getSupportFragmentManager()
				.beginTransaction();
		if (tag != null && !tag.equalsIgnoreCase("")) {
			fTrasaction.replace(R.id.container, fragment, tag);
		} else {
			fTrasaction.replace(R.id.container, fragment);
		}
		if (addToBackStack)
			fTrasaction.addToBackStack(null);
		fTrasaction.commit();

	}

}
