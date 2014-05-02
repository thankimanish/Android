package com.hdik.main.qrcode;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ToggleButton;

public class QRCodeActivity extends Activity implements Camera.PreviewCallback {
	private CameraPreview mPreview;
	private RelativeLayout mLayout;
	private Handler mAutoFocusHandler;
	private Camera mCamera;
	private boolean mPreviewing = true;
	private ImageScanner mScanner;
	private Button btnCancel;
	private ToggleButton btnFlash;
	private LinearLayout.LayoutParams btnCancelParams;
	private LinearLayout.LayoutParams btnFlashParams;
	private RelativeLayout.LayoutParams linearLayoutParams;
	private LinearLayout ll;
	private WakeLock wakeLock;
	private final String WAKE_LOCK_TAG = "TORCH_WAKE_LOCK";

	private String symData = "";

	static {
		System.loadLibrary("iconv");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		btnFlash = (ToggleButton) new ToggleButton(QRCodeActivity.this);
		btnFlash.setTextOff("Flash off");
		btnFlash.setTextOn("Flash on");
		btnFlash.setChecked(false);
		btnFlash.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Parameters parameters = null;
					if (mCamera != null) {
						parameters = mCamera.getParameters();
						if (btnFlash.isChecked()) {
							parameters
									.setFlashMode(Parameters.FLASH_MODE_TORCH);
							mCamera.setParameters(parameters);
							startWakeLock();

						} else {
							parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
							mCamera.setParameters(parameters);
							stopWakeLock();
						}
					}

				} catch (RuntimeException re) {
					onBackPressed();
				}
			}
		});

		btnCancel = new Button(QRCodeActivity.this);
		btnCancel.setText("Exit");
		btnCancel.setTextSize(12);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				onBackPressed();

			}
		});
		btnCancelParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		btnCancelParams.weight = 1;
		btnCancelParams.setMargins(0, 9, 0, 0);
		btnCancel.setLayoutParams(btnCancelParams);

		btnFlashParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		btnFlashParams.weight = 1;
		btnFlash.setLayoutParams(btnFlashParams);
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.addView(btnCancel);
		ll.addView(btnFlash);
		linearLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		linearLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		ll.setLayoutParams(linearLayoutParams);

		// Hide status-bar
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Hide title-bar, must be before setContentView
		// requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Requires RelativeLayout.
		setupScanner();
		mLayout = new RelativeLayout(QRCodeActivity.this);
		mLayout.setBackgroundColor(Color.BLACK);

		mAutoFocusHandler = new Handler();

		setContentView(mLayout);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set the second argument by your choice.
		// Usually, 0 for back-facing camera, 1 for front-facing camera.
		// If the OS is pre-gingerbreak, this does not have any effect.
		mPreview = new CameraPreview(QRCodeActivity.this, 0,
				CameraPreview.LayoutMode.FitToParent, autoFocusCB,
				QRCodeActivity.this);
		LayoutParams previewLayoutParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// Un-comment below lines to specify the size.
		// previewLayoutParams.height = 500;
		// previewLayoutParams.width = 500;

		// Un-comment below line to specify the position.
		// mPreview.setCenterPosition(270, 130);
		previewLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mCamera = mPreview.getCamera();
		mPreviewing = true;

		// lay.addRule(RelativeLayout.CENTER_HORIZONTAL);

		mLayout.addView(mPreview, 0, previewLayoutParams);
		mLayout.addView(ll, linearLayoutParams);

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mPreview != null)
			if (mPreview.getCamera() != null) {
				mPreview.setPreviewCallback(null);
				mCamera.cancelAutoFocus();
				mPreview.stop();
				mPreview = null;
				mPreviewing = false;
				mCamera = null;

			}
		mLayout.removeAllViews();
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (mCamera != null && mPreviewing) {
				mCamera.autoFocus(autoFocusCB);
			}
		}
	};

	// Mimic continuous auto-focusing
	Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		// TODO Auto-generated method stub
		// Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
		Camera.Parameters parameters = camera.getParameters();
		Camera.Size size = parameters.getPreviewSize();

		Image barcode = new Image(size.width, size.height, "Y800");
		barcode.setData(data);

		int result = mScanner.scanImage(barcode);

		if (result != 0) {

			SymbolSet syms = mScanner.getResults();
			for (Symbol sym : syms) {
				symData = sym.getData();

			}

			mCamera.cancelAutoFocus();
			mCamera.setPreviewCallback(null);
			mPreview.stop();
			mPreviewing = false;
			mCamera = null;
			if (symData != null) {
				Toast.makeText(this, symData, Toast.LENGTH_LONG).show();
			}

		}

	}

	public void setupScanner() {
		mScanner = new ImageScanner();
		mScanner.setConfig(0, Config.X_DENSITY, 3);
		mScanner.setConfig(0, Config.Y_DENSITY, 3);

		int[] symbols = { Symbol.QRCODE };
		if (symbols != null) {
			mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
			for (int symbol : symbols) {
				mScanner.setConfig(symbol, Config.ENABLE, 1);
			}
		}
	}

	private void startWakeLock() {
		try {
			if (wakeLock == null) {

				PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						WAKE_LOCK_TAG);

			}
			wakeLock.acquire();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void stopWakeLock() {
		try {
			if (wakeLock != null) {
				wakeLock.release();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
