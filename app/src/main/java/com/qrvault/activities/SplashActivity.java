package com.qrvault.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qrvault.R;

public class SplashActivity extends AppCompatActivity {

	private static final int SPLASH_DURATION = 2200; // Total screen time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		ImageView logo = findViewById(R.id.imgLogo);
		TextView appName = findViewById(R.id.txtAppName);
		TextView tagline = findViewById(R.id.txtTagline);

		// Load animations
		Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.splash_combo);
		Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up_fancy);

		// Start animations with delay for cascading effect
		logo.startAnimation(logoAnim);

		new Handler().postDelayed(() -> appName.startAnimation(textAnim), 400);
		new Handler().postDelayed(() -> tagline.startAnimation(textAnim), 800);

		// Move to MainActivity after SPLASH_DURATION
		new Handler().postDelayed(() -> {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
			finish();
		}, SPLASH_DURATION);
	}
}
