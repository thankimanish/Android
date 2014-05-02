package com.hdik.main.fbdemo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;


public class FbHandler {

	Activity activity;
	Bundle savedInstanceState;
	FbListner fbListner;
	WebDialog dialog;
	String dialogAction;
	Bundle dialogParams;

	private PendingAction pendingAction = PendingAction.NONE;

	HashMap<String, String> fbKeyValue;
	String fbUserId;

	private enum PendingAction {
		NONE, DOLOGIN, SHARE, INVITE_FRIEND
	}

	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	public FbHandler(Activity activity, Bundle savedInstanceState) {
		this.activity = activity;
		this.savedInstanceState = savedInstanceState;
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(activity, null,
						statusCallback, savedInstanceState);
			}
			if (session == null) {
				session = new Session(activity);
			}
			Session.setActiveSession(session);
		}

		if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
			session.openForRead(null);
		}
	}

	public boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		if (session != null) {
			if (session.isOpened())
				return true;
			else
				return false;

		} else {

			return false;
		}
	}

	public void loginFacebook() {
		Session session = Session.getActiveSession();

		System.out.println("isOpened : " + session.isOpened() + " isClose : "
				+ session.isClosed());
		if (!session.isOpened() && !session.isClosed()) {
			pendingAction = PendingAction.DOLOGIN;
			System.out.println("FbHandler > loginFacebook in if");

			// session.openForPublish(new Session.OpenRequest(activity)
			// .setCallback(statusCallback).setPermissions(
			// Arrays.asList("publish_stream,email,user_birthday,publish_actions,user_location,user_hometown,user_about_me,user_relationships")));

			session.openForRead(new Session.OpenRequest(activity)
					.setCallback(statusCallback)
					.setPermissions(
							Arrays.asList("email,user_birthday,user_location,user_hometown,user_about_me,user_relationships")));
		} else {
			System.out.println("FbHandler > loginFacebook in else");
			pendingAction = PendingAction.NONE;
			Session.openActiveSession(activity, true, statusCallback);
		}
	}

	public boolean logoutFacebook() {
		Session session = Session.getActiveSession();
		if (session != null) {
			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				return true;
			}

		}
		return false;

	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		try {
			if (pendingAction != PendingAction.NONE
					&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {

				Toast.makeText(
						activity,
						"Unable to perform selected action because permissions were not granted.",
						Toast.LENGTH_LONG).show();

				// handlePendingAction();
				// pendingAction = PendingAction.NONE;
			} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
				handlePendingAction();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("incomplete-switch")
	private void handlePendingAction() {
		try {
			PendingAction previouslyPendingAction = pendingAction;
			// These actions may re-set pendingAction if they are still pending,
			// but
			// we assume they
			// will succeed.
			pendingAction = PendingAction.NONE;

			switch (previouslyPendingAction) {
			case DOLOGIN:
				loginFacebook();
				break;
			case SHARE:
				postFbData(fbKeyValue);
				break;
			case INVITE_FRIEND:
				InviteFriend(fbUserId);
				break;
			}
		} catch (Exception e) {

		}
	}

	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	public void setFbListner(Activity activity) {
		fbListner = (FbListner) activity;
	}

	public void removeFbListner() {
		fbListner = null;
	}

	public void postFbData(HashMap<String, String> mapKeyValue) {

		try {
			fbKeyValue = mapKeyValue;

			Session session = Session.getActiveSession();
			if (hasPublishPermission()) {

				Request.Callback requestCallback = new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						// TODO Auto-generated method stub
						System.out.println("feed response" + response);
						if (fbListner != null) {
							fbListner.onFbPostSuccess(response);
						}
					}

				};

				Bundle postParams = new Bundle();

				for (String key : fbKeyValue.keySet()) {
					postParams.putString(key, fbKeyValue.get(key));
				}

				Request request = new Request(session, "me/feed", postParams,
						HttpMethod.POST, requestCallback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();

			} else {
				pendingAction = PendingAction.SHARE;
				session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
						activity, Arrays.asList("publish_actions")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getFBMe() {
		try {
			Session session = Session.getActiveSession();
			if (session != null) {

				Request user = Request.newMeRequest(session,
						new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {

								

								if (fbListner != null) {
									fbListner
											.onFbMeFetchSuccess(user, response);
								}
							}
						});
				Bundle paramsUser = new Bundle();
				paramsUser.putString("fields", "id,name,username");
				user.setParameters(paramsUser);
				user.executeAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void InviteFriend(String friendFbId) {
		try {
			fbUserId = friendFbId;
			Session session = Session.getActiveSession();
			if (session != null) {
				if (hasPublishPermission()
						&& session.getPermissions().contains("publish_stream")) {
					// try {

					Bundle params = new Bundle();
					params.putString("to", fbUserId);
					params.putString("message", "");
					params.putString(
							"description",
							"your description");
					params.putString("link", "http://www.xyz.com");
					params.putString(
							"picture",
							"http://www.xyz.com/abc.png");
					showFeedPostDialog(params, session);
					// Request req = Request.newPostRequest(session, "feed",
					// GraphObject.Factory.create(params),
					// new Request.Callback() {
					//
					// @Override
					// public void onCompleted(Response response) {
					//
					// if (fbListner != null) {
					// fbListner
					// .onFbInviteFriendSuccess(response);
					// }
					//
					// }
					// });
					//
					// Request.executeBatchAsync(req);

					// } catch (JSONException je) {
					// je.printStackTrace();
					// }
				} else {
					pendingAction = PendingAction.INVITE_FRIEND;
					session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
							activity, Arrays.asList("publish_actions",
									"publish_stream")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getFbFriends() {
		try {
			Session activeSession = Session.getActiveSession();
			if (activeSession.getState().isOpened()) {

				Request friendRequest = Request.newMyFriendsRequest(
						activeSession, new GraphUserListCallback() {

							@Override
							public void onCompleted(List<GraphUser> users,
									Response response) {
								// TODO Auto-generated method stub

								if (fbListner != null) {
									fbListner.onFbFetchFriendSuccess(users,
											response);
								}

							}
						});
				Bundle params = new Bundle();
				params.putString("fields", "id, name,picture,username");
				friendRequest.setParameters(params);
				friendRequest.executeAsync();

				// Request user = Request.newMeRequest(activeSession,
				// new Request.GraphUserCallback() {
				//
				// @Override
				// public void onCompleted(GraphUser user,
				// Response response) {
				//
				// if (user != null) {
				// BeanFbTwitter.setUsername(user.getUsername());
				// BeanFbTwitter.setFbName(user.getName());
				// }
				//
				// System.out.println("response" + response);
				// }
				// });
				// Bundle paramsUser = new Bundle();
				// paramsUser.putString("fields", "id,name,username");
				// user.setParameters(paramsUser);
				// user.executeAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showFeedPostDialog(Bundle params, Session session) {
		try {
			dialog = new WebDialog.FeedDialogBuilder(activity, session, params)
					.setOnCompleteListener(new WebDialog.OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {
							if (dialog != null && dialog.isShowing()) {
								dialog.dismiss();
							}
							dialog = null;
							if (fbListner != null) {
								fbListner
										.onFbInviteFriendSuccess(values, error);
							}

						}
					}).build();
			dialog.show();
		} catch (Exception e) {
			System.out
					.println("fb handler bad Token exception " + e.toString());
		}

	}
}