package com.hdik.main.fragmentapp;
//package com.example.fragmentapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentTransaction;
//
//public class CopyOfMainActivity extends FragmentActivity {
//
//	FragmentActivityWrapper mFwrapper;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);
//		mFwrapper=new FragmentActivityWrapper();
//		replaceFragment(new FirstFragment(), false, null);
//
//	}
//
//	protected void replaceFragment(Fragment fragment, boolean addToBackStack,
//			String tag) {
//
//		FragmentTransaction fTrasaction = getSupportFragmentManager()
//				.beginTransaction();
//		if (tag != null && !tag.equalsIgnoreCase("")) {
//			fTrasaction.replace(R.id.container, fragment, tag);
//		} else {
//			fTrasaction.replace(R.id.container, fragment);
//		}
//		if (addToBackStack)
//			fTrasaction.addToBackStack(null);
//		fTrasaction.commit();
//
//	}
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		mFwrapper.onActivityResult(this, requestCode, resultCode, data);
//	}
//
//}
