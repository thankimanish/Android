package com.hdik.main.twitterdemo;

import com.hdik.main.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TwitterDemoActivity extends Activity implements OnClickListener,
		TwitterListener {
	Button btnLoginTwitter, btnPost;
	EditText etPost;
	TwitterHandler handler;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.twitter_activity);
		btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);

		btnPost = (Button) findViewById(R.id.btnTwitt);
		etPost = (EditText) findViewById(R.id.etTwitt);
		handler = new TwitterHandler(TwitterDemoActivity.this);
		btnLoginTwitter.setOnClickListener(this);
		btnPost.setOnClickListener(this);
		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		updateUI();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLoginTwitter:
			if (btnLoginTwitter.getText().equals("Login")) {
				handler.LoginTwitter();
			} else {
				handler.LogoutTwitter();
				updateUI();
			}
			break;
		case R.id.btnTwitt:
			if (handler.isLoggedInTwitter())
				if (!etPost.getText().toString().trim().equalsIgnoreCase("")) {
					handler.twitt(etPost.getText().toString());
				} else {
					Toast.makeText(this, "Please Enter Message",
							Toast.LENGTH_SHORT).show();
				}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == RESULT_OK) {
			handler.storeData(data);
			updateUI();
		}

	}

	@Override
	public void onTwittCompleted(boolean response) {
		// TODO Auto-generated method stub
		if (response) {
			Toast.makeText(this, "Your Twitt Successfully Posted!",
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "Twitt Failed, may be duplicate twitt!",
					Toast.LENGTH_LONG).show();
		}
	}

	private void updateUI() {
		if (handler.isLoggedInTwitter()) {
			btnPost.setEnabled(true);
			etPost.setEnabled(true);
			btnLoginTwitter.setText("Logout");
		} else {
			btnPost.setEnabled(false);
			etPost.setEnabled(false);
			btnLoginTwitter.setText("Login");
		}
	}
}
