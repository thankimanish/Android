package com.hdik.main.twitterdemo;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class TwitterHandler {

	private Twitter mTwitter;
	private RequestToken mRequestToken;
	private Activity activity;
	private Context context;
	ConfigurationBuilder confbuilder;
	Configuration conf;
	ProgressDialog pDialog;
	private TwitterListener twitterListener;

	public TwitterHandler(Activity activity) {
		this.activity = activity;
		this.context = activity.getApplicationContext();
		this.twitterListener = (TwitterListener) activity;

	}

	public void LoginTwitter() {
		confbuilder = new ConfigurationBuilder();
		conf = confbuilder.setOAuthConsumerKey(Const.CONSUMER_KEY)
				.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
		mTwitter = new TwitterFactory(conf).getInstance();
		mTwitter.setOAuthAccessToken(null);

		new AsyncTask<Void, Void, RequestToken>() {
			protected void onPreExecute() {
				pDialog = new ProgressDialog(activity);
				pDialog.setMessage("Loading...");
				pDialog.show();
			};

			@Override
			protected RequestToken doInBackground(Void... params) {
				// TODO Auto-generated method stub
				try {
					mRequestToken = mTwitter
							.getOAuthRequestToken(Const.CALLBACK_URL);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return mRequestToken;
			}

			protected void onPostExecute(RequestToken mRequestToken) {
				if (pDialog.isShowing()) {

					pDialog.dismiss();
					pDialog = null;
				}
				if (mRequestToken != null
						&& mRequestToken.getAuthorizationURL() != null) {
					Intent intent = new Intent(activity, TwitterLogin.class);
					intent.putExtra(Const.IEXTRA_AUTH_URL,
							mRequestToken.getAuthorizationURL());
					activity.startActivityForResult(intent, 0);
				} else {
					Toast.makeText(
							activity,
							"not able to connect this time due to low internet conntection...",
							Toast.LENGTH_SHORT).show();
				}
			};

		}.execute();

	}

	public void LogoutTwitter() {

		SharedPreferences pref = context.getSharedPreferences(Const.PREF_NAME,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(Const.PREF_KEY_ACCESS_TOKEN);
		editor.remove(Const.PREF_KEY_ACCESS_TOKEN_SECRET);
		editor.commit();

		if (mTwitter != null) {
			mTwitter.shutdown();

		}

		Toast.makeText(activity, "Successfully logged out", Toast.LENGTH_SHORT)
				.show();
	}

	public void twitt(final String msg) {

		if (mTwitter == null) {
			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			Configuration conf = confbuilder
					.setOAuthConsumerKey(Const.CONSUMER_KEY)
					.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
			mTwitter = new TwitterFactory(conf).getInstance();
		}

		SharedPreferences pref = context.getSharedPreferences(Const.PREF_NAME,
				Context.MODE_PRIVATE);
		String accessToken = pref.getString(Const.PREF_KEY_ACCESS_TOKEN, null);
		String accessTokenSecret = pref.getString(
				Const.PREF_KEY_ACCESS_TOKEN_SECRET, null);
		if (accessToken == null || accessTokenSecret == null) {
			Toast.makeText(activity, "please Login first", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		mTwitter.setOAuthAccessToken(new AccessToken(accessToken,
				accessTokenSecret));

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = true;
				try {
					mTwitter.updateStatus(msg);

				} catch (TwitterException e) {
					e.printStackTrace();
					success = false;
				}

				final boolean finalSuccess = success;

				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (twitterListener != null) {
							twitterListener.onTwittCompleted(finalSuccess);
						}

					}
				});

			}
		}).start();

	}

	public void inviteFriends(String id, String msg) {
		try {
			if (mTwitter == null) {
				ConfigurationBuilder confbuilder = new ConfigurationBuilder();
				Configuration conf = confbuilder
						.setOAuthConsumerKey(Const.CONSUMER_KEY)
						.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
				mTwitter = new TwitterFactory(conf).getInstance();
			}

			SharedPreferences pref = context.getSharedPreferences(
					Const.PREF_NAME, Context.MODE_PRIVATE);
			String accessToken = pref.getString(Const.PREF_KEY_ACCESS_TOKEN,
					null);
			String accessTokenSecret = pref.getString(
					Const.PREF_KEY_ACCESS_TOKEN_SECRET, null);
			if (accessToken == null || accessTokenSecret == null) {
				Toast.makeText(activity, "please Login first",
						Toast.LENGTH_SHORT).show();
				return;
			}
			mTwitter.setOAuthAccessToken(new AccessToken(accessToken,
					accessTokenSecret));

			System.out.println("idddddd: " + id);
			mTwitter.sendDirectMessage(Long.parseLong(id), msg);

			Toast.makeText(activity, "Your Invitation has been sent!",
					Toast.LENGTH_SHORT).show();

		} catch (TwitterException e) {
			e.printStackTrace();
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
		}
	}

	public boolean isLoggedInTwitter() {

		SharedPreferences pref = context.getSharedPreferences(Const.PREF_NAME,
				Context.MODE_PRIVATE);
		String accessToken = pref.getString(Const.PREF_KEY_ACCESS_TOKEN, null);
		String accessTokenSecret = pref.getString(
				Const.PREF_KEY_ACCESS_TOKEN_SECRET, null);
		if (accessToken == null || accessTokenSecret == null) {

			return false;
		} else {
			return true;
		}
	}

	public void storeData(Intent intent) {
		AccessToken accessToken = null;
		try {
			String oauthVerifier = intent.getExtras().getString(
					Const.IEXTRA_OAUTH_VERIFIER);
			accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
					oauthVerifier);
			SharedPreferences pref = context.getSharedPreferences(
					Const.PREF_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(Const.PREF_KEY_ACCESS_TOKEN,
					accessToken.getToken());
			editor.putString(Const.PREF_KEY_ACCESS_TOKEN_SECRET,
					accessToken.getTokenSecret());
			editor.commit();

		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentUserScreenName() {

		try {
			if (mTwitter == null) {
				ConfigurationBuilder confbuilder = new ConfigurationBuilder();
				Configuration conf = confbuilder
						.setOAuthConsumerKey(Const.CONSUMER_KEY)
						.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
				mTwitter = new TwitterFactory(conf).getInstance();
			}
			SharedPreferences pref = context.getSharedPreferences(
					Const.PREF_NAME, Context.MODE_PRIVATE);
			String accessToken = pref.getString(Const.PREF_KEY_ACCESS_TOKEN,
					null);
			String accessTokenSecret = pref.getString(
					Const.PREF_KEY_ACCESS_TOKEN_SECRET, null);
			if (accessToken == null || accessTokenSecret == null) {
				Toast.makeText(activity, "please Login first",
						Toast.LENGTH_SHORT).show();
				return null;
			}

			mTwitter.setOAuthAccessToken(new AccessToken(accessToken,
					accessTokenSecret));
			return mTwitter.getScreenName();

		} catch (TwitterException te) {
			te.printStackTrace();

		}
		return null;
	}

}
