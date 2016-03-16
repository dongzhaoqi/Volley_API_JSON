package com.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.city.list.main.R;

public class CustomProgressDialog extends ProgressDialog {

	private Animation animRotate;
	private ImageView ivProgress;
	private TextView tvProgress;
	private String message;

	public CustomProgressDialog(Context context, String message) {
		super(context);
		this.message = message;

		animRotate = AnimationUtils.loadAnimation(context,
				R.anim.custom_progress_dialog);
		setIndeterminate(true);
		setCancelable(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_progress_dialog);
		ivProgress = (ImageView) findViewById(R.id.ivProgress);
		tvProgress = (TextView) findViewById(R.id.tvProgress);
		tvProgress.setText(message);
	}

	@Override
	public void show() {
		super.show();
		ivProgress.startAnimation(animRotate);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		animRotate.cancel();
	}
}
